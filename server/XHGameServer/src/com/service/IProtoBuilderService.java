package com.service;

import java.util.List;

import com.domain.Position;
import com.domain.Reward;
import com.domain.bag.PlayerBag;
import com.domain.bag.PlayerDrug;
import com.domain.bag.PlayerEquipment;
import com.domain.battle.DropItemInfo;
import com.domain.battle.SkillEffect;
import com.domain.battle.WigSkillInfo;
import com.domain.buff.Buff;
import com.domain.collect.Collect;
import com.domain.enemy.PlayerEnemy;
import com.domain.epigraph.PlayerWeaponEffect;
import com.domain.family.PlayerFamily;
import com.domain.fashion.PlayerFashion;
import com.domain.friend.PlayerApply;
import com.domain.furnace.PlayerFurnace;
import com.domain.guild.Guild;
import com.domain.guild.PlayerGuild;
import com.domain.mail.MailInbox;
import com.domain.market.PlayerMarket;
import com.domain.player.Player;
import com.domain.player.PlayerDaily;
import com.domain.player.PlayerExt;
import com.domain.player.PlayerProperty;
import com.domain.player.PlayerWealth;
import com.domain.puppet.BeckonPuppet;
import com.domain.puppet.MonsterPuppet;
import com.domain.puppet.PlayerPuppet;
import com.domain.rank.BattleValueRank;
import com.domain.rank.EquipRank;
import com.domain.rank.GoldRank;
import com.domain.sign.PlayerSign;
import com.domain.skill.PlayerSkill;
import com.domain.task.PlayerTask;
import com.domain.trading.PlayerTradeBag;
import com.domain.wakan.PlayerWakan;
import com.domain.wing.PlayerWing;
import com.message.ActivityProto.RewardDataMsg;
import com.message.ActivityProto.RewardMsg;
import com.message.ActivityProto.TurnRecMsg;
import com.message.BagProto.DrugLumnMsg;
import com.message.BagProto.PlayerBagMsg;
import com.message.BattleProto.SkillEffectMsg;
import com.message.BuffProto.BuffMsg;
import com.message.EnemyProto.EnemyMsg;
import com.message.EquipmentProto.PlayerEquipmentMsg;
import com.message.EquipmentProto.PlayerWeaponEffectMsg;
import com.message.FamilyProto.FamilyPlayerMsg;
import com.message.FashionProto.PlayerFashionMsg;
import com.message.FriendProto.ApplyMsg;
import com.message.FriendProto.FriendMsg;
import com.message.FurnaceProto.PlayerFurnaceMsg;
import com.message.GuildProto.ApplyPlayerMsg;
import com.message.GuildProto.GuildMsg;
import com.message.GuildProto.GuildPlayerMsg;
import com.message.GuildProto.GuildWarMsg;
import com.message.MailProto.MailInboxMsg;
import com.message.MarketProto.MarketItemMsg;
import com.message.PlayerProto.PlayerCommonMsg;
import com.message.PlayerProto.PlayerMsg;
import com.message.PlayerProto.SynPlayerPropertyMsg;
import com.message.RankProto.BattleValueRankMsg;
import com.message.RankProto.EquipRankMsg;
import com.message.RankProto.GoldRankMsg;
import com.message.SceneProto.BeckonPuppetMsg;
import com.message.SceneProto.CollectItemInfoMsg;
import com.message.SceneProto.DropItemInfoMsg;
import com.message.SceneProto.MonsterPuppetMsg;
import com.message.SceneProto.MonsterStateMsg;
import com.message.SceneProto.PlayerPuppetMsg;
import com.message.SceneProto.Vector3Msg;
import com.message.SceneProto.WigSkillInfoMsg;
import com.message.SignProto.SignMsg;
import com.message.SkillProto.PlayerSkillMsg;
import com.message.TaskProto.PlayerTaskMsg;
import com.message.TiantiProto.EndPKPlayerMsg;
import com.message.TiantiProto.LoadPkPlayerMsg;
import com.message.TiantiProto.PKRewardMsg;
import com.message.TradingProto.PlayerTradeBagMsg;
import com.message.TradingProto.PlayerTradeEquipmentMsg;
import com.message.WakanProto.WakanMsg;
import com.message.WingProto.WingMsg;

/**
 * proto生产
 * @author ken
 * @date 2016-12-24
 */
public interface IProtoBuilderService {

	/**
	 * 登录界面玩家信息
	 */
	PlayerMsg.Builder buildPlayerMsg(Player player);
	
	/**
	 * 玩家一般信息
	 * */
	PlayerCommonMsg.Builder buildPlayerCommonMsg(Player player, PlayerProperty playerProperty, 
			PlayerExt playerExt, PlayerWealth playerWealth,PlayerDaily playerDaily);
	
	/**
	 * 场景玩家信息
	 */
	PlayerPuppetMsg.Builder buildPlayerPuppetMsg(PlayerPuppet model);
	
	/**
	 * 场景怪物信息
	 */
	MonsterPuppetMsg.Builder buildMonsterPuppetMsg(MonsterPuppet model);
	
	/**
	 * 场景召唤怪物信息
	 */
	BeckonPuppetMsg.Builder buildBeckonPuppetMsg(BeckonPuppet model);
	
	/**
	 * 掉落信息
	 */
	DropItemInfoMsg.Builder buildDropItemInfoMsg(DropItemInfo model);
	
	/**
	 * 地效持续技能
	 */
	WigSkillInfoMsg.Builder buildWigSkillInfoMsg(WigSkillInfo model);
	
	/**
	 * 位置信息
	 */
	Vector3Msg.Builder buildVector3Msg(Position model);
	
	/**
	 * 位置信息
	 */
	Vector3Msg.Builder buildVector3Msg(int x, int y, int z);
	
	/**
	 * 同步玩家属性
	 */
	List<SynPlayerPropertyMsg.Builder> buildSynPlayerPropertyMsgList(PlayerProperty playerProperty, PlayerExt playerExt);
	
	/**
	 * 背包物品信息
	 */
	PlayerBagMsg.Builder buildPlayerBagMsg(PlayerBag playerBag);
	
	/**
	 * 装备信息
	 */
	PlayerEquipmentMsg.Builder buildPlayerEquipmentMsg(PlayerEquipment playerEquipment);
	
	/**
	 * 技能受击效果
	 */
	SkillEffectMsg.Builder buildSkillEffectMsg(SkillEffect model);
	
	/**
	 * 药品栏
	 */
	DrugLumnMsg.Builder buildDrugLumnMsg(PlayerDrug model);
	
	/**
	 * 玩家技能
	 */
	PlayerSkillMsg.Builder buildPlayerSkillMsg(PlayerSkill playerSkill);
	
	/**
	 * 玩家时装
	 */
	PlayerFashionMsg.Builder buildPlayerFashionMsg(PlayerFashion playerFashion);
	
	/**
	 * 邮件
	 */
	MailInboxMsg.Builder buildMailInboxMsg(MailInbox mailInbox);
	
	/**
	 * 好友
	 */
	ApplyMsg.Builder builderApplyMsg(PlayerApply playerApply);	
	
	/**
	 * 好友申请消息
	 */
	FriendMsg.Builder buildFriendMsg(long playerId);
	
	/**
	 * 注灵
	 */
	WakanMsg.Builder buildWakanMsg(PlayerWakan playerWakan);
	
	/**
	 * 任务信息
	 */
	PlayerTaskMsg.Builder buildPlayerTaskMsg(PlayerTask model);	
	
	/**
	 * 采集信息
	 */
	CollectItemInfoMsg.Builder buildCollectMsg(Collect model);
	
	/**
	 * 铭文信息
	 */
	PlayerWeaponEffectMsg.Builder buildPlayerWeaponEffectMsg(PlayerWeaponEffect model);
	
	/**
	 * buff信息
	 */
	BuffMsg.Builder buildBuffMsg(Buff buff);
	
	/**
	 * 玩家家族信息
	 */
	FamilyPlayerMsg.Builder buildFamilyPlayerMsg(PlayerFamily model);
	
	/**
	 * 玩家商城信息
	 */
	MarketItemMsg.Builder buildMarketItemMsg(PlayerMarket model);	
	
	/**
	 * 玩家签到信息
	 */	
	SignMsg.Builder buildSignMsg(PlayerSign model);	
	
	/**
	 * 玩家装备交易信息
	 */
	PlayerTradeBagMsg.Builder buildPlayerTradeBagMsg(PlayerTradeBag model);	
	
	/**
	 * 交易行装备信息
	 */
	PlayerTradeEquipmentMsg.Builder buildPlayerTradeEquipmentMsg(PlayerEquipment model);
	
	/**
	 * 奖励信息
	 */
	RewardMsg.Builder buildRewardMsg(int rewardId, int state);
	
	/**
	 * 羽翼信息
	 */
	WingMsg.Builder buildWingMsg(PlayerWing playerWing);
	
	/**
	 * 战力榜
	 */
	BattleValueRankMsg.Builder buildBattleValueRankMsg(BattleValueRank model);
	
	/**
	 * 装备榜
	 */
	EquipRankMsg.Builder buildEquipRankMsg(EquipRank model);
	
	/**
	 * 财富榜
	 */
	GoldRankMsg.Builder buildGoldRankMsg(GoldRank model);
	
	/**
	 * 仇敌数据
	 */
	EnemyMsg.Builder buildEnemyMsg(PlayerEnemy model);
	
	/**
	 * 怪物状态数据
	 */
	MonsterStateMsg.Builder buildMonsterStateMsg(int refreshId, int state);
	
	/**
	 * 竞技场加载数据
	 */
	LoadPkPlayerMsg.Builder buildLoadPkPlayerMsg(String guid, long playerId, String playerName, int level, int career, int stage, int star);
	
	/**
	 * 竞技场结算数据
	 */
	EndPKPlayerMsg.Builder buildEndPKPlayerMsg(String guid, long playerId, int state, int useTime, int score, int WinNum, List<Reward> rewards, int destroyTime);
	
	/**
	 * 竞技场PK奖励数据
	 */
	PKRewardMsg.Builder buildPKRewardMsg(int goodType, int itemId, int num);
	
	/**
	 * 转盘榜单信息
	 */
	TurnRecMsg.Builder buildTurnRecMsg(long playerId, String playerName, int rewardId);
	
	/**
	 * 帮派信息
	 */
	GuildMsg.Builder buildGuildMsg(Guild guild);
	
	/**
	 * 宣战信息
	 */
	GuildWarMsg.Builder buildGuildWarMsg(Guild guild);
	
	/**
	 * 帮派成员信息
	 */
	GuildPlayerMsg.Builder buildGuildPlayerMsg(PlayerGuild playerGuild);
	
	/**
	 * 帮派申请者信息
	 */
	ApplyPlayerMsg.Builder buildApplyPlayerMsg(long playerId);
	
	/**
	 * 玩家熔炉
	 */
	PlayerFurnaceMsg.Builder buildPlayerFurnaceMsg(PlayerFurnace model);
	
	/**
	 * 奖励信息
	 */
	RewardDataMsg.Builder buildRewardDataMsg(int rewardId, int num);
}
