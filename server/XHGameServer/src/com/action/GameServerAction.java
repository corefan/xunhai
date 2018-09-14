package com.action;

import com.cache.CacheService;
import com.common.GameContext;
import com.common.GameSocketService;
import com.common.ServiceCollection;
import com.core.GameMessage;
import com.domain.MessageObj;
import com.message.CommonProto.S_GetServerTime;
import com.message.MessageProto.MessageEnum.MessageID;
import com.util.LogUtil;


/**
 * 游戏服务
 * @author ken
 * @date 2016-12-20
 */
public class GameServerAction {

	private ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
	private GameSocketService gameSocketService = serviceCollection.getGameSocketService();
	
	/**
	 * 心跳
	 */
	
	public void getServerTime(GameMessage gameMessage) {
		long userId  = gameMessage.getConnection().getUserId();
		if(userId <= 0) return;
		
		long curTime = System.currentTimeMillis();
		
		Long time = CacheService.getTimeCacheMap().get(userId);
		
		Integer errorCount = CacheService.getTimeErrorMap().get(userId);
		
		if(time != null){
			//System.out.println("playerId="+playerId+", time="+(curTime - time) /1000 );
			//TODO 变速齿轮    踢下线
			if(curTime - time < 3000){
				try {
					
					if(errorCount == null){
						errorCount = 0;
					}
					errorCount++;
					
					if(errorCount >= 5){
						gameMessage.getConnection().destroy();
						
						CacheService.getTimeCacheMap().remove(userId);
						CacheService.getTimeErrorMap().remove(userId);
						return;
					}
					
					CacheService.getTimeErrorMap().put(userId, errorCount);
				} catch (Exception e) {
					LogUtil.error("心跳异常：", e);
				}
			}else{
				CacheService.getTimeErrorMap().remove(userId);
			}
		}
		CacheService.getTimeCacheMap().put(userId, curTime);
		
		S_GetServerTime.Builder builder = S_GetServerTime.newBuilder();
		builder.setServerTime(curTime);

		gameSocketService.sendData(gameMessage.getConnection(), new MessageObj(MessageID.S_GetServerTime_VALUE, builder.build().toByteArray()));		
	}
	
}
