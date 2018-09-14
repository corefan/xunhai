package com.service;

import java.util.List;
import java.util.Map;

import com.domain.player.PlayerExt;
import com.domain.puppet.PlayerPuppet;
import com.domain.team.Team;


/**
 * 组队系统
 * @author ken
 * @date 2017-3-1
 */
public interface ITeamService {

	/**
	 * 初始配置表
	 */
	void initBaseCache();
	
	/**
	 * 初始缓存
	 */
	void initCache();
	
	/**
	 * 队伍
	 */
	Map<Integer, Team> getTeamMap();
	
	/**
	 * 组队大厅
	 */
	void getTeamList(long playerId, int activityId);
	
	/**
	 * 取队伍数据 
	 */
	Team getTeam(int teamId);
	
	/**
	 * 创建队伍
	 */
	void createTeam(long playerId) throws Exception;
	
	/**
	 * 选择活动目标
	 */
	void changeTarget(long playerId, int activityId, int minLevel) throws Exception;
	
	/**
	 * 获取社交邀请列表
	 */
	void getInviteList(long playerId, int type, int start, int offset) throws Exception;
	
	/**
	 * 邀请
	 */
	void invite(long playerId, long inviterId) throws Exception;
	
	/**
	 * 同意邀请 
	 */
	void agreeInvite(long playerId, int teamId) throws Exception;
	
	/**
	 * 清空队伍申请列表
	 */
	void clearTeamApplyList(long playerId) throws Exception;
	
	/**
	 * 退出队伍
	 */
	void quitTeam(long playerId)  throws Exception;
	
	/**
	 * 踢队员
	 */
	void kickTeamPlayer(long playerId, long teamPlayerId) throws Exception;
	
	/**
	 * 
	 * 转让队长
	 */
	void changeCaptain(long playerId, long teamPlayerId) throws Exception; 
	
	/**
	 * 在线队员列表
	 */
	List<Long> getOnlineTeamPlayerIds(Team team);
	
	/**
	 * 同步等级
	 */
	void synUpLevel(PlayerExt playerExt);
	
	/**
	 * 同步血条
	 */
	void synHp(PlayerPuppet playerPuppet);
	
	/**
	 * 处理上线
	 */
	void dealLogin(PlayerExt playerExt);
	
	/**
	 * 处理下线
	 */
	void dealExit(PlayerExt playerExt);
	
	/**
	 * 申请加入队伍
	 */
	void applyJoinTeam(Long playerId, int teamId) throws Exception; 
	
	
	/**
	 * 获取申请加入队伍消息
	 */
	void getTeamApplyList(long playerId) throws Exception; 
	
	/**
	 * 加入队伍信息处理
	 */
	void applyJoinTeamDeal(long playerId, Long applyPlayerId, int state) throws Exception; 
	
	/**
	 * 玩家自动匹配队伍
	 */
	void playerAutoMatch(long playerId, int activityId) throws Exception; 
	
	/**
	 * 队伍自动匹配玩家
	 */
	void teamAutoMatch(long playerId, int autoMatch) throws Exception; 
	
	/**
	 * 跟随
	 */
	void follow(long playerId, int state) throws Exception;	
	
	/**
	 * 自动同意加入申请
	 */
	void autoAgreeApply(long playerId) throws Exception; 
	
	/**
	 * 获取队长位置信息
	 */
	void getCaptainPostion(long playerId) throws Exception;
	
	/**
	 * 同步队伍信息
	 */
	void synTeam(List<Long> playerIds, Team team);
	
	/**
	 * 同步队伍信息
	 */
	void synTeam(long playerId);
	
	/**
	 * 判断是否触发家族buff
	 */
	void checkFamilyBuff(long playerId, int teamId);
}
                               