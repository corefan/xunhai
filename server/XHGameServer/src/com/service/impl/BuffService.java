package com.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cache.BaseCacheService;
import com.common.GameContext;
import com.common.GameSocketService;
import com.common.LockService;
import com.common.ServiceCollection;
import com.constant.BattleConstant;
import com.constant.BuffConstant;
import com.constant.CacheConstant;
import com.constant.ConfigConstant;
import com.constant.LockConstant;
import com.constant.PlayerConstant;
import com.constant.ProdefineConstant;
import com.dao.buff.BaseBuffDAO;
import com.domain.MessageObj;
import com.domain.buff.BaseBuff;
import com.domain.buff.Buff;
import com.domain.player.PlayerExt;
import com.domain.puppet.BasePuppet;
import com.domain.puppet.PlayerPuppet;
import com.message.BuffProto.S_SynBuff;
import com.message.MessageProto.MessageEnum.MessageID;
import com.service.IBattleService;
import com.service.IBuffService;
import com.service.ICommonService;
import com.service.IFashionService;
import com.service.IPlayerService;
import com.service.IProtoBuilderService;
import com.service.ISceneService;
import com.service.ITeamService;
import com.util.LogUtil;
import com.util.SerialNumberUtil;
import com.util.SplitStringUtil;

/**
 * buff系统
 * @author ken
 * @date 2017-4-1
 */
public class BuffService implements IBuffService {

	private BaseBuffDAO baseBuffDAO = new BaseBuffDAO();
	
	@Override
	public void initBaseCache() {
		Map<Integer, BaseBuff> baseBuffMap = new HashMap<Integer, BaseBuff>();
		List<BaseBuff> buffs = baseBuffDAO.listBaseBuffs();
		for(BaseBuff model : buffs){
			model.setEffectProList(SplitStringUtil.getIntIntList(model.getEffectPro()));
			List<Integer> rlist = SplitStringUtil.getIntList(model.getRemoveType());

			model.setRemoveTypeList(rlist);
			
			baseBuffMap.put(model.getBuffId(), model);
		}
		
		BaseCacheService.putToBaseCache(CacheConstant.BASE_BUFF, baseBuffMap);
	}
	
	/**
	 * 取出buff配置
	 */
	@SuppressWarnings("unchecked")
	@Override
	public BaseBuff getBaseBuff(int buffId){
		Map<Integer, BaseBuff> baseBuffMap  = (Map<Integer, BaseBuff>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_BUFF);
		return baseBuffMap.get(buffId);
	}

	@Override
	public List<Buff> addBuff(BasePuppet fighter, BasePuppet target, List<Integer> buffIdList) {
		
		List<Buff> lists = new ArrayList<Buff>();		
		Map<Integer, Buff> buffMap = target.getBuffMap();
		
		for(Integer buffId : buffIdList){
			BaseBuff baseBuff = this.getBaseBuff(buffId);
			if(baseBuff == null){
				LogUtil.error("baseBuff is null with id is "+buffId);
				continue;
			}
			if(target.getImmuneDebuff() == 1 && baseBuff.getImmuneDebuff() == 1){
				//免疫debuff
				continue;
			}
			
			boolean bFind = false;
			Iterator<Map.Entry<Integer, Buff>> iterator = buffMap.entrySet().iterator();
			while(iterator.hasNext()){
				Map.Entry<Integer, Buff> entry = iterator.next();
				Buff buff = entry.getValue();
				BaseBuff bBuff = this.getBaseBuff(buff.getBuffId());	
				
				if(baseBuff.getGroupId() == bBuff.getGroupId() && baseBuff.getReplace() == 1){					
					if(baseBuff.getBuffLv() < bBuff.getBuffLv()){
						// 如果已存在同组等级更高的buff,那么低级buff则不再做处理
						bFind = true;	
						break;
					}
					
					//如果是属性buff， 则要移除之前添加的属性
					if(baseBuff.getType() == BuffConstant.TYPE_PROPERTY){
						this.changeProValue(target, buff, -1, false);
					}else if(baseBuff.getType() == BuffConstant.TYPE_ADDEXP){
						this.addBuffValue(fighter, target, bBuff, buff, false);
					}
					
					buff.setEndTime(0);
					buff.setUpdateTime(0);
					buff.setPeriodTime(0);
					iterator.remove();
					lists.add(buff);
					
					break;
				}	
			}
			
			if(bFind) continue;
			
			long currentTime = System.currentTimeMillis();			
			Buff buff = new Buff();
			buff.setId(SerialNumberUtil.getBuffId());
			buff.setFighter(fighter);
			buff.setTargetGuid(target.getGuid());
			buff.setBuffId(buffId);
			buff.setType(baseBuff.getType());
			if(baseBuff.getRemainTime() > 0){
				buff.setEndTime(currentTime + baseBuff.getRemainTime());
			}else{
				buff.setEndTime(baseBuff.getRemainTime());
			}
			
			if(baseBuff.getPeriodTime() > 0){
				buff.setPeriodTime(baseBuff.getPeriodTime());
				buff.setUpdateTime(currentTime);
			}
			buffMap.put(buff.getId(), buff);
			
			this.addBuffValue(fighter, target, baseBuff, buff, true);
			lists.add(buff);			
		}
		
		return lists;
	}

	/**
	 * buff数值产生
	 */
	private void addBuffValue(BasePuppet fighter, BasePuppet target, BaseBuff baseBuff, Buff buff, boolean addFlag){
		if(target == null){
			LogUtil.warn("target is null with buffId is " + buff.getBuffId());
			return;
		}
		if(baseBuff.getType() == BuffConstant.TYPE_PROPERTY){
			if(addFlag){
				if(buff.getAddValueMap().isEmpty()){
					List<List<Integer>> effectProList = baseBuff.getEffectProList();
					for(List<Integer> lists : effectProList){
						int type = lists.get(0);
						int propertyId = lists.get(1);
						double percent = lists.get(2) *0.01;
						int addValue = lists.get(3);
						int proId = lists.get(4);
						int proValue = 0;
						if(type == 1){
							proValue = this.calAddValue(fighter, propertyId, percent, addValue, buff);
						}else{
							proValue = this.calAddValue(target, propertyId, percent, addValue, buff);
						}
						if(proValue != 0){
							buff.getAddValueMap().put(proId, proValue);
						}
						
					}
				}
				this.changeProValue(target, buff, 1, true);
			}else{
				this.changeProValue(target, buff, -1, true);
			}

		}else if(baseBuff.getType() == BuffConstant.TYPE_FIXED){
			int flag = addFlag?1:0;
			if(target.getFixed() != flag){
				target.setFixed(flag);
			}
		}else if(baseBuff.getType() == BuffConstant.TYPE_VERTIGO){
			int flag = addFlag?1:0;
			if(target.getVertigo() != flag){
				target.setVertigo(flag);
			}
		}else if(baseBuff.getType() == BuffConstant.TYPE_INVISIBLE){
			int flag = addFlag?1:0;
			if(target.getInvisible() != flag){
				target.setInvisible(flag);
			}
		}else if(baseBuff.getType() == BuffConstant.TYPE_PURGE){
			
		}else if(baseBuff.getType() == BuffConstant.TYPE_DISPEL){
			
		}else if(baseBuff.getType() == BuffConstant.TYPE_ADDEXP){
			int flag = addFlag?1:0;
			List<List<Integer>> effectProList = baseBuff.getEffectProList();
			for(List<Integer> lists : effectProList){
				if(flag == 1){
					target.setExpRate(target.getExpRate() + lists.get(0));
				}else{
					target.setExpRate(target.getExpRate() - lists.get(0));
				}
			}
			
		}else if(baseBuff.getType() == BuffConstant.TYPE_IMMUNE){
			int flag = addFlag?1:0;
			if(target.getImmune() != flag){
				target.setImmune(flag);
			}
		}
	}
	
	/**
	 * 影响属性计算 取目标或者自己的属性进行运算
	 */
	private int calAddValue(BasePuppet basePuppet, int propertyId, double percent, int addValue, Buff buff){
		int value = 0;
		switch (propertyId) {
		case ProdefineConstant.HP_MAX_PANEL:
			value = basePuppet.getHpMax();
			break;
		case ProdefineConstant.MP_MAX_PANEL:
			value = basePuppet.getMpMax();
			break;
		case ProdefineConstant.P_ATTACK_PANEL:
			value = basePuppet.getP_attack();
			break;
		case ProdefineConstant.M_ATTACK_PANEL:
			value = basePuppet.getM_attack();
			break;
		case ProdefineConstant.P_DAMAGE_PANEL:
			value = basePuppet.getP_damage();
			break;
		case ProdefineConstant.M_DAMAGE_PANEL:
			value = basePuppet.getM_damage();
			break;
		case ProdefineConstant.CRIT_PANEL:
			value = basePuppet.getCrit();
			break;
		case ProdefineConstant.TOUGH_PANEL:
			value = basePuppet.getTough();
			break;
		case ProdefineConstant.DMG_DEEP_PER_PANEL:
			value = basePuppet.getDmgDeepPer();
			break;
		case ProdefineConstant.DMG_REDUCT_PER_PANEL:
			value = basePuppet.getDmgReductPer();
			break;
		case ProdefineConstant.DMG_CRIT_PER_PANEL:
			value = basePuppet.getDmgCritPer();
			break;
		case ProdefineConstant.MOVE_SPEED_PANEL:
			value = basePuppet.getMoveSpeed();
			break;
		case ProdefineConstant.HP:
			value = basePuppet.getHp();
			break;
		case ProdefineConstant.MP:
			value = basePuppet.getMp();
			break;
		case ProdefineConstant.HP_MAX:
			value = basePuppet.getHpMax();
			break;
		case ProdefineConstant.MP_MAX:
			value = basePuppet.getMpMax();
			break;
		case ProdefineConstant.MOVE_SPEED:
			value = basePuppet.getMoveSpeed();
			break;

		default:
			value = 0;
			break;
		}
		return (int)Math.ceil(value * percent) + addValue;
	}
	
	/**
	 * buff属性添加移除
	 */
	private void changeProValue(BasePuppet basePuppet, Buff buff, int sign, boolean offer){
		Map<Integer, Integer> toSceneProMap = new HashMap<Integer, Integer>(); //同步玩家属性for场景
		
		for(Map.Entry<Integer, Integer> entry : buff.getAddValueMap().entrySet()){
			int propertyId = entry.getKey();
			int addValue = entry.getValue() * sign;
			switch (propertyId) {
			case ProdefineConstant.HP_MAX_PANEL:
				basePuppet.setHpMax(basePuppet.getHpMax() + addValue);
				if(offer){
					toSceneProMap.put(ProdefineConstant.HP_MAX, basePuppet.getHpMax());
				}
				break;
			case ProdefineConstant.MP_MAX_PANEL:
				basePuppet.setMpMax(basePuppet.getMpMax() + addValue);
				if(offer){
					toSceneProMap.put(ProdefineConstant.MP_MAX, basePuppet.getMpMax());
				}
				break;
			case ProdefineConstant.P_ATTACK_PANEL:
				basePuppet.setP_attack(basePuppet.getP_attack() + addValue);
				break;
			case ProdefineConstant.M_ATTACK_PANEL:
				basePuppet.setM_attack(basePuppet.getM_attack() + addValue);
				break;
			case ProdefineConstant.P_DAMAGE_PANEL:
				basePuppet.setP_damage(basePuppet.getP_damage() + addValue);
				break;
			case ProdefineConstant.M_DAMAGE_PANEL:
				basePuppet.setM_damage(basePuppet.getM_damage() + addValue);
				break;
			case ProdefineConstant.CRIT_PANEL:
				basePuppet.setCrit(basePuppet.getCrit() + addValue);
				break;
			case ProdefineConstant.TOUGH_PANEL:
				basePuppet.setTough(basePuppet.getTough() + addValue);
				break;
			case ProdefineConstant.DMG_DEEP_PER_PANEL:
				basePuppet.setDmgDeepPer(basePuppet.getDmgDeepPer() + addValue);
				break;
			case ProdefineConstant.DMG_REDUCT_PER_PANEL:
				basePuppet.setDmgReductPer(basePuppet.getDmgReductPer() + addValue);
				break;
			case ProdefineConstant.DMG_CRIT_PER_PANEL:
				basePuppet.setDmgCritPer(basePuppet.getDmgCritPer() + addValue);
				break;
			case ProdefineConstant.MOVE_SPEED_PANEL:
				basePuppet.setMoveSpeed(basePuppet.getMoveSpeed() + addValue);
				if(offer){
					toSceneProMap.put(ProdefineConstant.MOVE_SPEED, basePuppet.getMoveSpeed());
				}
				break;

			default:
				break;
			}
		}
		if(offer){
			if(!toSceneProMap.isEmpty() && basePuppet.getSceneGuid() != null){
				GameContext.getInstance().getServiceCollection().getPlayerService().synPlayerPropertyToAll(basePuppet, toSceneProMap);
			}
		}
	}
	
	@Override
	public void dealBuff(BasePuppet basePuppet) {
		Map<Integer, Buff> buffMap = basePuppet.getBuffMap();
		if(buffMap.isEmpty()) return;
		
		try {
			long currentTime = System.currentTimeMillis();
			
			Set<Buff> changeBuffList = new HashSet<Buff>();
			
			Iterator<Map.Entry<Integer, Buff>> iterator = buffMap.entrySet().iterator();
			while(iterator.hasNext()){
				Map.Entry<Integer, Buff> entry = iterator.next();
				Buff buff = entry.getValue();
				
				if(buff.getPeriodTime() > 0 && currentTime - buff.getUpdateTime() >= buff.getPeriodTime()){
					//持续buff 
					buff.setUpdateTime(currentTime);
					changeBuffList.add(buff);
					
					this.dealAbidanceBuff(basePuppet, buff);
				}
				
				if(buff.getEndTime() > 0 && currentTime >= buff.getEndTime()){						
					//buff过期
					buff.setEndTime(0);	
					
					changeBuffList.add(buff);
					iterator.remove();
					
					BaseBuff baseBuff = this.getBaseBuff(buff.getBuffId());
					this.addBuffValue(basePuppet, basePuppet, baseBuff, buff, false);
				}
			}
			
			if(!changeBuffList.isEmpty()){
				this.synBuff(basePuppet, changeBuffList);
			}			
		} catch (Exception e) {
			LogUtil.error("buff结算异常:"+basePuppet.getGuid(),e);
			
			basePuppet.getBuffMap().clear();
		}
	}

	/**
	 *	处理持续buff
	 */
	private void dealAbidanceBuff(BasePuppet basePuppet, Buff buff){
		if(basePuppet == null || basePuppet.getState() == BattleConstant.STATE_DEAD) return;

		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		ICommonService commonService = serviceCollection.getCommonService();
		IBattleService battleService = serviceCollection.getBattleService();
		ITeamService teamService = serviceCollection.getTeamService();
		
		int AUTO_ADD_HP_MP = commonService.getConfigValue(ConfigConstant.AUTO_ADD_HP_MP);
		//满血满蓝, 中断调息buff
		if(buff.getBuffId() == AUTO_ADD_HP_MP && basePuppet.getHp() >= basePuppet.getHpMax() && 
				basePuppet.getMp() >= basePuppet.getMpMax() && buff.getEndTime() != 0){
			this.removeBuffById(basePuppet, AUTO_ADD_HP_MP);
		}
		
		Map<Integer, Integer> proMap = buff.getAddValueMap();
		for(Map.Entry<Integer, Integer> entry : proMap.entrySet()){
			int propertyId = entry.getKey();
			int proValue = entry.getValue();
			
			switch (propertyId) {
			case ProdefineConstant.HP:				
				buff.setHpShow(proValue);
				basePuppet.setHp(basePuppet.getHp() + proValue);
				//buff造成伤害
				if(proValue < 0){
					List<BasePuppet> listTargets = new ArrayList<BasePuppet>();
					listTargets.add(basePuppet);
					
					List<Integer> listDmgs = new ArrayList<Integer>();
					listDmgs.add(-proValue);
					battleService.attack(buff.getFighter(), listTargets, listDmgs);
				}
				
				playerService.synPlayerPropertyToAll(basePuppet, ProdefineConstant.HP, basePuppet.getHp());
			
				if(basePuppet.getType() == PlayerConstant.PLAYER){
					teamService.synHp((PlayerPuppet)basePuppet);
				}
				break;
			case ProdefineConstant.MP:
				basePuppet.setMp(basePuppet.getMp() + proValue);
				if(basePuppet.getType() == PlayerConstant.PLAYER){
					playerService.synPlayerPropertyToAll(basePuppet, ProdefineConstant.MP, basePuppet.getMp());
					
					teamService.synHp((PlayerPuppet)basePuppet);
				}
				break;

			default:
				break;
			}
		}
	}
	
	/**
	 * 同步buff
	 */
	private void synBuff(BasePuppet basePuppet, Set<Buff> changeBuffList){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		ISceneService sceneService = serviceCollection.getSceneService();
		
		S_SynBuff.Builder builder = S_SynBuff.newBuilder();
		for(Buff buff : changeBuffList){			
			builder.addBuffList(protoBuilderService.buildBuffMsg(buff));
		}
		
		List<Long> playerIds = sceneService.getNearbyPlayerIds(basePuppet);		
		MessageObj msg = new MessageObj(MessageID.S_SynBuff_VALUE, builder.build().toByteArray());
		gameSocketService.sendDataToPlayerList(playerIds, msg);
	}

	@Override
	public void dealDead(BasePuppet target) {
		Map<Integer, Buff> buffMap = target.getBuffMap();
		if(buffMap.isEmpty()) return;
		
		try {
			Set<Buff> changeBuffList = new HashSet<Buff>();
			
			Iterator<Map.Entry<Integer, Buff>> iterator = buffMap.entrySet().iterator();
			while(iterator.hasNext()){
				Map.Entry<Integer, Buff> entry = iterator.next();
				Buff buff = entry.getValue();
				
				BaseBuff baseBuff = this.getBaseBuff(buff.getBuffId());
				
				if(baseBuff.getRemoveTypeList() != null && !baseBuff.getRemoveTypeList().isEmpty()){
					if(baseBuff.getRemoveTypeList().contains(BuffConstant.REMOVE_TYPE_DEAD)){
						//buff过期
						buff.setEndTime(0);
						
						changeBuffList.add(buff);
						iterator.remove();

						this.addBuffValue(target, target, baseBuff, buff, false);
					}
				}
			}	
			
			if(!changeBuffList.isEmpty()){
				this.synBuff(target, changeBuffList);
			}
		} catch (Exception e) {
			LogUtil.error("死亡buff清理异常:"+target.getGuid(),e);
		}
	}

	@Override
	public void dealExit(PlayerExt playerExt, PlayerPuppet playerPuppet) {
		playerExt.getRemainBuffMap().clear();
		
		Map<Integer, Buff> buffMap = playerPuppet.getBuffMap();
		if(buffMap.isEmpty()) return;
		
		try {
			Set<Buff> changeBuffList = new HashSet<Buff>();
			
			Iterator<Map.Entry<Integer, Buff>> iterator = buffMap.entrySet().iterator();
			while(iterator.hasNext()){
				Map.Entry<Integer, Buff> entry = iterator.next();
				Buff buff = entry.getValue();
				
				BaseBuff baseBuff = this.getBaseBuff(buff.getBuffId());
				
				if(baseBuff.getRemoveTypeList() != null && !baseBuff.getRemoveTypeList().isEmpty()){
					if(baseBuff.getRemoveTypeList().contains(BuffConstant.REMOVE_TYPE_EXIT)){
						//buff过期
						buff.setEndTime(0);
						
						changeBuffList.add(buff);
						iterator.remove();

						this.addBuffValue(playerPuppet, playerPuppet, baseBuff, buff, false);
						continue;
					}

				}
				
				if(buff.getEndTime() != 0){
					playerExt.getRemainBuffMap().put(buff.getId(), buff);
				}
			}
			
			if(!changeBuffList.isEmpty()){
				this.synBuff(playerPuppet, changeBuffList);
			}
			
		} catch (Exception e) {
			LogUtil.error("下线buff清理异常:"+playerPuppet.getGuid(),e);
		}
	}

	@Override
	public void dealLogin(long playerId) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();	
		PlayerExt playerExt = playerService.getPlayerExtById(playerId);	
		ISceneService sceneService = serviceCollection.getSceneService();
		
		PlayerPuppet playerPuppet = sceneService.getPlayerPuppet(playerId);
		Map<Integer, Buff> buffMap = playerPuppet.getBuffMap();	
		
		// 处理下线不清的buff		
		if(!playerExt.getRemainBuffMap().isEmpty()){
			long currentTime = System.currentTimeMillis();
			
			for(Map.Entry<Integer, Buff> entry : playerExt.getRemainBuffMap().entrySet()){
				Buff buff = entry.getValue();
				if(buff.getEndTime() == 0) continue;
				
				if(buff.getEndTime() > 0){
					//2s容错
					if(buff.getEndTime() - currentTime < 2000){
						continue;
					}
				}
						
				if(buffMap.containsKey(buff.getId())) continue;
				
				Set<Buff> changeBuffList = new HashSet<Buff>();				
				buffMap.put(buff.getId(), buff);				
				BaseBuff baseBuff = this.getBaseBuff(buff.getBuffId());
				this.addBuffValue(playerPuppet, playerPuppet, baseBuff, buff, true);
				
				changeBuffList.add(buff);				
				if(!changeBuffList.isEmpty()){
					this.synBuff(playerPuppet, changeBuffList);
				}
			}
		}	
	
	
		//检测时装buff		
		IFashionService fashionService = serviceCollection.getFashionService();
		fashionService.dealLogin(playerId);
		
		ITeamService teamService = serviceCollection.getTeamService();
		teamService.checkFamilyBuff(playerId, playerExt.getTeamId());		
	}

	@Override
	public void autoAddHpMp(long playerId){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();	
		ICommonService commonService = serviceCollection.getCommonService();
		ISceneService sceneService = serviceCollection.getSceneService();
		
		PlayerPuppet playerPuppet = sceneService.getPlayerPuppet(playerId);		
		if(playerPuppet == null || playerPuppet.getState() != BattleConstant.STATE_NORMAL) return;
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_BUFF)) {
			
			// 调息buff
			int AUTO_ADD_HP_MP = commonService.getConfigValue(ConfigConstant.AUTO_ADD_HP_MP);			
			this.addBuffById(playerId, AUTO_ADD_HP_MP);
		}
	}

	@Override
	public void breakAddHpMp(long playerId) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		ICommonService commonService = serviceCollection.getCommonService();
		ISceneService sceneService = serviceCollection.getSceneService();
		
		PlayerPuppet playerPuppet = sceneService.getPlayerPuppet(playerId);		
		if(playerPuppet == null || playerPuppet.getState() != BattleConstant.STATE_NORMAL) return;
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_BUFF)) {
			
			// 调息buff
			int AUTO_ADD_HP_MP = commonService.getConfigValue(ConfigConstant.AUTO_ADD_HP_MP);
			this.removeBuffById(playerPuppet, AUTO_ADD_HP_MP);		
		}
	}	
	
	/**
	 * 根据buff编号ID 移除buff
	 */
	public void removeBuffById(BasePuppet basePuppet, int buffId){
		Map<Integer, Buff> buffMap = basePuppet.getBuffMap();
		if(buffMap.isEmpty()) return;
		
		try {
			Set<Buff> changeBuffList = new HashSet<Buff>();				
			Iterator<Map.Entry<Integer, Buff>> iterator = buffMap.entrySet().iterator();
			
			while(iterator.hasNext()){
				Map.Entry<Integer, Buff> entry = iterator.next();				
				Buff buff = entry.getValue();				
				if(buffId == buff.getBuffId()){	
					
					BaseBuff baseBuff = this.getBaseBuff(buff.getBuffId());
					if(baseBuff == null) {
						LogUtil.error("removeBuffById baseBuff不存在:"+ buffId);
					}
					
					buff.setEndTime(0);
					changeBuffList.add(buff);
					iterator.remove();						
					this.addBuffValue(basePuppet, basePuppet, baseBuff, buff, false);
				}				
			}
			
			if(!changeBuffList.isEmpty()){
				this.synBuff(basePuppet, changeBuffList);
			}
		} catch (Exception e) {
			LogUtil.error("打断buff清理异常:"+basePuppet.getGuid(),e);
		}
	}
	
	
	/**
	 * 根据buff编号ID 添加buff
	 */
	public void addBuffById(long playerId, int buffId){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();		
		ISceneService sceneService = serviceCollection.getSceneService();
		
		PlayerPuppet playerPuppet = sceneService.getPlayerPuppet(playerId);			
		if(playerPuppet == null || playerPuppet.getState() != BattleConstant.STATE_NORMAL) return; 
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_BUFF)) {
						
			BaseBuff baseBuff = this.getBaseBuff(buffId);
			if(baseBuff == null) {
				LogUtil.error("baseBuff不存在:"+ buffId);
			}
			
			Set<Buff> changeBuffList = new HashSet<Buff>();
			if(baseBuff.getBuffId() > 0){
				
				List<Integer> buffIds = new ArrayList<Integer>();
				buffIds.add(baseBuff.getBuffId());
				
				List<Buff> lists = this.addBuff(playerPuppet, playerPuppet, buffIds);	
				for(Buff buff : lists){
					changeBuffList.add(buff);	
				}
			}
			
			if(!changeBuffList.isEmpty()){
				this.synBuff(playerPuppet, changeBuffList);
			}
		}
	}

}
