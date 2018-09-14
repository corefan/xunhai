package com.scene;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.common.GameContext;
import com.common.GameSocketService;
import com.common.ServiceCollection;
import com.domain.puppet.PlayerPuppet;

/**
 * 场景线程服务
 * @author ken
 * @date 2017-1-9
 */
public class SceneServer {

	private Map<Integer, List<SceneModel>> sceneModelMap = new ConcurrentHashMap<Integer, List<SceneModel>>();
	
	/** 单位(秒) */
	private static final long keepAliveTime = 3600L;

	/** 线程数    可优化成配置 TODO */
	private static final int treadNum = 1;
	
	private SceneServer() {}

	public static SceneServer getInstance() {
		return SingletonHolder.instance;
	}

	public synchronized void start() {
		
		for(int index = 1; index <= treadNum; index++){
			
			sceneModelMap.put(index, new CopyOnWriteArrayList<SceneModel>());
			
			ThreadPoolExecutor battleThreadPool = new ThreadPoolExecutor(1, 1, keepAliveTime,
					TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new PriorityThreadFactory(
							"Game-ScenePlayerThread-"+index, "ScenePlayerThread", Thread.NORM_PRIORITY));
			battleThreadPool.execute(new ScenePlayerThreadHandler(index));
			
			ThreadPoolExecutor refreshMonster = new ThreadPoolExecutor(1, 1, keepAliveTime,
					TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new PriorityThreadFactory(
							"Game-SceneThread-"+index, "SceneThread", Thread.NORM_PRIORITY));
			refreshMonster.execute(new SceneThreadHandler(index));
			
			ThreadPoolExecutor aiThread = new ThreadPoolExecutor(1, 1, keepAliveTime,
					TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new PriorityThreadFactory(
							"Game-SceneAI-"+index, "SceneAI", Thread.NORM_PRIORITY));
			aiThread.execute(new SceneAiHandler(index));
		}
		
		
	}
	
	/**
	 * 获取副本列表
	 */
	public List<SceneModel> getSceneModelList(int index){
		
		return sceneModelMap.get(index);
	}
	
	/**
	 * 添加进副本列表
	 */
	public void addSceneModel(SceneModel sceneModel){
		int index = sceneModel.getSceneId() % treadNum + 1;

		sceneModelMap.get(index).add(sceneModel);
		
		//TODO 
		this.printSceneNum();
	}
	
	/**
	 * 从副本列表移除
	 */
	public void removeSceneModel(SceneModel sceneModel){
		int index = sceneModel.getSceneId() % treadNum + 1;

		sceneModelMap.get(index).remove(sceneModel);
	}
	
	/** 清理场景玩家数据补丁*/
	public void removePlayer(Long playerId, String guid){
		boolean bFind = false;
		for(Map.Entry<Integer, List<SceneModel>> entry : sceneModelMap.entrySet()){
			 List<SceneModel> lists = entry.getValue();
			 if(lists != null){
				 for(SceneModel model : lists){
					 for(Map.Entry<Integer, Map<String, PlayerPuppet>> entry2 : model.getPlayerPuppetMap().entrySet()){
						 Map<String, PlayerPuppet> map = entry2.getValue();
						 if(map.containsKey(guid)){
							 entry2.getValue().remove(guid);
							 bFind = true;
							 
							 break;
						 }
					 }
					 
					 for(Map.Entry<Integer, List<Long>> entry3 : model.getPlayerIdMap().entrySet()){
						 List<Long> ids = entry3.getValue();
						 if(ids.contains(playerId)){
							 ids.remove(playerId);
							 bFind = true;
							 
							 break;
						 }
					 }
					 
					 if(bFind){
						 break;
					 }
				 }
				 if(bFind){
					 break;
				 }
			 }
		}
	}
	private void printSceneNum(){
		int num = 0;
		for(Map.Entry<Integer, List<SceneModel>> entry : sceneModelMap.entrySet()){
			 List<SceneModel> lists = entry.getValue();
			 if(lists != null){
				 num += lists.size();
			 }
		}
		System.out.println("当前场景数量："+num);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		
		System.out.println("当前在线人数："+gameSocketService.getOnLinePlayerIDList().size());
	}
	
	/**
	 * 线程工厂
	 * @author ken
	 * @date 2017-1-9
	 */
	private static final class PriorityThreadFactory implements ThreadFactory {
		private int _prio;
		private String _name;
		private AtomicInteger _threadNumber = new AtomicInteger(1);
		private ThreadGroup _group;

		public PriorityThreadFactory(String name, String groupName, int prio) {
			_prio = prio;
			_name = name;
			_group = new ThreadGroup(groupName);
		}

		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(_group, r);
			t.setName(_name + "-" + _threadNumber.getAndIncrement());
			t.setPriority(_prio);
			t.setDaemon(true);
			return t;
		}
	}

	private static final class SingletonHolder {
		private static final SceneServer instance = new SceneServer();

	}
}
