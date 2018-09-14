package com.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cache.BaseCacheService;
import com.common.GameContext;
import com.common.ServiceCollection;
import com.constant.CacheConstant;
import com.constant.ProdefineConstant;
import com.dao.base.BaseDAO;
import com.domain.MessageObj;
import com.domain.base.BaseConstant;
import com.domain.base.BaseNewRole;
import com.domain.base.BaseProperty;
import com.domain.player.PlayerExt;
import com.domain.player.PlayerProperty;
import com.domain.player.PlayerWealth;
import com.message.ExceptionProto.S_Exception_Server;
import com.message.MessageProto.MessageEnum.MessageID;
import com.service.ICommonService;
import com.service.ISkillService;
import com.util.LogUtil;
import com.util.SplitStringUtil;

/**
 * 玩家属性系统
 * 
 * @author ken
 * @date 2016-12-29
 */
public class CommonService implements ICommonService {

	private BaseDAO baseDAO = new BaseDAO();

	@Override
	public void initBaseCache() {

		Map<Integer, Map<Integer, BaseProperty>> baseProMap = new HashMap<Integer, Map<Integer, BaseProperty>>();
		List<BaseProperty> propertys = baseDAO.listPropertys();
		for (BaseProperty model : propertys) {
			int career = model.getLevelId() / 1000;
			int level = model.getLevelId() % 1000;
			Map<Integer, BaseProperty> map = baseProMap.get(career);
			if (map == null) {
				map = new HashMap<Integer, BaseProperty>();
				baseProMap.put(career, map);
			}
			map.put(level, model);
		}
		BaseCacheService.putToBaseCache(CacheConstant.BASE_PROPERTY, baseProMap);

		Map<Integer, BaseConstant> baseConfigMap = new HashMap<Integer, BaseConstant>();
		List<BaseConstant> configs = baseDAO.listBaseConfigs();
		for (BaseConstant model : configs) {
			baseConfigMap.put(model.getId(), model);
		}
		BaseCacheService.putToBaseCache(CacheConstant.BASE_CONSTANT, baseConfigMap);

		Map<Integer, BaseNewRole> baseNewRoleMap = new HashMap<Integer, BaseNewRole>();
		List<BaseNewRole> roles = baseDAO.listBaseNewRoles();
		for (BaseNewRole model : roles) {
			model.setBageitemList(SplitStringUtil.getIntIntList(model.getBageitems()));
			model.setInitSkillList(SplitStringUtil.getIntList(model.getInitSkills()));

			List<Integer> positions = SplitStringUtil.getPosition(model.getPosition());
			model.setX(positions.get(0));
			model.setY(positions.get(1));
			model.setZ(positions.get(2));
			baseNewRoleMap.put(model.getCareer(), model);
		}
		BaseCacheService.putToBaseCache(CacheConstant.BASE_NEWROLE, baseNewRoleMap);

	}

	/**
	 * 成长属性配置表
	 */
	@SuppressWarnings("unchecked")
	public BaseProperty getBaseProperty(int career, int level) {
		Map<Integer, Map<Integer, BaseProperty>> baseProMap = (Map<Integer, Map<Integer, BaseProperty>>) BaseCacheService
				.getFromBaseCache(CacheConstant.BASE_PROPERTY);
		Map<Integer, BaseProperty> map = baseProMap.get(career);
		if (map == null)
			return null;

		return map.get(level);
	}

	@Override
	public void dealInitProperty(PlayerProperty playerProperty, PlayerExt playerExt, PlayerWealth playerWealth,
			int career) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();

		BaseProperty baseProperty = getBaseProperty(career, playerProperty.getLevel());
		BaseNewRole baseNewRole = getBaseNewRole(career);

		// 初始技能
		ISkillService skillService = serviceCollection.getSkillService();
		List<Integer> skillIDs = baseNewRole.getInitSkillList();
		try {
			skillService.studyPlayerSkill(playerProperty.getPlayerId(), skillIDs.get(0), true);
		} catch (Exception e) {
			LogUtil.debug("dealInitProperty study initSkill error :" + e);
		}

		// 初始财富
		playerWealth.setGold(baseNewRole.getGold());
		playerWealth.setDiamond(baseNewRole.getDiamond());
		playerWealth.setStone(baseNewRole.getStone());

		// 初始属性
		playerProperty.setMoveSpeed(baseNewRole.getMoveSpeed());
		Map<Integer, Integer> addProMap = new HashMap<Integer, Integer>();
		addProMap.put(ProdefineConstant.STRENGTH, baseProperty.getStrength());
		addProMap.put(ProdefineConstant.INTELLIGENCE, baseProperty.getIntelligence());
		addProMap.put(ProdefineConstant.ENDURANCE, baseProperty.getEndurance());
		addProMap.put(ProdefineConstant.SPIRIT, baseProperty.getSpirit());
		addProMap.put(ProdefineConstant.LUCKY, baseProperty.getLucky());
		addProMap.put(ProdefineConstant.DMG_DEEP_PER_PANEL, baseProperty.getDmgDeepPer());
		addProMap.put(ProdefineConstant.DMG_REDUCT_PER_PANEL, baseProperty.getDmgReductPer());
		addProMap.put(ProdefineConstant.DMG_CRIT_PER_PANEL, baseProperty.getDmgCritPer());
		serviceCollection.getPropertyService().addProValue(playerProperty.getPlayerId(), addProMap, false, false);
		serviceCollection.getPropertyService().calTotalBattleValue(playerProperty);

		playerExt.setHp(playerProperty.getHpMax());
		playerExt.setMp(playerProperty.getMpMax());
		playerExt.setDressStyle(baseNewRole.getDressStyle());
		playerExt.setWeaponStyle(baseNewRole.getWeaponStyle());
		playerExt.setMapId(baseNewRole.getMapName());
		playerExt.setX(baseNewRole.getX());
		playerExt.setY(baseNewRole.getY());
		playerExt.setZ(baseNewRole.getZ());
		playerExt.setLastMapId(playerExt.getMapId());
		playerExt.setLastX(playerExt.getX());
		playerExt.setLastY(playerExt.getY());
		playerExt.setLastZ(playerExt.getZ());
		playerExt.setDirection(baseNewRole.getDirection());
		playerExt.setBagGrid(baseNewRole.getBagGrid());
		playerExt.setTradeGridNum(baseNewRole.getMaxTradeGrid());

	}

	/**
	 * 根据职业取初始角色配置
	 */
	@SuppressWarnings("unchecked")
	public BaseNewRole getBaseNewRole(int career) {
		Map<Integer, BaseNewRole> baseNewRoleMap = (Map<Integer, BaseNewRole>) BaseCacheService
				.getFromBaseCache(CacheConstant.BASE_NEWROLE);
		return baseNewRoleMap.get(career);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int getConfigValue(int codeId) {
		Map<Integer, BaseConstant> map = (Map<Integer, BaseConstant>) BaseCacheService
				.getFromBaseCache(CacheConstant.BASE_CONSTANT);
		BaseConstant model = map.get(codeId);
		return model.getValue();
	}

	@Override
	public void sendNoticeMsg(long playerId, String msg) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		S_Exception_Server.Builder builder = S_Exception_Server.newBuilder();
		builder.setMsg(msg);
		MessageObj msg1 = new MessageObj(MessageID.S_Exception_Server_VALUE, builder.build().toByteArray());
		serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(playerId, msg1);
	}

}
