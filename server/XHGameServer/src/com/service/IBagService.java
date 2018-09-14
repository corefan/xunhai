package com.service;

import java.util.List;

import com.domain.bag.BaseItem;
import com.domain.bag.PlayerBag;
import com.domain.bag.PlayerDrug;

/**
 * 背包系统
 * @author ken
 * @date 2017-1-4
 */
public interface IBagService {

	/**
	 * 初始配置数据
	 */
	void initBaseCache();
	
	/**
	 * 根据玩家编号得到背包信息列表
	 * */
	public List<PlayerBag> getPlayerBagListByPlayerID(long playerId)throws Exception;
	
	/**
	 * 整理背包
	 * */
	public void tidyBag(long playerId)throws Exception;
	
	/**
	 * 出售背包物品
	 * */
	void sellItem(long playerId, long playerBagId)throws Exception;
	
	/**
	 *  使用背包物品
	 */
	public PlayerBag useItem(long playerId, long playerBagId, int num) throws Exception;
	
	/**
	 * 删除玩家道具
	 */
	public void removePlayerBag(PlayerBag playerBag);
	
	
	/**
	 * 取物品根据物品编号
	 */
	BaseItem getBaseItemById(Integer itemId);
	
	/**
	 * 根据玩家编号得到背包信息列表
	 * */
	List<PlayerBag> getAllPlayerBagListByPlayerID(long playerId);
	
	/**
	 * 根据背包物品编号得到背包物品
	 * @param playerID 
	 * */
	PlayerBag getPlayerBagById(long playerId, long playerBagId);
	
	/**
	 * 获取背包装备数据
	 */
	PlayerBag getPlayerBagForEquipment(long playerId, long playerEquipmentId);
	
	/**
	 * 创建背包物品
	 * */
	PlayerBag createPlayerBag(long playerId, long itemId, int goodsType, int isBinding, int num, Integer itemIndex);
	

	/**
	 * 根据玩家编号得到新物品在背包的格子索引
	 * 新物品 注：任何装备，背包里没有的道具
	 * */
	Integer getNewItemIndexByPlayerId(long playerId);
	
	/**
	 * 获取未用的格子数
	 */
	int getFreeGridNumByPlayerID(long playerId);
	
	/**
	 * 添加玩家道具(带验证)
	 * */
	List<PlayerBag> addPlayerBag_check(long playerId, BaseItem item, int num, int isBinding)throws Exception;
	
	/**
	 * 添加玩家道具(不带验证,背包满发邮件)
	 * */
	List<PlayerBag> addPlayerBag_nocheck(long playerId, Integer itemId, int num, int isBinding) throws Exception;
	
	/**
	 * 扣除背包道具 IRewardService应用
	 * @param isBinding 是否扣除绑定物品 ture:是 false:否
	 * */
	List<PlayerBag> deductItem(long playerId, Integer itemId, int num, boolean isBinding)throws Exception;
	
	/**
	 * 扣除指定背包道具应用
	 * @param 
	 * */
	PlayerBag deductItemByPlayerBagId(long playerId, long playerBagId, int num)throws Exception;
	
	/**
	 * 扣除背包道具 IRewardService应用
	 * */
	
	List<PlayerBag> deductItemList(long playerId, List<List<Integer>> items)throws Exception;
	
	/** 更新格子数*/
	PlayerBag updateNumByPlayerBagId(long playerId, PlayerBag playerBag, int num)throws Exception;
	
	/**
	 * 更新物品缓存
	 */
	void updatePlayerBag(PlayerBag playerBag);
	
	/**
	 * 删除玩家所有背包物品(缓存)
	 * */
	void deleteCache(long playerId);
	
	
	/***************药品栏*********************/
	List<PlayerDrug> listPlayerDrugs(long playerId);
	
	/**
	 * 装备药品
	 */
	PlayerDrug putonDrug(long playerId, int type, int itemId)throws Exception;
	
	/**
	 * 卸下药品
	 */
	PlayerDrug putdownDrug(long playerId, int type, int itemId)throws Exception;

	/**
	 * 加MP
	 */
	int useAddMpItem(long playerId, int addValue);

	/**
	 * 加HP
	 */
	int useAddHpItem(long playerId, int addValue);
	
	/**
	 *  获取绑定或者不绑的数量
	 */
	Integer getItemNumByIsBinding(long playerId, Integer itemId, int isBinding);
	
	/**
	 *  根据物品id获取物品的数量
	 */
	int getItemNumByPlayerIdAndItemId(long playerId, Integer itemId);
	
}
