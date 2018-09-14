package com.service;

import java.util.List;
import java.util.Map;

import com.domain.guild.Guild;
import com.domain.guild.GuildBuy;
import com.domain.guild.GuildFight;
import com.domain.guild.PlayerGuild;

/**
 * 帮派系统
 * @author ken
 * @date 2018年3月31日
 */
public interface IGuildService {

	/**
	 * 基础缓存
	 */
	void initBaseCache();
	
	/** 初始缓存*/
	void initCache();
	
	/**
	 * 删除缓存
	 */
	void deleteCache(long playerId);
	
	/**
	 * 缓存to入库
	 */
	void updateGuild(Guild guild);
	
	/**
	 * 缓存to入库
	 */
	void updatePlayerGuild(PlayerGuild playerGuild);
	
	/**
	 * 根据帮派编号找出帮派信息
	 */	
	Guild getGuildById(long guildId);
	
	/**
	 * 取玩家帮派名称
	 */
	String getGuildName(long playerId);
	
	/**
	 * 取玩家帮派信息
	 */
	PlayerGuild getPlayerGuild(long playerId);
	
	/**
	 * 获取职务名称
	 */
	String getRoleName(int roleId);
	
	/**
	 * 每分钟任务
	 */
	void minuteQuartz();
	
	/**
	 * 每小时任务
	 */
	void hourQuartz();
	
	/**
	 * 每天任务
	 */
	void dailyQuartz();
	
	/**
	 * 每周任务
	 */
	void weekQuartz();
	
	
	/**
	 * 帮派列表
	 */
	void getGuildList(long playerId) throws Exception;
	
	/**
	 * 成员列表
	 */
	void getGuildPlayerList(long playerId) throws Exception;
	
	/**
	 * 创建帮派
	 */
	void createGuild(long playerId, String guildName, String notice) throws Exception;
	
	/**
	 * 修改公告
	 */
	void modifyNotice(long playerId, String notice) throws Exception;
	
	/**
	 * 申请帮派
	 */
	void applyGuild(long playerId, long guildId) throws Exception;
	
	/**
	 * 一键申请
	 */
	List<Long> quickApply(long playerId) throws Exception;
	
	/**
	 * 申请列表
	 */
	List<Long> getApplyList(long playerId) throws Exception;
	
	/**
	 * 同意申请
	 */
	void agreeApply(long playerId, Long applyId) throws Exception;
	
	/**
	 * 拒绝申请
	 */
	void refuseApply(long playerId, long applyId) throws Exception;
	
	/**
	 * 清空申请列表
	 */
	void clearApplys(long playerId) throws Exception;
	
	/**
	 * 自动接受申请
	 */
	void autoAgreeApply(long playerId, int selected, int autoMinLv, int autoMaxLv) throws Exception;
	
	/**
	 * 邀请进入
	 */
	void inviteJoin(long playerId, long invitedId) throws Exception;
	
	/**
	 * 同意邀请
	 */
	void agreeInvite(long playerId, long guildId) throws Exception;
	
	/**
	 * 拒绝邀请
	 */
	void refuseInvite(long playerId, long guildId) throws Exception;
	
	/**
	 * 退出帮派
	 */
	void quitGuild(Long playerId) throws Exception;
	
	/**
	 * 转让帮主
	 */
	void changeGuilder(long playerId, long targetId) throws Exception;
	
	/**
	 * 踢出帮派
	 */
	void kickGuild(long playerId, Long targetId) throws Exception;
	
	/**
	 * 任免职务
	 */
	void changeGuildRole(Long playerId, Long targetId, int newRoleId) throws Exception;
	
	/**
	 * 升级帮派
	 */
	void upgradeGuild(long playerId) throws Exception;
	
	/**
	 * 获取捐献各次数
	 */
	void getDonateTimes(long playerId) throws Exception;
	
	/**
	 * 捐献
	 */
	void donate(Long playerId, int id) throws Exception;
	
	/**
	 * 获取宣战信息
	 */
	void getGuildWarList(long playerId) throws Exception;
	
	/**
	 * 发起宣战
	 */
	long guildWar(long playerId, long guildId) throws Exception;
	
	/**
	 * 是否为敌对帮派
	 */
	boolean isGuildWar(long playerId, long targetId);
	
	/**
	 * 研发帮派技能
	 */
	int upgradeGuildSkill(long playerId, int type) throws Exception;
	
	/**
	 * 学习帮派技能
	 */
	int studyGuildSkill(long playerId, int type) throws Exception;
	
	/**
	 * 获取已学习和研发的技能列表
	 */
	void getGuildSkills(long playerId)throws Exception;
	
	/*********************城战**********************/
	
	/**
	 * 获取当前城战信息
	 */
	GuildFight getGuildFightCache();
	
	/**
	 * 城战面板
	 */
	void getGuildFightData(long playerId);
	
	/**
	 * 报名攻城
	 */
	void applyGuildFight(long playerId) throws Exception;

	/**
	 * 报名攻城列表
	 */
	void getGuildFights(long playerId) throws Exception;
	
	/**
	 * 创建联盟
	 */
	void createUnion(long playerId, String unionName) throws Exception;
	
	/**
	 * 联盟列表
	 */
	void getUnions(long playerId) throws Exception;
	
	/**
	 * 申请联盟
	 */
	void applyUnion(long playerId, long unionId) throws Exception;
	
	/**
	 * 同意加入联盟
	 */
	void agreeJoinUnion(long playerId, Long guildId) throws Exception;
	
	/**
	 * 提交攻城令
	 */
	void submitItem(long playerId, int itemNum) throws Exception;
	
	/**
	 * 进入城战
	 */
	void enterGuildFight(long playerId) throws Exception;
	
	/**
	 * 进入或退出 诛仙台
	 * enter true：进入   false：退出
	 */
	void enterZhuXianTai(long playerId, int pkType, boolean enter);
	
	
	/*********************税收**********************/
	
	/**
	 * 添加今日税收额
	 */
	void addCurRevenue(int addValue);
	
	/**
	 * 获取税收数据
	 */
	void getRevenueData(long playerId);
	
	/**
	 * 领取税收
	 */
	void receiveRevenue(long playerId) throws Exception;
	
	/**
	 * 领取俸禄
	 */
	int receiveSalary(long playerId) throws Exception;
	
	/**
	 * 领取礼包
	 */
	void receiveGift(long playerId) throws Exception;
	
	/**
	 * 优惠购买记录
	 */
	Map<Integer, GuildBuy> getGuidBuyMap();
	
	/**
	 * 优惠购买
	 */
	int guildBuy(long playerId, int itemId, int itemNum) throws Exception;
	
	/**
	 * 凌烟阁  1：开始 2：进入
	 */
	void guildFB(long playerId, int type) throws Exception;
	
	/**
	 * 凌烟阁副本结束
	 */
	void endGuildFB();
	
	/*********************领地****************************/
	
	/**
	 * 获取领地面板数据
	 */
	void getManorData(long playerId) throws Exception;
	
	/**
	 * 进入帮派领地
	 */
	void guildManor(long playerId) throws Exception;
	
	/**
	 * 召唤领地boss
	 */
	void callManorBoss(long playerId) throws Exception;
	
	/**
	 * 喂养领地boss
	 */
	int feedManorBoss(long playerId, int itemNum) throws Exception;
}
