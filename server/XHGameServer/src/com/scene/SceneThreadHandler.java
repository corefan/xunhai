package com.scene;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import com.common.GameContext;
import com.common.ServiceCollection;
import com.constant.BattleConstant;
import com.constant.RewardTypeConstant;
import com.constant.SceneConstant;
import com.domain.battle.DropItemInfo;
import com.domain.battle.WigSkillInfo;
import com.domain.collect.Collect;
import com.domain.monster.RefreshMonsterInfo;
import com.domain.puppet.MonsterPuppet;
import com.domain.tianti.BaseDropItem;
import com.service.ITiantiService;
import com.util.LogUtil;

/**
 * 场景服务线程
 * @author ken
 * @date 2017-1-9
 */
public class SceneThreadHandler implements Runnable {

	/** 线程下标*/
	private int index;
	
	public SceneThreadHandler() {
		
	}

	public SceneThreadHandler(int index) {
		this.index = index;
	}

	@Override
	public void run() {
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		ITiantiService tiantiService = serviceCollection.getTiantiService();
		
		while (true) {
			
				try {
					long currentTime = System.currentTimeMillis();
					
					List<SceneModel> sceneList = SceneServer.getInstance().getSceneModelList(index);
					if(sceneList != null && !sceneList.isEmpty()){
						for(SceneModel scene : sceneList){
							if(scene.getSceneState() == SceneConstant.SCENE_STATE_COMMON){
								//刷怪
								Map<Integer, RefreshMonsterInfo> map = scene.getRefMonsterMap();
								for(Map.Entry<Integer, RefreshMonsterInfo> entry : map.entrySet()){
									RefreshMonsterInfo model = entry.getValue();
									if(model.getRefreshDate() > 0 && currentTime >= model.getRefreshDate()){
										model.setRefreshDate(0);
										model.setCurNum(0);
										serviceCollection.getMonsterService().refreshMonsters(scene, entry.getKey(), model.getCurLayerId(), true);
									}
								}
								while (!scene.getDeadList().isEmpty()) {
									MonsterPuppet m = scene.getDeadList().peek();
									if (m != null) {
										if (currentTime - m.getDeadTime() >= m.getRefreshTime()) {
											serviceCollection.getMonsterService().resetMonsterPuppet(scene, m);
											scene.getDeadList().poll();
										} else {
											break;
										}
									} else {
										break;
									}
								}
								
								// 刷竞技场掉落		
								if(scene.getMapType() == SceneConstant.TIANTI_SCENE){
									
									if(scene.getBaseDropItems() != null){									
										
										// 计算场景已存在时间
										int haveTime =  (int)(scene.getLifeTime() - (scene.getEndTime() - currentTime))/1000;										
										
										// 首次掉落																		
										if(haveTime >= 30){										
											int spaceTime = (int)((currentTime - scene.getDropItemTime())/1000);
											if(scene.getDropItemTime() < 1 || spaceTime > 30){
												BaseDropItem baseDropItem = scene.getBaseDropItems().get(scene.getIndex());
												
												// 产生掉落列表
												tiantiService.refreshDropItem(baseDropItem, scene);	
												scene.setDropItemTime(currentTime);
												
												scene.setIndex(scene.getIndex() + 1);
											}											
										}	
										
										// 添加固定buff
										int buffId = 0;
										if(haveTime >=  SceneConstant.ADD_PK_BUFF_SCE_1 && haveTime < SceneConstant.ADD_PK_BUFF_SCE_2){
											buffId = SceneConstant.PK_BUFF_1;
										}else if(haveTime >= SceneConstant.ADD_PK_BUFF_SCE_2 && haveTime < SceneConstant.ADD_PK_BUFF_SCE_3){
											buffId = SceneConstant.PK_BUFF_2;
										}else if(haveTime >= SceneConstant.ADD_PK_BUFF_SCE_3){
											buffId =  SceneConstant.PK_BUFF_3;
										}
										
										if(buffId > 0 && !scene.getPkBuff().contains(buffId)){
											tiantiService.addPKbuff(scene, buffId);
										}
									}																						
								}								
								
								//掉落过期重刷检测
								for(Map.Entry<Integer, Map<Integer,DropItemInfo>> entry : scene.getDropItemMap().entrySet()){
									scene.getRemoveDrops().clear();									
									Iterator<Map.Entry<Integer, DropItemInfo>> iterator = entry.getValue().entrySet().iterator();
									
									while(iterator.hasNext()){
										Map.Entry<Integer, DropItemInfo> infoMap = iterator.next();
										DropItemInfo info = infoMap.getValue();
										if(info.getGoodsType() != RewardTypeConstant.BOX){
											long calTime = currentTime - info.getDropTime();
											if(info.getState() == BattleConstant.DROP_NORMAL && calTime >= 20000){
												info.setBelongPlayerIds(null);
												if(calTime >= 60000){
													info.setState(BattleConstant.DROP_TIMEOUT);
													scene.getRemoveDrops().add(infoMap.getKey());
													iterator.remove();
													
													if(info.getPlayerEquipmentId() > 0){
														serviceCollection.getEquipmentService().deleteDropEquipment(info.getPlayerEquipmentId());
													}
												}
											}
										}										
									}
									
									if(!scene.getRemoveDrops().isEmpty()){
										serviceCollection.getBattleService().removeDropItems(scene.getRemoveDrops(), serviceCollection.getSceneService().getNearbyPlayerIdsByGridId(scene, entry.getKey()));
									}								
								}
																
								for(Map.Entry<Integer, Map<Integer, Collect>> entry : scene.getCollectMap().entrySet()){
									// 高级采集信息 
									scene.getCollectList().clear();								
									for(Map.Entry<Integer, Collect> entry1 : entry.getValue().entrySet()){
										Collect collect = entry1.getValue();
										if(collect.getRefreshDate() > 0 && currentTime >= collect.getRefreshDate()){										
											collect.setRefreshDate(0);
											collect.setCollectNum(0);
											collect.getPlayerIds().clear();									
											collect.setState(BattleConstant.COLLECT_NORMAL);
											scene.getCollectList().add(collect);										
										}
									}	
									
									//同步场景采集信息
									if (!scene.getCollectList().isEmpty()){
										serviceCollection.getCollectService().offerAddCollect(scene.getCollectList(), serviceCollection.getSceneService().getNearbyPlayerIdsByGridId(scene, entry.getKey()));
									}	
								}	
								
							}	
							
						    //地效技能
							Map<Integer, BlockingQueue<WigSkillInfo>> wigMap = scene.getWigSkillMap();
							for(Map.Entry<Integer, BlockingQueue<WigSkillInfo>> entry : wigMap.entrySet()){
								BlockingQueue<WigSkillInfo> wigInfos = entry.getValue();
								if (wigInfos != null && !wigInfos.isEmpty()) {
									while (!wigInfos.isEmpty()) {
										WigSkillInfo info = wigInfos.peek();
										if (info != null) {
											if (currentTime - info.getEndTime() >= -100) {
												info.setDeleteFlag(true);
												wigInfos.poll();
											} else {
												break;
											}
										} else {
											break;
										}
									}
								}
							}
							
							//副本到时间
							if(scene.getMapType() == SceneConstant.INSTANCE_SCENE){
								if(scene.getSceneState() == SceneConstant.SCENE_STATE_COMMON){
									if(scene.getEndTime() > 0 && currentTime >= scene.getEndTime()){
										//结束
										serviceCollection.getInstanceService().end(scene, 2);
									}
								}else if(scene.getSceneState() == SceneConstant.SCENE_STATE_END){
									if(scene.getWaitingTime() > 0 && currentTime - scene.getEndTime() >= scene.getWaitingTime()){
										//摧毁
										serviceCollection.getSceneService().destroy(scene);
									}
								}
							}else if(scene.getMapType() == SceneConstant.TIANTI_SCENE){
								if(scene.getSceneState() == SceneConstant.SCENE_STATE_COMMON){
									if(scene.getEndTime() > 0 && currentTime >= scene.getEndTime()){
										//结束
										serviceCollection.getTiantiService().end(scene, 0, 0);
									}
								}else if(scene.getSceneState() == SceneConstant.SCENE_STATE_END){
									if(scene.getWaitingTime() > 0 && currentTime - scene.getEndTime() >= scene.getWaitingTime()){
										//摧毁
										serviceCollection.getSceneService().destroy(scene);
									}
								}
							}
						}
					}					
					
					// 等待500毫秒
					TimeUnit.MILLISECONDS.sleep(500);
				} catch (Exception e) {
					LogUtil.error("SceneRefreshMonsHandler: ",e);
				}
		}
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
