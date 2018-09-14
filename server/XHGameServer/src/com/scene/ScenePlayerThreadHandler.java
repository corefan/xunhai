package com.scene;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.common.GameContext;
import com.domain.puppet.PlayerPuppet;
import com.service.IBattleService;
import com.service.IRebotService;
import com.util.LogUtil;

/**
 * 场景玩家服务线程
 * @author ken
 * @date 2017-1-9
 */
public class ScenePlayerThreadHandler implements Runnable {
	
	/** 线程下标*/
	private int index;
	
	public ScenePlayerThreadHandler() {
		
	}
	
	public ScenePlayerThreadHandler(int index) {
		this.index = index;
	}

	@Override
	public void run() {
		while (true) {
			
				try {
					List<SceneModel> sceneList = SceneServer.getInstance().getSceneModelList(index);
					if(sceneList != null && !sceneList.isEmpty()){
						
						IBattleService battleService = GameContext.getInstance().getServiceCollection().getBattleService();
						IRebotService rebotService = GameContext.getInstance().getServiceCollection().getRebotService();
						
						for(SceneModel model : sceneList){
							Map<Integer, Map<String, PlayerPuppet>> playerMap = model.getPlayerPuppetMap();
							for(Map.Entry<Integer, Map<String, PlayerPuppet>> entry : playerMap.entrySet()){
								Map<String, PlayerPuppet> map = entry.getValue();
								for(Map.Entry<String, PlayerPuppet> entry2 : map.entrySet()){
									PlayerPuppet p = entry2.getValue();
									
									//处理玩家buff
									battleService.dealBuff(p);
									
									//处理玩家pk值
									battleService.dealPkValue(p);
									
									//处理玩家高级采集
									battleService.dealCollect(p, model, entry.getKey());
									
									//处理活动副本一些特殊要求
									battleService.dealFB(p, model);
									
									//处理机器人AI
									if(p.isRebot()){
										rebotService.dealRebotAI(p, model);
									}									
								}
							}
						}
					}
					
					// 等待16毫秒
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (Exception e) {
					LogUtil.error("SceneBattleHandler: ",e);
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
