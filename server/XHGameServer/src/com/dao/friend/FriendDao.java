package com.dao.friend;

import java.util.List;

import com.db.GameSqlSessionTemplate;
import com.domain.friend.PlayerFriend;


public class FriendDao extends GameSqlSessionTemplate {		
	/**
	 * 创建玩家好友
	 * */
	public void createPlayerFriend(PlayerFriend playerFriend) {
		this.insert_noreturn(playerFriend.getInsertSql());
	}
	
	/**
	 * 获得玩家好友列表
	 * */
	public List<PlayerFriend> listPlayerFriend(long playerId) {
		String sql = "SELECT * FROM player_friend  WHERE playerId="+playerId+"  and deleteFlag = 0";
		return this.selectList(sql, PlayerFriend.class);
	}
	
	/**
	 * 定时清理无效数据
	 */
	public void quartzDelete(){
		
		this.delete("DELETE FROM player_friend WHERE deleteFlag = 1");
	}	
	
	
}
