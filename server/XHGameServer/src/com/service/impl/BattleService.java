package com.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import com.common.GameContext;
import com.common.GameSocketService;
import com.common.LockService;
import com.common.RandomService;
import com.common.ServiceCollection;
import com.constant.BattleConstant;
import com.constant.ChatConstant;
import com.constant.ConfigConstant;
import com.constant.ExceptionConstant;
import com.constant.GuildConstant;
import com.constant.ItemConstant;
import com.constant.LockConstant;
import com.constant.PlayerConstant;
import com.constant.ProdefineConstant;
import com.constant.RewardTypeConstant;
import com.constant.SceneConstant;
import com.constant.SkillConstant;
import com.constant.TaskConstant;
import com.constant.WakanConstant;
import com.core.GameMessage;
import com.domain.GameException;
import com.domain.MessageObj;
import com.domain.Reward;
import com.domain.bag.BaseEquipment;
import com.domain.bag.BaseItem;
import com.domain.bag.PlayerEquipment;
import com.domain.base.BaseNewRole;
import com.domain.battle.DropItemInfo;
import com.domain.battle.SkillEffect;
import com.domain.battle.WigSkillInfo;
import com.domain.buff.Buff;
import com.domain.chat.Notice;
import com.domain.collect.BaseCollect;
import com.domain.collect.Collect;
import com.domain.family.PlayerFamily;
import com.domain.guild.Guild;
import com.domain.guild.GuildFight;
import com.domain.guild.PlayerGuild;
import com.domain.instance.BaseInstanceReward;
import com.domain.map.BaseMap;
import com.domain.monster.BaseMonster;
import com.domain.player.Player;
import com.domain.player.PlayerExt;
import com.domain.player.PlayerProperty;
import com.domain.puppet.BasePuppet;
import com.domain.puppet.BeckonPuppet;
import com.domain.puppet.EnemyModel;
import com.domain.puppet.MonsterPuppet;
import com.domain.puppet.PlayerPuppet;
import com.domain.skill.BaseSkill;
import com.domain.skill.DamagModel;
import com.domain.team.Team;
import com.domain.team.TeamPlayer;
import com.message.BagProto.S_SynBagItem;
import com.message.BattleProto.S_Revive;
import com.message.BattleProto.S_SkillResult;
import com.message.BattleProto.S_SynSkill;
import com.message.ChatProto.ParamType;
import com.message.MessageProto.MessageEnum.MessageID;
import com.message.SceneProto.C_Pickup;
import com.message.SceneProto.S_AddDropItemInfos;
import com.message.SceneProto.S_Pickup;
import com.message.SceneProto.S_RemoveDropItemInfos;
import com.scene.SceneModel;
import com.service.IBattleService;
import com.service.IBuffService;
import com.service.IChatService;
import com.service.ICollectService;
import com.service.ICommonService;
import com.service.IEquipmentService;
import com.service.IFamilyService;
import com.service.IGuildService;
import com.service.IInstanceService;
import com.service.IMonsterService;
import com.service.IPlayerService;
import com.service.IProtoBuilderService;
import com.service.IRewardService;
import com.service.ISceneService;
import com.service.ISkillService;
import com.service.ITeamService;
import com.service.IWakanService;
import com.util.CommonUtil;
import com.util.ComparatorUtil;
import com.util.LogUtil;
import com.util.NineGridUtil;
import com.util.SerialNumberUtil;

/**
 * 战斗处理
 * 
 * @author ken
 * @date 2017-1-10
 */
public class BattleService implements IBattleService {

	@Override
	public void synSkill(BasePuppet basePuppet, int skillId,
			int type, int direction, int x, int y, int z, String targetId)
			throws Exception {
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		ISkillService skillService = serviceCollection.getSkillService();
		ISceneService sceneService = serviceCollection.getSceneService();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		SceneModel sceneModel = sceneService.getSceneModel(basePuppet.getSceneGuid());
		if(sceneModel.getSceneState() == SceneConstant.SCENE_STATE_DISTROY) return;
	
		if(basePuppet.getVertigo() == 1){
			// 眩晕不能放技能
			return;
		}
		
		// 消耗魔法
		BaseSkill baseSkill = skillService.getBaseSkill(skillId);
		if(baseSkill.getN32UseMP() > 0){

			if(basePuppet.getMp() < baseSkill.getN32UseMP()){
				if(basePuppet.getType() == PlayerConstant.PLAYER){
					throw new GameException(ExceptionConstant.SKILL_1604);
				}
				return;
			}
			
			basePuppet.setMp(basePuppet.getMp() - baseSkill.getN32UseMP());
			playerService.synPlayerPropertyToAll(basePuppet, ProdefineConstant.MP, basePuppet.getMp());
		}	
		
		List<Long> playerIds = serviceCollection.getSceneService().getNearbyPlayerIds(basePuppet);
		
		if(playerIds != null && !playerIds.isEmpty()){
			int wigId = 0;
			if(baseSkill.getN32LifeTime() > 0){
				
				int gridId = NineGridUtil.calInGrid(x, z, sceneModel.getColNum());
				BlockingQueue<WigSkillInfo> wigInfos = sceneModel.getWigSkillMap().get(gridId);
				if(wigInfos == null){
					wigInfos = new LinkedBlockingQueue<WigSkillInfo>();
					sceneModel.getWigSkillMap().put(gridId, wigInfos);
				}
				WigSkillInfo wigSkillInfo = new WigSkillInfo(basePuppet.getGuid(), skillId, x, y, z, gridId, System.currentTimeMillis() + baseSkill.getN32LifeTime());
				wigInfos.offer(wigSkillInfo);
				
				wigId = wigSkillInfo.getWigId();
			}		
			
			S_SynSkill.Builder builder = S_SynSkill.newBuilder();
			builder.setGuid(basePuppet.getGuid());
			builder.setSkillId(skillId);
			builder.setType(type);
			builder.setDirection(direction);
			builder.setTargetPoint(protoBuilderService.buildVector3Msg(x,y,z));
			builder.setTargetId(targetId);
			builder.setWigId(wigId);
			
			MessageObj msg = new MessageObj(MessageID.S_SynSkill_VALUE, builder.build().toByteArray());
			serviceCollection.getGameSocketService().sendDataToPlayerList(playerIds, msg);			
		}
		
		if(basePuppet.getType() == PlayerConstant.PLAYER){
			// 添加玩家技能熟练度
			serviceCollection.getSkillService().addMastery(basePuppet.getEid(), skillId);
		}
	}	
	
	@Override
	public void useSkill(String sceneGuid, String guid, int skillId, List<String> targetIds, int accountModelId, int wigId, int fighterType) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		ISceneService sceneService = serviceCollection.getSceneService();
		ISkillService skillService = serviceCollection.getSkillService();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IFamilyService familyService = serviceCollection.getFamilyService();
		IGuildService guildService = serviceCollection.getGuildService();
		
		// 攻击者
		BasePuppet basePuppet = sceneService.getBasePuppet(sceneGuid, guid, fighterType);
		
		if(basePuppet == null || basePuppet.getState() != BattleConstant.STATE_NORMAL) return;
			
		// 地图模式过滤
		BaseMap baseMap = sceneService.getBaseMap(basePuppet.getMapId());
		if(baseMap.getMapType() == SceneConstant.MAIN_CITY){
			throw new GameException(ExceptionConstant.SCENE_1204);
		}
		
		BaseSkill baseSkill = skillService.getBaseSkill(skillId);
		if(baseSkill.getSkillType() != SkillConstant.SKILL_TYPE_4){
			if(targetIds == null || targetIds.isEmpty()) return;
		}
		
		synchronized (basePuppet.getUseSkillLock()) {
			
			if(baseSkill.getSkillType() == SkillConstant.SKILL_TYPE_4){
				// 召唤技
				SceneModel sceneModel = serviceCollection.getSceneService().getSceneModel(basePuppet.getSceneGuid());
				if(sceneModel.getSceneState() == SceneConstant.SCENE_STATE_DISTROY) return;
				
				PlayerPuppet p = (PlayerPuppet)basePuppet;
				serviceCollection.getMonsterService().removeBeckonPuppet(p, sceneModel);
				
				serviceCollection.getMonsterService().initBeckonPuppet(sceneModel, p, accountModelId);
				return;
			}
			
			List<BasePuppet> targetList = new ArrayList<BasePuppet>();
			for(String targetId : targetIds){
				
				BasePuppet target = sceneService.getBasePuppet(sceneGuid, targetId, 0);
				
				if(target == null){
					sceneService.removeSelfToNearby(targetId, sceneService.getNearbyPlayerIds(basePuppet));
					continue;
				}else{
					// 清理怪物残留
					if(target.getHp() < 1){
						if(target.getType() > PlayerConstant.PLAYER){
							sceneService.removeSelfToNearby(targetId, sceneService.getNearbyPlayerIds(target));
						}
						continue;
					}
				}
				
				// 刷选目标 如pk模式 自己人等等
				if(baseSkill.geteSkillTargetCate() == SkillConstant.TARGET_ME){
					// 对自己的技能
					
					if(!target.getGuid().equals(basePuppet.getGuid())){
						continue;
					}
				}else if(baseSkill.geteSkillTargetCate() == SkillConstant.TARGET_TEAM_ALL){
					// 对全体队友技能
					
					if(fighterType != PlayerConstant.PLAYER){
						break;
					}
					PlayerExt p = playerService.getPlayerExtById(basePuppet.getEid());
					if(p.getTeamId() <= 0){
						break;
					}
					if(target.getType() != PlayerConstant.PLAYER){
						continue;
					}
					if(!target.getGuid().equals(basePuppet.getGuid())){
						PlayerExt t = playerService.getPlayerExtById(target.getEid());
						if(t.getTeamId() != p.getTeamId()){
							continue;
						}
					}
				}else if(baseSkill.geteSkillTargetCate() == SkillConstant.TARGET_TEAM_NO_ME){
					// 对队友除了自己的技能
					
					if(fighterType != PlayerConstant.PLAYER){
						break;
					}

					PlayerExt p = playerService.getPlayerExtById(basePuppet.getEid());
					if(p.getTeamId() <= 0){
						break;
					}

					if(target.getType() != PlayerConstant.PLAYER){
						continue;
					}
					
					if(target.getGuid().equals(basePuppet.getGuid())){
						continue;
					}
					
					PlayerExt t = playerService.getPlayerExtById(target.getEid());
					if(t.getTeamId() != p.getTeamId()){
						continue;
					}
				}else if(baseSkill.geteSkillTargetCate() == SkillConstant.TARGET_ENEMY){
					// 对全体敌人的技能
					if(basePuppet.getType() == PlayerConstant.PLAYER){
						PlayerPuppet attcker = (PlayerPuppet)basePuppet;
						PlayerPuppet targeter = null;
						if(target.getType() == PlayerConstant.PLAYER){
							targeter = (PlayerPuppet)target;
						}else if(target.getType() == PlayerConstant.BECKON){
							targeter = sceneService.getPlayerPuppet(((BeckonPuppet)target).getPlayerId());
						}
						
						if(targeter != null){
							if(baseMap.getPkModel() == SceneConstant.PK_MODEL_SAFE){
								// 安全模式的地图 只能杀红名玩家
								if(targeter.getNameColor() < 3){
									continue;
								}
								
							}else if(baseMap.getPkModel() == SceneConstant.PK_MODEL_PK || baseMap.getPkModel() == SceneConstant.PK_MODEL_PVP){
								if(attcker.getPkMode() == BattleConstant.PK_MODE_GOODBAD){
									if(targeter.getNameColor() == 1){
										continue;
									}
								}
								
								//城战
								if(baseMap.isGuild()){
									if(attcker.getPkType() != targeter.getPkType()){
										targetList.add(target);
										continue;
									}	
								}
							}
							
							//去掉了组队模式。 改成只要组队下都不能攻击
							PlayerExt p = playerService.getPlayerExtById(attcker.getEid());
							if(p.getTeamId() > 0){
								PlayerExt t = playerService.getPlayerExtById(targeter.getEid());
								if(p.getTeamId() == t.getTeamId()){
									continue;
								}
							}
							
							if(attcker.getPkMode() == BattleConstant.PK_MODE_PEACE){
								//和平模式
								break;
							}else if(attcker.getPkMode() == BattleConstant.PK_MODE_GUILD){
								//帮派模式
								PlayerGuild playerGuild = guildService.getPlayerGuild(attcker.getEid());
								if(playerGuild != null && playerGuild.getGuildId() > 0){
									PlayerGuild targetGuild = guildService.getPlayerGuild(targeter.getEid());
									if(targetGuild != null && targetGuild.getGuildId() == playerGuild.getGuildId()){
										continue;
									}
								}
							}else if(attcker.getPkMode() == BattleConstant.PK_MODE_FAMILY){
								//家族模式
								PlayerFamily pf = familyService.getPlayerFamily(attcker.getEid());
								if(pf != null && pf.getPlayerFamilyId() > 0){
									PlayerFamily targetPf = familyService.getPlayerFamily(targeter.getEid());
									if(pf.getPlayerFamilyId() == targetPf.getPlayerFamilyId()){
										continue;
									}
								}
							}
						}
					}
				}else if(baseSkill.geteSkillTargetCate() == SkillConstant.TARGET_ALL){
					
				}else if(baseSkill.geteSkillTargetCate() == SkillConstant.TARGET_US_ALL){
					
				}
				
				targetList.add(target);
			}
			
			if(targetList.isEmpty()) return;
			
			this.dealSkill(basePuppet, skillId, targetList, accountModelId, wigId);
		}

	}


	/**
	 * 处理技能
	 */
	private void dealSkill(BasePuppet basePuppet, int skillId, List<BasePuppet> targetList, int accountModelId, int wigId){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		ISkillService skillService = serviceCollection.getSkillService();
		ISceneService sceneService = serviceCollection.getSceneService();
		
		DamagModel damagModel = skillService.getDamagModel(accountModelId);
		
		if(damagModel == null) return;
		
		List<BasePuppet> listTargets = new ArrayList<BasePuppet>();
		List<Integer> listDmgs = new ArrayList<Integer>();
		
		S_SkillResult.Builder builder = S_SkillResult.newBuilder();
		
		// 触发给自己buff
		if(basePuppet.getHp() > 0 && damagModel.getMyBuffIdList() != null && !damagModel.getMyBuffIdList().isEmpty()){
			List<Buff> buffs = serviceCollection.getBuffService().addBuff(basePuppet, basePuppet, damagModel.getMyBuffIdList());
			if(!buffs.isEmpty()){
				for(Buff buff : buffs){
					builder.addBuffList(protoBuilderService.buildBuffMsg(buff));
				}
			}
		}
		
		for(BasePuppet target : targetList){
			
			// 过滤死亡者
			if(target.getHp() < 1){
				if(target.getType() > PlayerConstant.PLAYER){
					// 清理怪物残留
					sceneService.removeSelfToNearby(target.getGuid(), sceneService.getNearbyPlayerIds(target));
				}
				continue;
			}
			
			int[] resultArr = calDamge(basePuppet, target, damagModel);
			int dmg = 0;
			int realDmg = 0;
			int fightType = 0;
			if(resultArr != null){
				dmg = resultArr[0];
				realDmg = resultArr[1];
				fightType = resultArr[2];
				SkillEffect effect = new SkillEffect(target.getGuid(), dmg, target.getHp(), fightType);
				builder.addSkillEffect(protoBuilderService.buildSkillEffectMsg(effect));
			}
			
			// 触发给目标buff
			if(target.getHp() > 0 && damagModel.getBuffIdList() != null && !damagModel.getBuffIdList().isEmpty()){
				List<Buff> buffs = serviceCollection.getBuffService().addBuff(basePuppet, target, damagModel.getBuffIdList());
				if(!buffs.isEmpty()){
					for(Buff buff : buffs){
						builder.addBuffList(protoBuilderService.buildBuffMsg(buff));
					}
				}
			}
			
			listTargets.add(target);
			listDmgs.add(realDmg);
		}
		
		builder.setGuid(basePuppet.getGuid());
		builder.setSkillId(skillId);
		builder.setWigId(wigId);
		builder.setAccountModelId(accountModelId);
		
		// 如果是野外PK地图, 怪物技能和移动不开启九宫格
		List<Long> playerIds = null;
		BaseMap baseMap = sceneService.getBaseMap(basePuppet.getMapId());
		if(baseMap.getMapType() == SceneConstant.WORLD_SCENE &&	baseMap.getPkModel() == 3 && basePuppet.getType() == PlayerConstant.MONSTER){
			SceneModel sceneModel = serviceCollection.getSceneService().getSceneModel(basePuppet.getSceneGuid());
			playerIds = sceneService.getScenePlayerIds(sceneModel);
		}else{
		    playerIds = sceneService.getNearbyPlayerIds(basePuppet);
		}
		
		MessageObj msg = new MessageObj(MessageID.S_SkillResult_VALUE, builder.build().toByteArray());
		gameSocketService.sendDataToPlayerList(playerIds, msg);
		
		this.attack(basePuppet, listTargets, listDmgs);
	}
	
	/**
	 * 攻击
	 */
	public void attack(BasePuppet basePuppet, List<BasePuppet> listTargets, List<Integer> listDmgs){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		ISceneService sceneService = serviceCollection.getSceneService();
		IInstanceService instanceService = serviceCollection.getInstanceService();
		
		SceneModel sceneModel = sceneService.getSceneModel(basePuppet.getSceneGuid());
		if(sceneModel.getSceneState() == SceneConstant.SCENE_STATE_DISTROY) return;
		
		boolean bClear = false;
		// 产生掉落列表
		Map<Integer, List<DropItemInfo>> dropItems = new HashMap<Integer, List<DropItemInfo>>();
		
		for(int i = 0; i< listTargets.size(); i++){
			BasePuppet target = listTargets.get(i);
			int dmg = listDmgs.get(i);
			
			// 处理死亡
			if(target.getHp() < 1){
				target.setState(BattleConstant.STATE_DEAD);
				
				if(target.getType() == PlayerConstant.MONSTER){
					// 怪物死亡
					
					MonsterPuppet monster = (MonsterPuppet)target;
					bClear = this.monsterDead(sceneModel, basePuppet, monster, dmg, dropItems);
				}else if(target.getType() == PlayerConstant.BECKON){
					// 召唤怪死亡
					
					// 名字颜色变动 (竞技场内不作改变)
					if(sceneModel.getMapType() != SceneConstant.TIANTI_SCENE && sceneModel.getMapType() != SceneConstant.GUILD_SCENE && basePuppet.getType() == PlayerConstant.PLAYER){
						PlayerPuppet attcker = (PlayerPuppet)basePuppet;
						//召唤怪的主人
						long playerId = ((BeckonPuppet)target).getPlayerId();
						PlayerPuppet targeter = serviceCollection.getSceneService().getPlayerPuppet(playerId);
						
						if(targeter != null){
							if(targeter.getNameColor() == 1){
								if(attcker.getNameColor() < 3){
									attcker.setGrayUpdateTime(System.currentTimeMillis());
									if(attcker.getNameColor() == 1){
										attcker.setNameColor(2);
										playerService.synPlayerPropertyToAll(attcker, ProdefineConstant.NAME_COLOR, attcker.getNameColor());
									}
								}
							}	
						}
					}
					
					BeckonPuppet beckon = (BeckonPuppet)target;
					this.beckonDead(sceneModel, beckon);
				}else{
					// 角色死亡
					
					PlayerPuppet targeter = (PlayerPuppet)target;
					this.playerDead(sceneModel, basePuppet, targeter, dmg, dropItems);
				}
			} else{
				// 受伤
				if(dmg > 0){
					
					if(target.getType() == PlayerConstant.MONSTER){
						// 设置怪物敌人列表
						serviceCollection.getMonsterService().addMonsterEnemy(dmg, basePuppet, target);
						
					}else{
						PlayerPuppet targeter = null;
						if(target.getType() == PlayerConstant.BECKON){
							//召唤怪的主人
							long playerId = ((BeckonPuppet)target).getPlayerId();
							targeter = serviceCollection.getSceneService().getPlayerPuppet(playerId);
							
							if(targeter == null) return;
							
						}else{
							targeter = (PlayerPuppet)target;
							
							// 同步队员血量显示
							serviceCollection.getTeamService().synHp(targeter);
						}
					
						// 名字颜色变动 (竞技场内不作改变)
						if(sceneModel.getMapType() != SceneConstant.TIANTI_SCENE && sceneModel.getMapType() != SceneConstant.GUILD_SCENE && basePuppet.getType() == PlayerConstant.PLAYER){
							PlayerPuppet attcker = (PlayerPuppet)basePuppet;
							
							if(targeter.getNameColor() == 1){
								if(attcker.getNameColor() < 3){
									attcker.setGrayUpdateTime(System.currentTimeMillis());
									if(attcker.getNameColor() == 1){
										attcker.setNameColor(2);
										playerService.synPlayerPropertyToAll(attcker, ProdefineConstant.NAME_COLOR, attcker.getNameColor());
									}
								}
							}							
						}
					}
					
				}
				
			}
		}
		
		// 通关
		if(bClear){
			long playerId = 0; // 通关者
			if(basePuppet.getType() == PlayerConstant.PLAYER){
				playerId = basePuppet.getEid();
			}else if(basePuppet.getType() == PlayerConstant.BECKON){
				playerId = ((BeckonPuppet)basePuppet).getPlayerId();
			}else{
				return;
			}
			
			if(sceneModel.getMapType() == SceneConstant.TOWER_SCENE){
				// 大荒塔通关
				serviceCollection.getTowerService().end(playerId, 1, sceneModel);

			}else if(sceneModel.getMapType() == SceneConstant.INSTANCE_SCENE){								
				// 副本通关
				serviceCollection.getInstanceService().end(sceneModel, 1);				
				if(sceneModel.getMapType() == SceneConstant.INSTANCE_SCENE){
				
					BaseMap baseMap = sceneService.getBaseMap(sceneModel.getMapId());
					BaseInstanceReward baseInstanceReward = null;
					if(baseMap.getRewardType() > 0){
						PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerId);					
						baseInstanceReward = instanceService.getBaseInstanceReward(baseMap.getRewardType(), playerProperty.getLevel());
					}
					int index = 1;	
					for(Map.Entry<Integer, List<Long>> entry : sceneModel.getPlayerIdMap().entrySet()){
						for(Long pid : entry.getValue()){
							
							instanceService.costCount(pid, baseMap);
							
							// 副本结束前掉宝箱
							if(baseInstanceReward != null){
								index = this.createDrop(dropItems, "", basePuppet.getX(),
										basePuppet.getY(), basePuppet.getZ(), 
										RewardTypeConstant.BOX, 0, 0, 0, 0, null,
										index, baseMap, sceneModel, 300, 0);
							}
						}
					}			
					
				}

				if (sceneModel.getSceneState() == SceneConstant.SCENE_STATE_END){
					try {
						// 副本任务
						List<Integer> conditionList = new ArrayList<Integer>();
						conditionList.add(sceneModel.getMapId());								
						serviceCollection.getTaskService().executeTask(playerId, TaskConstant.TYPE_3, conditionList);
						
						//共享方式：组队并同地图
						PlayerExt playerExt = playerService.getPlayerExtById(playerId);
						if(playerExt.getTeamId() > 0){
							Team team = serviceCollection.getTeamService().getTeam(playerExt.getTeamId());
							if(team != null){
								GameSocketService gameSocketService = serviceCollection.getGameSocketService();
								for(Map.Entry<Long, TeamPlayer> entry : team.getTeamPlayerMap().entrySet()){
									TeamPlayer tp = entry.getValue();
									if(tp != null){
										if(tp.getPlayerId() == playerId){
											continue;
										}
										
										PlayerExt p = playerService.getPlayerExtById(tp.getPlayerId());
										if(gameSocketService.checkOnLine(tp.getPlayerId()) && p.getMapId() == playerExt.getMapId()){
											serviceCollection.getTaskService().executeTask(tp.getPlayerId(), TaskConstant.TYPE_3, conditionList);
										}
									}
								}
							}
						}
					} catch (Exception e) {
						LogUtil.error("执行副本任务出错：", e);
					}
				}
			}
		}
		
		// 产生掉落
		if(!dropItems.isEmpty()){
			
			for(Map.Entry<Integer, List<DropItemInfo>> entry : dropItems.entrySet()){
				List<Long> playerIds = serviceCollection.getSceneService().getNearbyPlayerIdsByGridId(sceneModel, entry.getKey());
				if(!playerIds.isEmpty()){
					this.offerDropItems(entry.getValue(), playerIds);
				}
			}
		
		}
	}
	
	/**
	 * 玩家死亡处理
	 */
	private void playerDead(SceneModel sceneModel, BasePuppet attacker, PlayerPuppet targeter, int dmg, Map<Integer, List<DropItemInfo>> dropItems){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IGuildService guildService = serviceCollection.getGuildService();
		
		if(attacker.getType() == PlayerConstant.MONSTER){
			// 角色被怪杀死
			if(sceneModel.getMapType() == SceneConstant.TOWER_SCENE){
				serviceCollection.getTowerService().end(targeter.getEid(), 2, sceneModel);
			}
		}else{
			
			PlayerPuppet attcker = null;
			if(attacker.getType() == PlayerConstant.BECKON){
				// 角色被召唤怪杀死
				
				long playerId = ((BeckonPuppet)attacker).getPlayerId();
				attcker = serviceCollection.getSceneService().getPlayerPuppet(playerId);
			}else{
				attcker = (PlayerPuppet)attacker;
			}
			if(attcker == null) return;
			
			if(sceneModel.getMapType() == SceneConstant.TIANTI_SCENE){
				// 天梯pk
				serviceCollection.getTiantiService().end(sceneModel, attcker.getEid(), targeter.getEid());
			}else if(sceneModel.getMapType() == SceneConstant.GUILD_SCENE){
				//城战
				PlayerGuild targetGuild = guildService.getPlayerGuild(targeter.getEid());
				if(targetGuild != null && targetGuild.getRoleId() > GuildConstant.ROLE_1){
					Guild guild = guildService.getGuildById(targetGuild.getGuildId());
					if(guild != null){
						//玩家{0}在长安城战中击杀了【{1}】都护府的{2}{3}
						List<Notice> paramList = new ArrayList<Notice>();
						Notice notice1 = new Notice(ParamType.PLAYER, attcker.getEid(), 0, attcker.getName());
						paramList.add(notice1);
						
						Notice notice2 = new Notice(ParamType.PARAM, 0, 0, guild.getGuildName());
						paramList.add(notice2);
						
						Notice notice3 = new Notice(ParamType.PARAM, 0, 0, guildService.getRoleName(targetGuild.getRoleId()));
						paramList.add(notice3);
						
						Notice notice4 = new Notice(ParamType.PLAYER, targeter.getEid(), 0, targeter.getName());
						paramList.add(notice4);
						
						GameSocketService gameSocketService = serviceCollection.getGameSocketService();
						serviceCollection.getChatService().synNotice(ChatConstant.CHAT_NOTICE_MAG_59, paramList,  gameSocketService.getOnLinePlayerIDList());
					}
	
				}
			}else{
				// 玩家pk死亡掉落
				addDropItems(dropItems, targeter, sceneModel);
				
				if(dmg > 0){
					boolean isGuildWar = false;
					//是否帮战中
					if(sceneModel.getMapType() == SceneConstant.GUILD_SCENE){
						isGuildWar = true;
					}else{
						//是否帮派宣战中
						if(attcker.getPkMode() == BattleConstant.PK_MODE_GUILD){
							isGuildWar = serviceCollection.getGuildService().isGuildWar(attcker.getEid(), targeter.getEid());
						}
					}
					if(!isGuildWar){
						// 同步pk值与红名
						if(targeter.getNameColor() == 1){
							if(attcker.getPkVlaue() == 0){
								attcker.setPkValueUpdateTime(System.currentTimeMillis());
							}
							attcker.setPkVlaue(attcker.getPkVlaue() + 100);
						
							if(attcker.getPkVlaue() >= 300){
								if(attcker.getNameColor() != 3){
									attcker.setNameColor(3);
									playerService.synPlayerPropertyToAll(attcker, ProdefineConstant.NAME_COLOR, attcker.getNameColor());
								}
								
							}else{
								attcker.setGrayUpdateTime(System.currentTimeMillis());
								if(attcker.getNameColor() == 1){
									attcker.setNameColor(2);
									playerService.synPlayerPropertyToAll(attcker, ProdefineConstant.NAME_COLOR, attcker.getNameColor());
								}
							}
							
							playerService.synPlayerProperty(attcker.getEid(), ProdefineConstant.PK_VALUE, attcker.getPkVlaue());
						}	
					}
				}
				
				try {
					// 砍死人获得经验=（0.01*敌人等级^3+0.05*敌人等级^2+敌人等级+2）*10
					int addExp = (int)((0.01 * Math.pow(targeter.getLevel(), 3) + 0.05*Math.pow(targeter.getLevel(), 2)+targeter.getLevel()+2)*10);
					playerService.addPlayerExp(attcker.getEid(), addExp);
				} catch (Exception e) {
					LogUtil.error("玩家pk获得经验异常：", e);
				}
				
				// 仇敌
				serviceCollection.getEnemyService().addEnemy(targeter.getEid(), attcker.getEid());	
				
				//击杀公告
				List<Notice> paramList = new ArrayList<Notice>();
				Notice notice1 = new Notice(ParamType.PLAYER, attcker.getEid(), 0, attcker.getName());
				paramList.add(notice1);
				
				Notice notice2 = new Notice(ParamType.SCENE, 0, 0, sceneModel.getMapName());
				paramList.add(notice2);
				
				Notice notice3 = new Notice(ParamType.PLAYER, targeter.getEid(), 0, targeter.getName());
				paramList.add(notice3);
				
				GameSocketService gameSocketService = serviceCollection.getGameSocketService();
				serviceCollection.getChatService().synNotice(ChatConstant.CHAT_NOTICE_MAG_18, paramList,  gameSocketService.getOnLinePlayerIDList());
			}			
		}
		
		// 同步队员血量显示
		serviceCollection.getTeamService().synHp(targeter);
		
		// 铭文移除
//		serviceCollection.getEpigraphService().clearPlayerWeaponEffect(targeter.getEid());
					
		// 移除召唤怪
		serviceCollection.getMonsterService().removeBeckonPuppet(targeter, sceneModel);
		
		// 死亡buff移除
		serviceCollection.getBuffService().dealDead(targeter);		
	}
	
	/**
	 * 怪物死亡处理
	 */
	private boolean monsterDead(SceneModel sceneModel, BasePuppet attacker, MonsterPuppet monster, int dmg, Map<Integer, List<DropItemInfo>> dropItems){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		IMonsterService monsterService = serviceCollection.getMonsterService();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		boolean bClear = false;
		
		monster.setDeadTime(System.currentTimeMillis());
		
		// 移除怪物列表
		Map<String, MonsterPuppet> monMap = sceneModel.getMonsterPuppetMap().get(monster.getGridId());
		if(monMap != null){
			monMap.remove(monster.getGuid());
		}
		
		//击杀者
		long playerId = 0;
		if(attacker.getType() == PlayerConstant.PLAYER){
			playerId = attacker.getEid();
		}else if(attacker.getType() == PlayerConstant.BECKON){
			playerId = ((BeckonPuppet)attacker).getPlayerId();
		}else{
			return false;
		}
		
		if(sceneModel.getMapType() >= SceneConstant.TOWER_SCENE){
			bClear = true;
			for(Map.Entry<Integer, Map<String, MonsterPuppet>> entry : sceneModel.getMonsterPuppetMap().entrySet()){
				Map<String, MonsterPuppet> map = entry.getValue();
				if(!map.isEmpty()){
					bClear = false;
					break;
				}
			}
			
		}else{
			// 判断怪物刷新
			if(monster.getRefreshTime() > 0){
				sceneModel.getDeadList().offer(monster);					
			}
		}
		
		// 设置怪物敌人列表
		if(dmg > 0){
			monsterService.addMonsterEnemy(dmg, attacker, monster);
		}
		
		// 杀怪掉落
		addDropItems(dropItems, monster, sceneModel);
		
		int monsterId = (int)monster.getEid();
		
		// 杀怪经验  （伤害最高者以及其队员）
		long dmgPlayerId = playerId;
		Collection<EnemyModel> enemyList = monster.getEnemyMap().values();
		if(enemyList != null && enemyList.size() > 1){
			List<EnemyModel> enemys = new ArrayList<EnemyModel>();
			enemys.addAll(enemyList);
			Collections.sort(enemys, new ComparatorUtil(ComparatorUtil.DOWN));
			
			dmgPlayerId = enemys.get(0).getPlayerId();
		}
		monsterService.addPlayerExp(dmgPlayerId, monsterId);
		
		try {
			// 杀怪任务
			List<Integer> conditionList = new ArrayList<Integer>();
			conditionList.add(monster.getMapId());
			conditionList.add(monsterId);	                       		
			
			// 队伍ID
			HashSet<Integer> teamIds = new HashSet<>();	
			//没有队伍玩家ID
			List<Long> playerIds =  new ArrayList<>();
			
			//共享方式：造成伤害
			for(Map.Entry<String, EnemyModel> entry : monster.getEnemyMap().entrySet()){
				Long eid = entry.getValue().getPlayerId();
				PlayerExt playerExt = playerService.getPlayerExtById(eid);
				if(playerExt.getTeamId() > 0){
					teamIds.add(playerExt.getTeamId());
				}else{				
					playerIds.add(eid);
				}				
			}
			
			//共享方式：组队并同地图				
			for(int teamId : teamIds){
				Team team = serviceCollection.getTeamService().getTeam(teamId);
				if(team != null){					
					for(Map.Entry<Long, TeamPlayer> entry1 : team.getTeamPlayerMap().entrySet()){
						TeamPlayer tp = entry1.getValue();
						if(tp != null){
							PlayerExt p = playerService.getPlayerExtById(tp.getPlayerId());
							if(gameSocketService.checkOnLine(tp.getPlayerId()) && p.getMapId() == sceneModel.getMapId()){
								serviceCollection.getTaskService().executeTask(tp.getPlayerId(), TaskConstant.TYPE_4, conditionList);
								serviceCollection.getTaskService().executeTask(tp.getPlayerId(), TaskConstant.TYPE_12, conditionList);
							}
						}
					}
				}
				
			}
			
			// 没有队伍的玩家
			for(long id : playerIds){
				PlayerExt p = playerService.getPlayerExtById(id);
				if(gameSocketService.checkOnLine(id) && p.getMapId() == sceneModel.getMapId()){
					serviceCollection.getTaskService().executeTask(id, TaskConstant.TYPE_4, conditionList);
					serviceCollection.getTaskService().executeTask(id, TaskConstant.TYPE_12, conditionList);
				}
			}
			
		} catch (Exception e) {
			LogUtil.error("杀怪任务异常：", e);
		}	

		return bClear;
	}
	
	/**
	 * 召唤兽死亡处理
	 */
	private void beckonDead(SceneModel sceneModel, BeckonPuppet beckon){
		Map<String, BeckonPuppet> beckMap = sceneModel.getBeckonPuppetMap().get(beckon.getGridId());
		if(beckMap != null){
			beckMap.remove(beckon.getGuid());
		}
	}
	
	@Override
	public void offerDropItems(List<DropItemInfo> dropItems, List<Long> playerIds){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		S_AddDropItemInfos.Builder dropBuidler = S_AddDropItemInfos.newBuilder();
		for(DropItemInfo info : dropItems){
			dropBuidler.addListDropItemInfos(protoBuilderService.buildDropItemInfoMsg(info));
		}
		MessageObj msg = new MessageObj(MessageID.S_AddDropItemInfos_VALUE, dropBuidler.build().toByteArray());
		serviceCollection.getGameSocketService().sendDataToPlayerList(playerIds, msg);
	}

	/**
	 * 爆出掉落
	 */
	private void addDropItems(Map<Integer, List<DropItemInfo>> dropItems, BasePuppet target, SceneModel sceneModel){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		
		BaseMap baseMap = serviceCollection.getSceneService().getBaseMap(target.getMapId());
		
		if(target.getType() == PlayerConstant.MONSTER){
			BaseMonster baseMonster = serviceCollection.getMonsterService().getBaseMonster((int)target.getEid());
			
			long playerId = 0; // 仇恨最高的击杀者
			List<Long> belongPlayerIds = null;  //归属者   
			Collection<EnemyModel> enemyList = target.getEnemyMap().values(); //仇恨列表
			if(enemyList != null && !enemyList.isEmpty()){
				List<EnemyModel> enemys = new ArrayList<EnemyModel>();
				enemys.addAll(enemyList);
				Collections.sort(enemys, new ComparatorUtil(ComparatorUtil.DOWN));
				
				playerId = enemys.get(0).getPlayerId();
			}
			
			if(playerId <= 0) return;
			
			if(baseMonster.getDropType() == 0){ //0：伤害最高者及其队友   
				belongPlayerIds = new ArrayList<Long>(4);

				PlayerExt playerExt = serviceCollection.getPlayerService().getPlayerExtById(playerId);
				if(playerExt.getTeamId() > 0){
					ITeamService teamService = serviceCollection.getTeamService();
					Team team = teamService.getTeam(playerExt.getTeamId());	
					if(team != null){
						List<Long> playerIds = teamService.getOnlineTeamPlayerIds(team);
						belongPlayerIds.addAll(playerIds);	
					}else{
						belongPlayerIds.add(playerId);			
					}
				}else{
					belongPlayerIds.add(playerId);	
				}
			}else if(baseMonster.getDropType() == 1){ //1：所有参与者  
				belongPlayerIds = new ArrayList<Long>();
				for(EnemyModel model : enemyList){
					belongPlayerIds.add(model.getPlayerId());
				}
			}else{ //2 所有者，无归属 
				
			}
		
			List<Reward> dropInfos = baseMonster.getDropInfoList();
			if(dropInfos != null && !dropInfos.isEmpty()){
				int rand = RandomService.getRandomNum(2);
				int index = 1;
				for(Reward model : dropInfos){
					if(model.getRate() > 0){
						double random = Math.random() * 10000;
						if(random < model.getRate()){
							// 掉落
							index = this.createDrop(dropItems, target.getGuid(), target.getX(), 
									target.getY(), target.getZ(), model.getType(), model.getId(), 
									model.getNum(), model.getBlind(), 0, belongPlayerIds, index, baseMap, sceneModel, 100, rand);
						}
					}
				}
			}
			
			//击杀野外boss广播
			if(baseMonster.getMonsterType() == BattleConstant.MONSTER_TYPE_3 && baseMap.isWorldScene()){
				Player player = serviceCollection.getPlayerService().getPlayerByID(playerId);
				List<Notice> paramList = new ArrayList<Notice>();	
				Notice notice1 = new Notice(ParamType.PLAYER, playerId, 0, player.getPlayerName());
				Notice notice2 = new Notice(ParamType.SCENE, sceneModel.getMapId(), 0, sceneModel.getMapName());
				Notice notice3 = new Notice(ParamType.PARAM, 0, 0, baseMonster.getName());
				
				paramList.add(notice1);
				paramList.add(notice2);
				paramList.add(notice3);
				
				for(Map.Entry<Integer, List<DropItemInfo>> entry : dropItems.entrySet()){
					List<DropItemInfo> lists = entry.getValue();
					for(DropItemInfo info : lists){
						Notice notice = null;
						if(info.getGoodsType() == RewardTypeConstant.EQUIPMENT){
							BaseEquipment baseEquipment = serviceCollection.getEquipmentService().getBaseEquipmentById(info.getItemId());
							if(baseEquipment.getShowType() == 1){
								notice = new Notice(ParamType.EQUIPMENT, playerId, info.getItemId(), info.getPlayerEquipmentId()+"");
							}
						}else if(info.getGoodsType() == RewardTypeConstant.ITEM){						
							BaseItem baseItem =  serviceCollection.getBagService().getBaseItemById(info.getItemId());
							if(baseItem.getShowType() == 1){
								notice = new Notice(ParamType.ITEM, info.getItemId(), 0, "");
							}
						}
						if(notice != null){
							paramList.add(notice);
						}
					}
				}
				if(paramList.size() == 3){
					serviceCollection.getChatService().synNotice(ChatConstant.CHAT_NOTICE_MAG_44, paramList, serviceCollection.getGameSocketService().getOnLinePlayerIDList());	
				}else{
					serviceCollection.getChatService().synNotice(ChatConstant.CHAT_NOTICE_MAG_45, paramList, serviceCollection.getGameSocketService().getOnLinePlayerIDList());				
				}
			}
			
		}else if(target.getType() == PlayerConstant.PLAYER){
			int nameColor = ((PlayerPuppet) target).getNameColor();
			
			// 切磋地图非红名玩家不掉落
			if(baseMap.getPkModel() == SceneConstant.PK_MODEL_PVP){
				if(nameColor < 3) return;
			}
			
			IEquipmentService equipmentService = serviceCollection.getEquipmentService();
			
			int index = 1;
			
			S_SynBagItem.Builder builder = S_SynBagItem.newBuilder();
			
			List<PlayerEquipment> dressEquipList = equipmentService.getPlayerEquipmentListByState(target.getEid(), ItemConstant.EQUIP_STATE_DRESS);
			Collections.shuffle(dressEquipList);
			for(PlayerEquipment equip : dressEquipList){
				BaseEquipment baseEquipment = equipmentService.getBaseEquipmentById(equip.getEquipmentId());
				if(baseEquipment.getIsPkDrop() == 0 || equip.getIsBinding() == 1) continue;
				
				int pkDropRate = equipmentService.getPkDropRate(equip.getEquipType(), nameColor);
				double random = Math.random() * 10000;
				if(random < pkDropRate){
					
					// 处理玩家装备位灵力值
					IWakanService wakanService =  serviceCollection.getWakanService();
					wakanService.changeProValueByEquipment(target.getEid(), equip.getEquipType(), -1);
					
					if(equip.getEquipType() == WakanConstant.WEAPON01){
						// 更换主武器外形
						BaseEquipment equipment = equipmentService.getBaseEquipmentById(equip.getEquipmentId());
						if(equipment.getWeaponStyle() > 0){
							IPlayerService playerService = serviceCollection.getPlayerService();
							ICommonService commonService = serviceCollection.getCommonService();
							Player player = playerService.getPlayerByID(target.getEid());
							
							BaseNewRole baseNewRole = commonService.getBaseNewRole(player.getCareer());
							
							PlayerPuppet playerPuppet = serviceCollection.getSceneService().getPlayerPuppet(target.getEid());
							if(playerPuppet != null){
								playerPuppet.setWeaponStyle(baseNewRole.getWeaponStyle());
								playerService.synPlayerProperty(target.getEid(), ProdefineConstant.WEAPON_STYLE, baseNewRole.getWeaponStyle());			
							}
							// 同步队伍信息
							serviceCollection.getTeamService().synTeam(target.getEid());
						}
						
						// 更改玩家技能铭文信息
						serviceCollection.getEpigraphService().clearPlayerWeaponEffect(target.getEid());
					}
					
					// 处理脱装备副属性
					equipmentService.handleInfoWhenChangeEquipment(equip, -1, true);
					
					// 从装备栏移除
					equip.setState(ItemConstant.EQUIP_STATE_DELETE);
					equip.setPlayerId(0);
					equipmentService.updatePlayerEquipment(equip);
					equipmentService.getPlayerEquipmentList(target.getEid()).remove(equip);
					builder.addListPlayerEquipments(protoBuilderService.buildPlayerEquipmentMsg(equip));
					equipmentService.addDropEquipmentCache(equip);
					
					MessageObj msg = new MessageObj(MessageID.S_SynBagItem_VALUE, builder.build().toByteArray());
					serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(target.getEid(), msg);
					
					index = this.createDrop(dropItems, target.getGuid(), target.getX(), 
							target.getY(), target.getZ(), RewardTypeConstant.EQUIPMENT, 
							equip.getEquipmentId(), 1, ItemConstant.ITEM_NOT_BINDING, equip.getPlayerEquipmentId(), null,
							index, baseMap, sceneModel, 100, 0);
					
					// 只掉一件
					break;
				}
			}
			
			// 关闭背包掉落
//			List<PlayerEquipment> bagEquipList = equipmentService.getPlayerEquipmentListByState(target.getEid(), ItemConstant.EQUIP_STATE_BACKPACK);
//			for(PlayerEquipment equip : bagEquipList){
//				int pkDropRate = equipmentService.getPkDropRate(0, nameColor);
//				double random = Math.random() * 10000;
//				if(random < pkDropRate){
//					
			// //从背包栏移除
//					PlayerBag playerBag = bagService.getPlayerBagForEquipment(target.getEid(), equip.getPlayerEquipmentId());
//					if(playerBag == null) continue;
//					
//					playerBag.reset();
//					bagService.updatePlayerBag(playerBag);
//					builder.addListPlayerBags(protoBuilderService.buildPlayerBagMsg(playerBag));
//					
//					equip.setState(ItemConstant.EQUIP_STATE_DELETE);
//					equip.setPlayerId(0);
//					equipmentService.updatePlayerEquipment(equip);
//					equipmentService.getPlayerEquipmentList(target.getEid()).remove(equip);
//					builder.addListPlayerEquipments(protoBuilderService.buildPlayerEquipmentMsg(equip));
//					equipmentService.addDropEquipmentCache(equip);
//					
//					MessageObj msg = new MessageObj(MessageID.S_SynBagItem_VALUE, builder.build().toByteArray());
//					serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(target.getEid(), msg);
//					
//					index = this.createDrop(dropItems, target, RewardTypeConstant.EQUIPMENT, equip.getEquipmentId(), 1, equip.getPlayerEquipmentId(), null, index, baseMap, sceneModel);
//				}
//			}
		}
		
	}
	
	/**
	 * 创建掉落品
	 */
	@Override
	public int createDrop(Map<Integer, List<DropItemInfo>> dropItems, String targetGuid, 
			int x, int y, int z, int goodsType, int itemId, int num, int blind, long playerEquipmentId,
			List<Long> belongPlayerIds, int index, BaseMap baseMap, SceneModel sceneModel, 
			int offset, int randomType){
		// 掉落
		DropItemInfo drop = new DropItemInfo();
		drop.setDropId(SerialNumberUtil.getDropId());		
		drop.setState(BattleConstant.DROP_NORMAL);
		drop.setGoodsType(goodsType);	
		drop.setX(x);
		drop.setY(y);
		drop.setZ(z);	
		drop.setBlind(blind);
		
		if(goodsType != RewardTypeConstant.BOX){
			drop.setTargetGuid(targetGuid);
			drop.setDropTime(System.currentTimeMillis());
			if(goodsType == RewardTypeConstant.MONEY){
				int money = (int)(num * (Math.random()*0.2 + 0.9));
				drop.setNum(money);
			}else if(goodsType == RewardTypeConstant.ITEM){
				drop.setItemId(itemId);
				drop.setNum(num);
			}else if(goodsType == RewardTypeConstant.EQUIPMENT){
				drop.setItemId(itemId);
				drop.setNum(num);
				drop.setPlayerEquipmentId(playerEquipmentId);
			}else if(goodsType == RewardTypeConstant.BUFF){
				drop.setItemId(itemId);
				drop.setNum(num);
			}
			
			drop.setBelongPlayerIds(belongPlayerIds);	
		}	
		
		if(index > 0){
			if(randomType == 0){
				index = this.getDropPonit(baseMap, drop, index, offset);
			}else if(randomType == 1){
				index = this.getDropPonit2(baseMap, drop, index, offset);	
			}
		}
		
		drop.setGridId(NineGridUtil.calInGrid(drop.getX(), drop.getZ(), sceneModel.getColNum()));
		
		Map<Integer, DropItemInfo> map = sceneModel.getDropItemMap().get(drop.getGridId());
		if(map == null){
			map = new ConcurrentHashMap<Integer, DropItemInfo>();
			sceneModel.getDropItemMap().put(drop.getGridId(), map);
		}
		map.put(drop.getDropId(), drop);
		
		List<DropItemInfo> infoList = dropItems.get(drop.getGridId());
		if(infoList == null){
			infoList =Collections.synchronizedList(new ArrayList<DropItemInfo>()) ;
			dropItems.put(drop.getGridId(), infoList);
		}
		infoList.add(drop);
		
		return index;
	}
	
	/**
	 * 掉落点选择  九宫格模式
	 */
	private int getDropPonit(BaseMap baseMap, DropItemInfo drop, int index, int offset){
		int x = drop.getX();
		int z = drop.getZ();		
		switch (index) {
		case 1:
			x = drop.getX() - offset;
			break;
		case 2:
			x = drop.getX() - offset;
			z = drop.getZ() + offset;
			break;
		case 3:
			z = drop.getZ() + offset;
			break;
		case 4:
			x = drop.getX() + offset;
			z = drop.getZ() + offset;
			break;
		case 5:
			x = drop.getX() + offset;
			break;
		case 6:
			x = drop.getX() + offset;
			z = drop.getZ() - offset;
			break;
		case 7:
			z = drop.getZ() - offset;
			break;
		case 8:
			x = drop.getX() - offset;
			z = drop.getZ() - offset;
			break;

		default:
			index = 1;
			offset += offset;
			return getDropPonit(baseMap, drop, index, offset);
		}
		index++;
		if(baseMap.isBlock(x, z)){
			return getDropPonit(baseMap, drop, index, offset);
		}
		drop.setX(x);
		drop.setZ(z);
		return index;
	}
	
	/**
	 * 掉落点选择  圆内随机模式
	 */
	private int getDropPonit2(BaseMap baseMap, DropItemInfo drop, int index, int offset){
		int x = drop.getX();
		int z = drop.getZ();		
		
		int[] point = CommonUtil.getRandomPoint(baseMap, x, z, offset*2);
		drop.setX(point[0]);
		drop.setZ(point[1]);
		index++;
	
		return index;
	}
	
	@Override
	public void removeDropItems(List<Integer> dropIds, List<Long> playerIds) {
		
		S_RemoveDropItemInfos.Builder builder = S_RemoveDropItemInfos.newBuilder();
		builder.addAllDropIds(dropIds);
		MessageObj msg = new MessageObj(MessageID.S_RemoveDropItemInfos_VALUE, builder.build().toByteArray());
		GameContext.getInstance().getServiceCollection().getGameSocketService().sendDataToPlayerList(playerIds, msg);
	}

	@Override
	public void pickup(GameMessage gameMessage) throws Exception {
		C_Pickup param = C_Pickup.parseFrom(gameMessage.getData());
		Integer dropId = param.getDropId();		
		Long playerId = gameMessage.getConnection().getPlayerId();		
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		IRewardService rewardService = serviceCollection.getRewardService();
		ISceneService sceneService = serviceCollection.getSceneService();
		IInstanceService instanceService = serviceCollection.getInstanceService();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IChatService chatService = serviceCollection.getChatService();
		IEquipmentService equipmentService = serviceCollection.getEquipmentService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_PICKUP)) {
			
			PlayerPuppet playerPuppet = sceneService.getPlayerPuppet(playerId);
			if(playerPuppet == null) return;
			
			if(playerPuppet.getState() != BattleConstant.STATE_NORMAL) return;
			
			SceneModel sceneModel = sceneService.getSceneModel(playerPuppet.getSceneGuid());
			
			if(sceneModel.getSceneState() == SceneConstant.SCENE_STATE_DISTROY) return;
			
			DropItemInfo info = null;
			
			for(Map.Entry<Integer, Map<Integer, DropItemInfo>> entry : sceneModel.getDropItemMap().entrySet()){
				Map<Integer, DropItemInfo> map = entry.getValue();
				info = map.get(dropId);
				if(info != null){
					break;
				}
			}
			
			if(info == null || info.getState() != BattleConstant.DROP_NORMAL){
				return;
			}
			synchronized (info.getPickLock()) {
				if(info.getState() != BattleConstant.DROP_NORMAL){
					return;
				}
				
				if(info.getBelongPlayerIds() != null && !info.getBelongPlayerIds().contains(playerId)){
					throw new GameException(ExceptionConstant.BAG_1309);
				}				
							
				// 根据玩家场景ID,和队伍等级ID,取出奖励
				BaseMap baseMap = sceneService.getBaseMap(sceneModel.getMapId());	
				if(info.getGoodsType() == RewardTypeConstant.BOX){
					if(playerPuppet.isPickUp()) throw new GameException(ExceptionConstant.BAG_1310);
					
					PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerId);					
					BaseInstanceReward baseInstanceReward = instanceService.getBaseInstanceReward(baseMap.getRewardType(), playerProperty.getLevel());
					List<List<Object>> resultList = null;
					if(baseInstanceReward.getRandType() == 2){
						List<Reward> list = rewardService.independRandom(baseInstanceReward.getRewardList());
						resultList = rewardService.fetchRewardList(playerId, list);
					}else if(baseInstanceReward.getRandType() == 1){
						Reward reward = rewardService.globalRandom(baseInstanceReward.getRewardList());
						resultList = rewardService.fetchRewardOne(playerId, reward.getType(), reward.getId(), reward.getNum(), reward.getBlind());
					}
					
					playerPuppet.setPickUp(true);
					
					//拾取宝箱 公告
					if(resultList != null){
						for(List<Object> list : resultList){
							int type = (Integer)list.get(0);
							int baseId = (Integer)list.get(1);
							long pEquipId = (Long)list.get(2);
							
							//拾取公告
							Notice notice = null;
							if(type == RewardTypeConstant.EQUIPMENT){
								BaseEquipment baseEquipment = equipmentService.getBaseEquipmentById(baseId);
								if(baseEquipment.getShowType() == 1){
									notice = new Notice(ParamType.EQUIPMENT, playerId, baseId, pEquipId+"");
								}
							}else if(type == RewardTypeConstant.ITEM){						
								BaseItem baseItem =  serviceCollection.getBagService().getBaseItemById(baseId);
								if(baseItem.getShowType() == 1){
									notice = new Notice(ParamType.ITEM, baseId, 0, "");
								}
							}
					
							if(notice != null){
								Notice noticePlayer = new Notice(ParamType.PLAYER, playerId, 0, playerPuppet.getName());
								Notice noticeInstance = new Notice(ParamType.SCENE, 0, 0, baseMap.getMap_name());							
								List<Notice> paramList = new ArrayList<Notice>();
								paramList.add(noticePlayer);
								paramList.add(noticeInstance);
								paramList.add(notice);
								
								chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_1, paramList, gameSocketService.getOnLinePlayerIDList());
							}
						}
					}
					
				}else{
					
					int x = info.getX();
					int z = info.getZ();
					
					int space = (x-playerPuppet.getX())*(x-playerPuppet.getX()) + (z-playerPuppet.getZ())*(z-playerPuppet.getZ());
					if(space > 90000){
						//LogUtil.warn("拾取太远了 " + Math.sqrt(space));
						return;
					}
					
					long currentTime = System.currentTimeMillis();
					long calTime = currentTime - info.getDropTime();
					// 拾取等待时间
					if(calTime < 2000) return;
					
					if(info.getPlayerEquipmentId() > 0){
						rewardService.fetchDropEquipment(playerId, info.getPlayerEquipmentId());
					}else{
						rewardService.fetchRewardOne(playerId, info.getGoodsType(), info.getItemId(), info.getNum(), info.getBlind());	
					}

				}				
				
				info.setState(BattleConstant.DROP_PICKUP);	
				sceneModel.getDropItemMap().get(info.getGridId()).remove(dropId);
				
				List<Long> playerIds = sceneService.getNearbyPlayerIdsByGridId(sceneModel, info.getGridId());
				// 通知别人掉落移除
				if(playerIds != null && playerIds.size() > 1){
					List<Long> newIDList = new ArrayList<Long>(playerIds);
					newIDList.remove(playerId);
					
					List<Integer> pids = new ArrayList<Integer>();
					pids.add(dropId);
					
					removeDropItems(pids, newIDList);
				}
				
				S_Pickup.Builder builder = S_Pickup.newBuilder();
				builder.setDropId(dropId);

				MessageObj msg = new MessageObj(MessageID.S_Pickup_VALUE, builder.build().toByteArray());
				gameSocketService.sendData(gameMessage.getConnection(), msg);
			}			
		}		
	}

	@Override
	public void revive(long playerId, int type) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		ISceneService sceneService = serviceCollection.getSceneService();
		IBuffService buffService = serviceCollection.getBuffService();
		
		PlayerPuppet playerPuppet = sceneService.getPlayerPuppet(playerId);
		if(playerPuppet == null) throw new GameException(ExceptionConstant.PLAYER_1111);
		
		if(playerPuppet.getState() != BattleConstant.STATE_DEAD) throw new GameException(ExceptionConstant.ERROR_10000);
		
		BaseMap baseMap = sceneService.getBaseMap(playerPuppet.getMapId());
		
		
		int FRESH_MAP_ID = serviceCollection.getCommonService().getConfigValue(ConfigConstant.FRESH_MAP_ID);
		
		synchronized (playerPuppet.getUseSkillLock()) {
			
			if(type == 1){
				// 普通
				sceneService.resetPuppet(playerPuppet);
				if(baseMap.getMap_id() == FRESH_MAP_ID){
					sceneService.enterScene(playerId, FRESH_MAP_ID, 0, false, null, 0);
				}else{
					if(baseMap.isInstance()){
						if(baseMap.isGuild()){
							GuildFight guildFight = serviceCollection.getGuildService().getGuildFightCache();
							if(guildFight.getState() == 3){
								BaseMap toMap = sceneService.getBaseMap(GuildConstant.MAP_GUILD_7001);
								sceneService.transfer(playerId, toMap.getMap_id(), toMap.getRevivePositions().get(playerPuppet.getPkType() - 1));
							}else{
								PlayerExt playerExt = serviceCollection.getPlayerService().getPlayerExtById(playerId);
								sceneService.enterScene(playerId, playerExt.getLastMapId(), 0, false, null, 0);	
							}
						}else{
							PlayerExt playerExt = serviceCollection.getPlayerService().getPlayerExtById(playerId);
							sceneService.enterScene(playerId, playerExt.getLastMapId(), 0, false, null, 0);	
						}
							
					}else{
						
						sceneService.enterScene(playerId, 1001, 0, false, null, 0);
					}

				}

			}else if(type == 2){
				int ITEM_ID_23001 = serviceCollection.getCommonService().getConfigValue(ConfigConstant.ITEM_ID_23001);
				
				// 道具复活
				IRewardService rewardService = serviceCollection.getRewardService();
				rewardService.deductItem(playerId, ITEM_ID_23001, 1, true);
				
				sceneService.resetPuppet(playerPuppet);
				
				
				// 加buff
				int REVIVE_BUFF_ID = serviceCollection.getCommonService().getConfigValue(ConfigConstant.REVIVE_BUFF_ID);
				buffService.addBuffById(playerId, REVIVE_BUFF_ID);
				
				S_Revive.Builder builder = S_Revive.newBuilder();
				builder.setGuid(playerPuppet.getGuid());
				builder.setHp(playerPuppet.getHp());
				builder.setMp(playerPuppet.getMp());
				builder.setHpMax(playerPuppet.getHpMax());
				builder.setMpMax(playerPuppet.getMpMax());
				builder.setType(type);
				
				List<Long> playerIds = sceneService.getNearbyPlayerIds(playerPuppet);
				MessageObj msg = new MessageObj(MessageID.S_Revive_VALUE, builder.build().toByteArray());
				serviceCollection.getGameSocketService().sendDataToPlayerList(playerIds, msg);
				
			}else{
				LogUtil.error("未开放其他复活方式");
			}
			
			// 同步队员血量显示
			serviceCollection.getTeamService().synHp(playerPuppet);
		}
	}
	
	/**
	 * 伤害计算
	 */
	private int[] calDamge(BasePuppet attacker, BasePuppet target, DamagModel damagModel){
		int damage = 0;
		int fightResult = BattleConstant.FIGHT_RESULT_NOMAL;
		
		if(target.getType() == PlayerConstant.MONSTER){
			MonsterPuppet monsterPuppet = (MonsterPuppet)target;
			if(monsterPuppet.getAiState() == BattleConstant.AI_STATE_GOBACK || monsterPuppet.getAiState() == BattleConstant.AI_STATE_IMMUNE){
				return new int[]{0, 0, BattleConstant.FIGHT_RESULT_MISS};
			}
		}
		// 免疫
		if(target.getImmune() == 1){
			return new int[]{0, 0, BattleConstant.FIGHT_RESULT_MISS};
		}
		
		int realDmg = target.getHp();
		if(damagModel.geteEffectCate() == SkillConstant.DAMAGE_P_ATTACK){
			// 基础伤害
			double hurt = attacker.getP_attack() - target.getP_damage() + target.getP_damage() * Math.pow(Math.E, 1-Math.pow(Math.E, attacker.getP_attack() * 1.0 / target.getP_damage()));

			// 技能系数
			hurt = hurt * (damagModel.getN32EffectRate()/1000.0) + damagModel.getN32EffectAdd();
			
			// 暴击判定
			double critRandom = 0.05 + 2 * (attacker.getCrit() - target.getTough())*0.001;
			if(Math.random() < critRandom){
				hurt = hurt * (attacker.getDmgCritPer() * 0.0001);
				
				fightResult = BattleConstant.FIGHT_RESULT_CRIT;
			}
			
			// 伤害加深减免
			hurt = hurt * (1 + (attacker.getDmgDeepPer() - target.getDmgReductPer())*0.0001);
			
			double random = Math.random()*0.2 + 0.9;
			damage = (int)(Math.ceil((hurt*random)));
			damage = Math.max(damage, 1);
			target.setHp(target.getHp() - damage);
		}else if(damagModel.geteEffectCate() == SkillConstant.DAMAGE_M_ATTACK){
			// 基础伤害
			double hurt = attacker.getM_attack() - target.getM_damage() + target.getM_damage() * Math.pow(Math.E, 1-Math.pow(Math.E, attacker.getM_attack() * 1.0 / target.getM_damage()));

			// 技能系数
			hurt = hurt * (damagModel.getN32EffectRate()/1000.0) + damagModel.getN32EffectAdd();
			
			// 暴击判定
			double critRandom = 0.05 + 2 * (attacker.getCrit() - target.getTough())*0.001;
			if(Math.random() < critRandom){
				hurt = hurt * (attacker.getDmgCritPer() * 0.0001);
				
				fightResult = BattleConstant.FIGHT_RESULT_CRIT;
			}
			
			// 伤害加深减免
			hurt = hurt * (1 + (attacker.getDmgDeepPer() - target.getDmgReductPer())*0.0001);
			
			double random = Math.random()*0.2 + 0.9;
			damage = (int)(Math.ceil((hurt*random)));
			damage = Math.max(damage, 1);
			target.setHp(target.getHp() - damage);
		}else if(damagModel.geteEffectCate() == SkillConstant.DAMAGE_M_ATTACK_RESUME){
			damage = (int)((attacker.getM_attack()*(damagModel.getN32EffectRate()/1000.0) + damagModel.getN32EffectAdd()));
			damage = Math.max(0, damage);
			target.setHp(target.getHp() + damage);
			damage = -damage;
		}else{
			return null;
		}
		
		int[] arr = new int[3];
		arr[0] = damage;
		arr[1] = realDmg - target.getHp();
		arr[2] = fightResult;
		return arr;
	}


	@Override
	public void changePkModel(long playerId, int pkModel) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		ISceneService sceneService = serviceCollection.getSceneService();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		PlayerPuppet playerPuppet = sceneService.getPlayerPuppet(playerId);
		if(playerPuppet == null) throw new GameException(ExceptionConstant.PLAYER_1111);
		
		if(playerPuppet.getPkMode() != pkModel){
			playerPuppet.setPkMode(pkModel);
			playerService.synPlayerPropertyToAll(playerPuppet, ProdefineConstant.PK_MODEL, pkModel);			
		
		}
		
		// 触发pk模式任务
		List<Integer> conditionList = new ArrayList<Integer>();				
		conditionList.add(pkModel);
		serviceCollection.getTaskService().executeTask(playerId, TaskConstant.TYPE_11, conditionList);
		
		// 设置模式
		PlayerExt playerExt = playerService.getPlayerExtById(playerId);
		playerExt.setPkMode(pkModel);
		playerService.updatePlayerExt(playerExt);
	}


	@Override
	public void dealPkValue(PlayerPuppet p) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		long currentTime = System.currentTimeMillis();
		
		// 断线20秒
		if(p.getLogoutTime() > 0 && currentTime - p.getLogoutTime() >= 20000){
			p.setLogoutTime(0);
			serviceCollection.getLoginService().exitGame(p.getEid());
		}
		
		// 灰名一分钟
		if(p.getGrayUpdateTime() > 0 && currentTime - p.getGrayUpdateTime() >= 60000){
			p.setGrayUpdateTime(0);
			if(p.getNameColor() < 3){
				p.setNameColor(1);
				
				playerService.synPlayerPropertyToAll(p, ProdefineConstant.NAME_COLOR, p.getNameColor());
			}
		}
		
		// 每10分钟减10点pk值
		if(p.getPkVlaue() > 0 && p.getPkValueUpdateTime() > 0 && currentTime - p.getPkValueUpdateTime() >= 600000){
			p.setPkVlaue(p.getPkVlaue() - 10);
			if(p.getPkVlaue() > 0){
				p.setPkValueUpdateTime(currentTime);
			}
			playerService.synPlayerProperty(p.getEid(), ProdefineConstant.PK_VALUE, p.getPkVlaue());
			
			if(p.getPkVlaue() < 300){
				p.setNameColor(1);
				if(p.getGrayUpdateTime() > 0){
					p.setNameColor(2);
				}
				playerService.synPlayerPropertyToAll(p, ProdefineConstant.NAME_COLOR, p.getNameColor());
			}
		}
	}

	@Override
	public void dealCollect(PlayerPuppet p, SceneModel scene, int gridId) {		
		ICollectService collectService = GameContext.getInstance().getServiceCollection().getCollectService();
		long currentTime = System.currentTimeMillis();
		
		// 玩家采集刷奖
		if(p.getPlayerCollectId() > 0 && p.getState() == BattleConstant.STATE_NORMAL){
			Collect collect = collectService.getCollect(p.getPlayerCollectId(), p, scene);			
			if(collect != null){
				BaseCollect baseCollect = collectService.getBaseCollectById(collect.getCollectId());				
				if (baseCollect.getType() == BattleConstant.COLLECT_SENIOR && collect.getCollectNum() < baseCollect.getCount()){
					if(p.getCollectRewardRefTime() == 0){						
						if(p.getCollectTime() > 0){	
							p.setCollectRewardRefTime(currentTime);							
						}
					}else{											
						if(p.getCollectRewardRefTime() > 0 && currentTime - p.getCollectRewardRefTime() >= baseCollect.getCost()){								
							p.setCollectRewardRefTime(currentTime);								
							collectService.quartzRefreshSeniorCollectAward(scene, p, collect, baseCollect);
						}
					}					
				}
				
				if((currentTime - p.getCollectTime()) > baseCollect.getCollectTime() + 200){					
					collectService.endCollect(scene, p, collect, baseCollect);
				}
				
			}
		}	
	}


	@Override
	public void dealBuff(BasePuppet basePuppet) {
		Map<Integer, Buff> buffMap = basePuppet.getBuffMap();
		if(buffMap.values() == null ||buffMap.values().isEmpty()) return;
		
		IBuffService buffService = GameContext.getInstance().getServiceCollection().getBuffService();
		buffService.dealBuff(basePuppet);
	}

	@Override
	public void dealFB(PlayerPuppet playerPuppet, SceneModel sceneModel) {
		if(sceneModel.getMapType() == SceneConstant.GUILD_SCENE){
			
			IPlayerService playerService = GameContext.getInstance().getServiceCollection().getPlayerService();
			
			long currentTime = System.currentTimeMillis();
			
			if(currentTime - playerPuppet.getGuildFightUpdateTime() >= 30000){
				playerPuppet.setGuildFightUpdateTime(currentTime);
				
				playerService.addPlayerExp(playerPuppet.getEid(), 2000 + (playerPuppet.getLevel() - 20) * 100);
			}
		}
	}
}
