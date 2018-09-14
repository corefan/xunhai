package com.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;

import com.core.Connection;
import com.domain.player.Player;
import com.domain.player.PlayerDaily;
import com.domain.player.PlayerExt;
import com.domain.player.PlayerOptional;
import com.domain.player.PlayerProperty;
import com.domain.player.PlayerWealth;
import com.domain.puppet.BasePuppet;


/**
 * 玩家系统
 * @author ken
 * @date 2016-12-24
 */
public interface IPlayerService {

	/** 初始缓存*/
	void initCache();
	
	/** 清理缓存*/
	void deleteCache(long playerId);
	
	/**
	 * 活跃玩家编号缓存
	 */
	Set<Long> getPlayerIDCache();
	
	/**
	 * 获取账号角色缓存
	 */
	List<Player> listPlayerByUserId(long userId);
	
	/**
	 * 创建角色入库
	 */
	Player createPlayer_sp(long userId, String site, int serverNo, String playerName, int career, long telePhone) throws Exception;
	
	
	/**
	 * 根据玩家编号去玩家数据
	 */
	Player getPlayerByID(long playerId);
	
	/**
	 * 根据玩家名字去找玩家数据
	 */
	Player getPlayerByName(String playerName);
	
	/**
	 * 根据玩家编号取扩展信息
	 */
	PlayerExt getPlayerExtById(long playerId);
	
	/**
	 * 更新玩家扩展
	 * */
	void updatePlayerExt(PlayerExt playerExt);
	
	/**
	 * 根据玩家编号取属性信息
	 */
	PlayerProperty getPlayerPropertyById(long playerId);
	
	/**
	 * 更新玩家属性
	 */
	void updatePlayerProperty(PlayerProperty playerProperty);

	/**
	 * 玩家每日数据
	 */
	PlayerDaily getPlayerDailyById(long playerId);
	
	/**
	 * 更新玩家每日数据
	 */
	void updatePlayerDaily(PlayerDaily playerDaily);
	
	/**
	 * 更新玩家数据
	 */
	void updatePlayer(Player player);
	
	/**
	 * 获取玩家财富
	 */
	PlayerWealth getPlayerWealthById(long playerId);
	
	/**
	 * 更新玩家财富
	 */
	void updatePlayerWealth(PlayerWealth playerWealth);	
	
	/**
	 * 根据玩家编号取操作信息
	 */	
	PlayerOptional getPlayerOptionalById(long playerId);
	
	/**
	 * 更新玩家操作数据
	 */
	void updatePlayerOptional(PlayerOptional playerOptional);
	
	/**
	 * 起服加载玩家数据
	 */
	void loadPlayerData();
	
	/**
	 * 下线掉线
	 */
	void exit(Connection connection) throws Exception;
	
	/**
	 * 游戏中下线数据处理
	 */
	void dealExitData(long playerId);
	
	/**
	 * 关服处理在线玩家下线
	 */
	void closeServerDealExit(List<Long> playerIDList);
	
	/**
	 * 处理每日信息
	 */
	void handlePlayerInfoForDaily() throws JSONException;
	
	/**
	 * 周结处理
	 */
	void handlePlayerInfoForWeek();
	
	/**
	 * 加减金币
	 */
	int addGold_syn(long playerId, int addMoney);
	
	/**
	 * 加减钻石
	 */
	int addDiamond_syn(long playerId, int addDiamond, String costName);
	
	/**
	 * 加减宝玉
	 */
	int addStone_syn(long playerId, int addStone);
	
	/**
	 * 加玩家经验
	 */
	void addPlayerExp(long playerId, int addExp);
	
	/**
	 * 同步玩家单一属性
	 */
	void synPlayerProperty(long playerId, int propertyId, int propertyValue);
	
	/**
	 * 同步玩家多条属性
	 */
	void synPlayerProperty(long playerId, Map<Integer, Integer> propertyMap);
	
	/**
	 * 同步玩家单一属性给全场人
	 */
	void synPlayerPropertyToAll(BasePuppet basePuppet, int propertyId, int propertyValue);
	
	/**
	 * 同步玩家多条属性给全场人
	 */
	void synPlayerPropertyToAll(BasePuppet basePuppet, Map<Integer, Integer> propertyMap);
	
	/**
	 * 同步玩家多条属性给全场人
	 */
	int difTime(Date loginTime);
	
	/**
	 * 删除角色
	 */
	void deletePlayer(long playerId) throws Exception;

	/**
	 * 角色展示信息
	 */
	void getShowPlayer(long playerId, long showPlayerId);
	
	/**
	 * 玩家信息提示框
	 */
	void quickTips(long playerId, long tipPlayerId) throws Exception;	
	
	/**
	 * 获取当前服创过角用户数， 总创角人数
	 */
	int[] getCreateNum(String gameSite);
	
	/**
	 * 各在线时长人数
	 */
	Map<String, Object> getOnlineTimeNum(String gameSite, String date);
	
	/**
	 * 计算购买基金人数(每小时)
	 */
	void calBuyGrowthFundNum();
	
	/**
	 * 获取购买基金人数总和
	 */
	int getBuyGrowthFundNum();
	
	/**
	 * 头顶称谓同步
	 */
	void synPlayerTitle(long playerId, int type, int sortId, String title );
}
