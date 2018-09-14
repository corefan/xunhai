package com.action;

import com.common.GameContext;
import com.common.ServiceCollection;
import com.core.GameMessage;
import com.message.RankProto.C_GetRankList;

/**
 * 排行榜接口
 * @author ken
 * @date 2017-5-8
 */
public class RankAction {

	private ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
	
	/**
	 * 获取排行榜数据
	 */
	
	public void GetRankList(GameMessage gameMessage) throws Exception {
		long playerId = gameMessage.getConnection().getPlayerId();
		C_GetRankList param = C_GetRankList.parseFrom(gameMessage.getData());
		int type = param.getType();
		int career = param.getCareer();
		int start = param.getStart();
		int offset = param.getOffset();
		
		serviceCollection.getRankService().getRankList(playerId, type, career, start, offset);
	}

}
