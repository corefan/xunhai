package com.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.common.GameContext;
import com.common.ServiceCollection;
import com.constant.ProdefineConstant;
import com.domain.player.PlayerProperty;
import com.domain.puppet.PlayerPuppet;
import com.service.IPlayerService;
import com.service.IPropertyService;
import com.service.ISceneService;

/**
 * 属性变动通用
 * @author ken
 * @date 2017-2-16
 */
public class PropertyService implements IPropertyService {

	@Override
	public void addProValue(long playerId, List<List<Integer>> addProList,  int sign, boolean offer, boolean offerBattleValue) {

		if(addProList == null || addProList.isEmpty()) return;
		
		Map<Integer, Integer> addProMap = new HashMap<Integer, Integer>();
		for(List<Integer> list : addProList){
			addProMap.put(list.get(0), list.get(1)*sign);
		}
		this.addProValue(playerId, addProMap, offer, offerBattleValue);
	}
	
	@Override
	public void addProValue(long playerId, Map<Integer, Integer> addProMap,
			boolean offer, boolean offerBattleValue) {
		
		if(addProMap == null) return;
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		ISceneService sceneService = serviceCollection.getSceneService();
		
		
		Map<Integer, Integer> proMap = new HashMap<Integer, Integer>(); //同步玩家属性for面板
		Map<Integer, Integer> toSceneProMap = new HashMap<Integer, Integer>(); //同步玩家属性for场景
		
		PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerId);
		
		PlayerPuppet playerPuppet = sceneService.getPlayerPuppet(playerId);
		
		for(Map.Entry<Integer, Integer> entry : addProMap.entrySet()){
			int propertyId = entry.getKey();
			int addValue = entry.getValue();
			
			switch (propertyId) {
			case ProdefineConstant.STRENGTH:
				playerProperty.setStrength(playerProperty.getStrength() + addValue);
				playerProperty.setP_attack(playerProperty.getP_attack() + addValue);
				playerProperty.setHpMax(playerProperty.getHpMax() + 4 * addValue);
				if(playerPuppet != null){
					playerPuppet.setP_attack(playerPuppet.getP_attack() + addValue);
					playerPuppet.setHpMax(playerPuppet.getHpMax() + 4 * addValue);
				}
				if(offer){
					proMap.put(ProdefineConstant.STRENGTH, playerProperty.getStrength());
					proMap.put(ProdefineConstant.P_ATTACK_PANEL, playerProperty.getP_attack());
					proMap.put(ProdefineConstant.HP_MAX_PANEL, playerProperty.getHpMax());
					toSceneProMap.put(ProdefineConstant.HP_MAX, playerPuppet.getHpMax());
					toSceneProMap.put(ProdefineConstant.HP, playerPuppet.getHp());
				}
				break;
			case ProdefineConstant.INTELLIGENCE:
				playerProperty.setIntelligence(playerProperty.getIntelligence() + addValue);
				playerProperty.setM_attack(playerProperty.getM_attack() + addValue);
				playerProperty.setMpMax(playerProperty.getMpMax() + 2 * addValue);
				if(playerPuppet != null){
					playerPuppet.setM_attack(playerPuppet.getM_attack() + addValue);
					playerPuppet.setMpMax(playerPuppet.getMpMax() + 2 * addValue);
				}
				if(offer){
					proMap.put(ProdefineConstant.INTELLIGENCE, playerProperty.getIntelligence());
					proMap.put(ProdefineConstant.M_ATTACK_PANEL, playerProperty.getM_attack());
					proMap.put(ProdefineConstant.MP_MAX_PANEL, playerProperty.getMpMax());
					toSceneProMap.put(ProdefineConstant.MP_MAX, playerPuppet.getMpMax());
					toSceneProMap.put(ProdefineConstant.MP, playerPuppet.getMp());
				}
				break;
			case ProdefineConstant.ENDURANCE:
				playerProperty.setEndurance(playerProperty.getEndurance() + addValue);
				playerProperty.setP_damage(playerProperty.getP_damage() + addValue);
				playerProperty.setHpMax(playerProperty.getHpMax() + 10 * addValue);
				if(playerPuppet != null){
					playerPuppet.setP_damage(playerPuppet.getP_damage() + addValue);
					playerPuppet.setHpMax(playerPuppet.getHpMax() + 10 * addValue);
				}
				if(offer){
					proMap.put(ProdefineConstant.ENDURANCE, playerProperty.getEndurance());
					proMap.put(ProdefineConstant.P_DAMAGE_PANEL, playerProperty.getP_damage());
					proMap.put(ProdefineConstant.HP_MAX_PANEL, playerProperty.getHpMax());
					toSceneProMap.put(ProdefineConstant.HP_MAX, playerPuppet.getHpMax());
					toSceneProMap.put(ProdefineConstant.HP, playerPuppet.getHp());
				}
				break;
			case ProdefineConstant.SPIRIT:
				playerProperty.setSpirit(playerProperty.getSpirit() + addValue);
				playerProperty.setM_damage(playerProperty.getM_damage() + addValue);
				playerProperty.setMpMax(playerProperty.getMpMax() + 5 * addValue);
				if(playerPuppet != null){
					playerPuppet.setM_damage(playerPuppet.getM_damage() + addValue);
					playerPuppet.setMpMax(playerPuppet.getMpMax() + 5 * addValue);
				}
				if(offer){
					proMap.put(ProdefineConstant.SPIRIT, playerProperty.getSpirit());
					proMap.put(ProdefineConstant.M_DAMAGE_PANEL, playerProperty.getM_damage());
					proMap.put(ProdefineConstant.MP_MAX_PANEL, playerProperty.getMpMax());
					toSceneProMap.put(ProdefineConstant.MP_MAX, playerProperty.getMpMax());
					toSceneProMap.put(ProdefineConstant.MP, playerPuppet.getMp());
				}
				break;
			case ProdefineConstant.LUCKY:
				playerProperty.setLucky(playerProperty.getLucky() + addValue);
				playerProperty.setCrit(playerProperty.getCrit() +  addValue);
				playerProperty.setTough(playerProperty.getTough() +  addValue);
				if(playerPuppet != null){
					playerPuppet.setCrit(playerPuppet.getCrit() +  addValue);
					playerPuppet.setTough(playerPuppet.getTough() +  addValue);
				}
				if(offer){
					proMap.put(ProdefineConstant.LUCKY, playerProperty.getLucky());
					proMap.put(ProdefineConstant.CRIT_PANEL, playerProperty.getCrit());
					proMap.put(ProdefineConstant.TOUGH_PANEL, playerProperty.getTough());	
				}
				break;
				
			case ProdefineConstant.HP_MAX_PANEL:
				playerProperty.setHpMax(playerProperty.getHpMax() + addValue);
				if(playerPuppet != null){
					playerPuppet.setHpMax(playerPuppet.getHpMax() + addValue);
				}
				if(offer){
					proMap.put(ProdefineConstant.HP_MAX_PANEL, playerProperty.getHpMax());
					if(playerPuppet != null){
						toSceneProMap.put(ProdefineConstant.HP_MAX, playerPuppet.getHpMax());
						toSceneProMap.put(ProdefineConstant.HP, playerPuppet.getHp());	
					}

				}
				break;
			case ProdefineConstant.MP_MAX_PANEL:
				playerProperty.setMpMax(playerProperty.getMpMax() + addValue);
				if(playerPuppet != null){
					playerPuppet.setMpMax(playerPuppet.getMpMax() + addValue);
				}
				if(offer){
					proMap.put(ProdefineConstant.MP_MAX_PANEL, playerProperty.getMpMax());
					if(playerPuppet != null){
						toSceneProMap.put(ProdefineConstant.MP_MAX, playerPuppet.getMpMax());
						toSceneProMap.put(ProdefineConstant.MP, playerPuppet.getMp());
					}

				}
				break;
			case ProdefineConstant.P_ATTACK_PANEL:
				playerProperty.setP_attack(playerProperty.getP_attack() + addValue);
				if(playerPuppet != null){
					playerPuppet.setP_attack(playerProperty.getP_attack());	
				}
				if(offer){
					proMap.put(ProdefineConstant.P_ATTACK_PANEL, playerProperty.getP_attack());
				}
				break;
			case ProdefineConstant.M_ATTACK_PANEL:
				playerProperty.setM_attack(playerProperty.getM_attack() + addValue);
				if(playerPuppet != null){
					playerPuppet.setM_attack(playerPuppet.getM_attack() + addValue);
				}
				if(offer){
					proMap.put(ProdefineConstant.M_ATTACK_PANEL, playerProperty.getM_attack());
				}
				break;
			case ProdefineConstant.P_DAMAGE_PANEL:
				playerProperty.setP_damage(playerProperty.getP_damage() + addValue);
				if(playerPuppet != null){
					playerPuppet.setP_damage(playerPuppet.getP_damage() + addValue);
				}
				if(offer){
					proMap.put(ProdefineConstant.P_DAMAGE_PANEL, playerProperty.getP_damage());
				}
				break;
			case ProdefineConstant.M_DAMAGE_PANEL:
				playerProperty.setM_damage(playerProperty.getM_damage() + addValue);
				if(playerPuppet != null){
					playerPuppet.setM_damage(playerPuppet.getM_damage() + addValue);
				}
				if(offer){
					proMap.put(ProdefineConstant.M_DAMAGE_PANEL, playerProperty.getM_damage());
				}
				break;
			case ProdefineConstant.CRIT_PANEL:
				playerProperty.setCrit(playerProperty.getCrit() + addValue);
				if(playerPuppet != null){
					playerPuppet.setCrit(playerPuppet.getCrit() + addValue);
				}
				if(offer){
					proMap.put(ProdefineConstant.CRIT_PANEL, playerProperty.getCrit());
				}
				break;
			case ProdefineConstant.TOUGH_PANEL:
				playerProperty.setTough(playerProperty.getTough() + addValue);
				if(playerPuppet != null){
					playerPuppet.setTough(playerPuppet.getTough() + addValue);
				}
				if(offer){
					proMap.put(ProdefineConstant.TOUGH_PANEL, playerProperty.getTough());
				}
				break;
			case ProdefineConstant.DMG_DEEP_PER_PANEL:
				playerProperty.setDmgDeepPer(playerProperty.getDmgDeepPer() + addValue);
				if(playerPuppet != null){
					playerPuppet.setDmgDeepPer(playerPuppet.getDmgDeepPer() + addValue);	
				}
				if(offer){
					proMap.put(ProdefineConstant.DMG_DEEP_PER_PANEL, playerProperty.getDmgDeepPer());
				}
				break;
			case ProdefineConstant.DMG_REDUCT_PER_PANEL:
				playerProperty.setDmgReductPer(playerProperty.getDmgReductPer() + addValue);
				if(playerPuppet != null){
					playerPuppet.setDmgReductPer(playerPuppet.getDmgReductPer() + addValue);
				}
				if(offer){
					proMap.put(ProdefineConstant.DMG_REDUCT_PER_PANEL, playerProperty.getDmgReductPer());
				}
				break;
			case ProdefineConstant.DMG_CRIT_PER_PANEL:
				playerProperty.setDmgCritPer(playerProperty.getDmgCritPer() + addValue);
				if(playerPuppet != null){
					playerPuppet.setDmgCritPer(playerPuppet.getDmgCritPer() + addValue);
				}
				if(offer){
					proMap.put(ProdefineConstant.DMG_CRIT_PER_PANEL, playerProperty.getDmgCritPer());
				}
				break;
			case ProdefineConstant.MOVE_SPEED_PANEL:
				playerProperty.setMoveSpeed(playerProperty.getMoveSpeed() + addValue);
				if(playerPuppet != null){
					playerPuppet.setMoveSpeed(playerPuppet.getMoveSpeed() + addValue);
				}
				if(offer){
					proMap.put(ProdefineConstant.MOVE_SPEED_PANEL, playerProperty.getMoveSpeed());
					toSceneProMap.put(ProdefineConstant.MOVE_SPEED, playerPuppet.getMoveSpeed());
				}
				break;

			default:
				break;
			}
			
		}
		
		if(offer){
			if(offerBattleValue){
				this.calTotalBattleValue(playerProperty);
				proMap.put(ProdefineConstant.BATTLE_VALUE, playerProperty.getBattleValue());
			}

			playerService.synPlayerProperty(playerId, proMap);
			
			if(!toSceneProMap.isEmpty()){
				playerService.synPlayerPropertyToAll(playerPuppet, toSceneProMap);
			}
		}
	}
	

	@Override
	public void calTotalBattleValue(PlayerProperty playerProperty) {
		ServiceCollection serviceCollection = GameContext.getInstance()
				.getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();

		double value = playerProperty.getHpMax()
				+ playerProperty.getMpMax()
				+ (playerProperty.getP_attack() + playerProperty.getM_attack()
						+ playerProperty.getP_damage()
						+ playerProperty.getM_damage()
						+ playerProperty.getCrit() + playerProperty.getTough())* 10
				+ playerProperty.getDmgDeepPer()
				+ playerProperty.getDmgReductPer()
				+ (playerProperty.getDmgCritPer() - 18000)
				* 0.5
				+ (Math.pow(playerProperty.getSkillLv(), 2) + playerProperty
						.getSkillLv() * 50);

		playerProperty.setBattleValue((int) value);

		playerService.updatePlayerProperty(playerProperty);

	}

	@Override
	public void synBattleValue(PlayerProperty playerProperty) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();	
		
		this.calTotalBattleValue(playerProperty);
		
		playerService.synPlayerProperty(playerProperty.getPlayerId(), ProdefineConstant.BATTLE_VALUE, playerProperty.getBattleValue());
	}
}
