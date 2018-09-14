package com.domain.bag;

import java.io.Serializable;
import java.util.List;

import com.domain.Reward;

/**
 * 物品表
 * @author ken
 * @date 2017-1-4
 */
public class BaseItem implements Serializable {

	private static final long serialVersionUID = 8446957006727431558L;

	/** 物品编号*/
	private Integer id;
	/** 物品名字*/
	private String name;
	/** 物品类1=装备 2=妙药 3=珍材*/
	private int goodsType;
	/** 物品小类*/
	private int tinyType;
	/** 效果类  @ItemConstant*/
	private int effectType;
	/** 使用效果值*/
	private int effectValue;
	/** 品级*/
	private int rare;
	/** 0通用  1：贵重*/
	private int bevaluable;
	/** 使用等级*/
	private int level;
	/** 使用职业   0=通用 */
	private int needJob;
	/** 购买价格*/
	private int buyPrice;
	/** 售卖价格  0：不可售卖*/
	private int sellPrice;
	/** 物品图标*/
	private int icon;
	/** 掉落图标*/
	private int dropsIcon;
	/** 广播类型  0无广播  1世界 2队伍*/
	private int showType;
	/** 最大叠加数量*/
	private int pileNumber;
	/** 装备物品栏   0不可装备  1红药栏  2蓝药栏*/
	private int partType;
	/** buff编号*/
	private int buffId;
	/** 使用类型(0.不能直接使用 1.可使用 2.可批量使用)*/
	private int useType;
	
	/** 是否可出售给交易行0：否 1：是*/
	private int isTrade;	
	/** 交易行初始*/
	private int tradeInitPrice;	
	/** 交易行最低出售价格限制 */
	private int tradeMinPrice;	
	/** 交易行最高出售价格限制 */
	private int tradeMaxPrice;
	/** 每日购买次数  0：无限制*/
	private int buyOnes;	
	/** 每日出售次数  0：无限制*/
	private int sellOnes;
	/** 交易类型*/
	private int tradeType;
	/** 使用消耗*/
	private String useExpend;
	private List<Reward> useExpendList;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getGoodsType() {
		return goodsType;
	}
	public void setGoodsType(int goodsType) {
		this.goodsType = goodsType;
	}
	public int getTinyType() {
		return tinyType;
	}
	public void setTinyType(int tinyType) {
		this.tinyType = tinyType;
	}
	public int getRare() {
		return rare;
	}
	public void setRare(int rare) {
		this.rare = rare;
	}
	public int getBevaluable() {
		return bevaluable;
	}
	public void setBevaluable(int bevaluable) {
		this.bevaluable = bevaluable;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getNeedJob() {
		return needJob;
	}
	public void setNeedJob(int needJob) {
		this.needJob = needJob;
	}
	public int getBuyPrice() {
		return buyPrice;
	}
	public void setBuyPrice(int buyPrice) {
		this.buyPrice = buyPrice;
	}
	public int getSellPrice() {
		return sellPrice;
	}
	public void setSellPrice(int sellPrice) {
		this.sellPrice = sellPrice;
	}
	public int getDropsIcon() {
		return dropsIcon;
	}
	public void setDropsIcon(int dropsIcon) {
		this.dropsIcon = dropsIcon;
	}
	public int getShowType() {
		return showType;
	}
	public void setShowType(int showType) {
		this.showType = showType;
	}
	public int getPileNumber() {
		return pileNumber;
	}
	public void setPileNumber(int pileNumber) {
		this.pileNumber = pileNumber;
	}
	public int getPartType() {
		return partType;
	}
	public void setPartType(int partType) {
		this.partType = partType;
	}
	public int getBuffId() {
		return buffId;
	}
	public void setBuffId(int buffId) {
		this.buffId = buffId;
	}
	public int getUseType() {
		return useType;
	}
	public void setUseType(int useType) {
		this.useType = useType;
	}
	public int getEffectValue() {
		return effectValue;
	}
	public void setEffectValue(int effectValue) {
		this.effectValue = effectValue;
	}
	public int getEffectType() {
		return effectType;
	}
	public void setEffectType(int effectType) {
		this.effectType = effectType;
	}
	public int getTradeType() {
		return tradeType;
	}
	public void setTradeType(int tradeType) {
		this.tradeType = tradeType;
	}
	public int getIsTrade() {
		return isTrade;
	}
	public void setIsTrade(int isTrade) {
		this.isTrade = isTrade;
	}
	public int getTradeInitPrice() {
		return tradeInitPrice;
	}
	public void setTradeInitPrice(int tradeInitPrice) {
		this.tradeInitPrice = tradeInitPrice;
	}
	public int getTradeMinPrice() {
		return tradeMinPrice;
	}
	public void setTradeMinPrice(int tradeMinPrice) {
		this.tradeMinPrice = tradeMinPrice;
	}
	public int getBuyOnes() {
		return buyOnes;
	}
	public void setBuyOnes(int buyOnes) {
		this.buyOnes = buyOnes;
	}
	public int getSellOnes() {
		return sellOnes;
	}
	public void setSellOnes(int sellOnes) {
		this.sellOnes = sellOnes;
	}
	public int getTradeMaxPrice() {
		return tradeMaxPrice;
	}
	public void setTradeMaxPrice(int tradeMaxPrice) {
		this.tradeMaxPrice = tradeMaxPrice;
	}
	public String getUseExpend() {
		return useExpend;
	}
	public List<Reward> getUseExpendList() {
		return useExpendList;
	}
	public void setUseExpend(String useExpend) {
		this.useExpend = useExpend;
	}
	public void setUseExpendList(List<Reward> useExpendList) {
		this.useExpendList = useExpendList;
	}
	public int getIcon() {
		return icon;
	}
	public void setIcon(int icon) {
		this.icon = icon;
	}	
	
}
