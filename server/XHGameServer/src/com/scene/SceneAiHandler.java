package com.scene;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.common.GameContext;
import com.constant.SceneConstant;
import com.domain.puppet.BeckonPuppet;
import com.domain.puppet.MonsterPuppet;
import com.util.LogUtil;

/**
 * 场景ai服务线程
 * @author ken
 * @date 2017-1-9
 */
public class SceneAiHandler implements Runnable {

	/**
	 * 线程下标
	 */
	private int index;
	
	
	public SceneAiHandler() {
		
	}

	public SceneAiHandler(int index) {
		this.index = index;
	}

	/**
	 * 执行怪物ai 可能线路问题可以优化
	 */
	@Override
	public void run() {
		while (true) {
			
				try {
					long currentTime = System.currentTimeMillis();
					
					List<SceneModel> sceneList = SceneServer.getInstance().getSceneModelList(index);
					if(sceneList != null && !sceneList.isEmpty()){
						for(SceneModel model : sceneList){
							if(model.getSceneState() == SceneConstant.SCENE_STATE_COMMON){
								
								if(!model.isExecAIFlag()){
									continue;
								}
								
								for(Map.Entry<Integer, Map<String, MonsterPuppet>> entry : model.getMonsterPuppetMap().entrySet()){
									Map<String, MonsterPuppet> monMap = entry.getValue();
									
									for(Map.Entry<String, MonsterPuppet> entry2 : monMap.entrySet()){
										MonsterPuppet m = entry2.getValue();
										
										if(currentTime - m.getPreAiTime() < 2000) continue;
										
										//处理怪物buff
										GameContext.getInstance().getServiceCollection().getBattleService().dealBuff(m);
										
										//处理怪物ai
										GameContext.getInstance().getServiceCollection().getMonsterService().dealAI(m, model);
									}
								}
								
								// 处理召唤怪buff								
								for(Map.Entry<Integer, Map<String, BeckonPuppet>> entry : model.getBeckonPuppetMap().entrySet()){
									Map<String, BeckonPuppet> monMap = entry.getValue();
									
									for(Map.Entry<String, BeckonPuppet> entry3 : monMap.entrySet()){
										BeckonPuppet m = entry3.getValue();
										
										//召唤怪buff	
										GameContext.getInstance().getServiceCollection().getBattleService().dealBuff(m);
									}
								}
							}
						}
					}

					
					// 等待61毫秒
					TimeUnit.MILLISECONDS.sleep(61);
				} catch (Exception e) {
					LogUtil.error("SceneAiHandler: ",e);
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
