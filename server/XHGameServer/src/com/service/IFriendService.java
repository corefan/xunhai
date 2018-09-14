package com.service;

import java.util.List;

import com.domain.friend.PlayerApply;
import com.domain.friend.PlayerFriend;
import com.domain.player.Player;



/**
 * 好友系统
 * @author jiangqin
 * @date 2017-2-16
 */
public interface IFriendService {

	/**
	 * 清缓存
	 */
	void deleteCache(long playerId);

	/**
	 * 玩家消息列表
	 */
	List<PlayerApply> listPlayerApplys(long playerId);	
	
	/** 申请添加好友 
	 * @return */
	PlayerApply addPlayerFriend(long playerId, long applyPlayerId)throws Exception;
	
	/**
	 * 申请消息处理 
	 */
	void applyDeal(long playerId, long applyPlayerId, int state)throws Exception;

	/**
	 * 玩家好友信息列表(包含详细数据)
	 * @param type 
	 */
	List<Long> getFriendList(long playerId, int type);	

	/**
	 * 删除好友
	 * @param deletePlayerId 
	 */
	void deleteFriend(long playerId, long deletePlayerId);
	
	/**
	 * 搜索好友
	 * @param deletePlayerId 
	 */
	Player searchFriend(String playerName);
	
	/**
	 * 清空申请列表
	 */
	void deleteAllApply (long playerId);	
	
	/**
	 * 同意全部申请信息
	 */
	void agreeAllApply (long playerId);	
			
	/**
	 * 调度清理邮件
	 */
	void quartzDeleteFriends();
	
	/**
	 * 判断是否已是好友
	 */
	public Boolean isFriend(long playerId, long applyPlayerId);
	
	/**
	 * 获取玩家好友
	 */
	public List<PlayerFriend> listPlayerFriend(long playerId);
	
	/**
	 * 自动添加好友
	 */
	public void autoAddFriend(long playerId, List<Long> playerIds);
	
	/**
	 * 设置是否接受好友申请
	 */
	public void setIsAcceptApply(long playerId, int state);
}
