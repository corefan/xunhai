package com.domain.bag;

import java.io.Serializable;
import java.util.List;

import com.domain.Reward;

/**
 * 装备表
 * @author ken
 * @date 2017-1-4
 */
public class BaseEquipment implements Serializable {

	private static final long serialVersionUID = 6602799205974565709L;

	/** 装备编号*/
	private Integer id;
	/** 物品类1=装备 2=妙药 3=珍材*/
	private int goodsType;
	/** 装备名字*/
	private String name;
	/** 品级*/
	private int rare;
	/** 0通用  1：贵重*/
	private int bevaluable;
	/** 使用等级*/
	private int level;
	/** 附加属性等级(对应eauipAddAttr表）*/
	private int addAttrLevel;	
	/** 使用职业   0=通用 */
	private int needJob;
	/** 购买价格*/
	private int buyPrice;
	/** 售卖价格  0：不可售卖*/
	private int sellPrice;
	/** 掉落图标*/
	private int dropsIcon;
	/** 广播类型  0无广播  1世界 2队伍*/
	private int showType;
	/** 装备部位*/
	private int equipType;	
	
	/** 基础属性 */
	private String baseProperty;
	private List<List<Integer>> basePropertyList;
	
	/** 装备附加属性条数*/
	private int addAttrNum;
	
	/** 主武器外形*/
	private int weaponStyle;

	/** 铭文开孔几率（百分比）*/
	private int openHole;
	/** 最大铭文孔*/
	private int maxHole;
	/** 最小铭文孔*/
	private int minHole;
	
	/** 是否可出售给交易行0：否1：是*/
	private int isTrade;
	/** 交易行最低卖价*/
	private int tradeMinPrice;
	/** 交易行最高卖价*/
	private int tradeMaxPrice;	

	/** pk是否掉落*/
	private int isPkDrop;
	
	/** 合成新装备消耗金币*/
	private String composeCost;
	private List<Reward> composeCostList;
	/** 可合成的新装备*/
	private int composeEquip;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public int getGoodsType() {
		return goodsType;
	}
	public void setGoodsType(int goodsType) {
		this.goodsType = goodsType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public int getEquipType() {
		return equipType;
	}
	public void setEquipType(int equipType) {
		this.equipType = equipType;
	}
	public String getBaseProperty() {
		return baseProperty;
	}
	public void setBaseProperty(String baseProperty) {
		this.baseProperty = baseProperty;
	}
	public List<List<Integer>> getBasePropertyList() {
		return basePropertyList;
	}
	public void setBasePropertyList(List<List<Integer>> basePropertyList) {
		this.basePropertyList = basePropertyList;
	}
	public int getOpenHole() {
		return openHole;
	}
	public void setOpenHole(int openHole) {
		this.openHole = openHole;
	}
	public int getWeaponStyle() {
		return weaponStyle;
	}
	public void setWeaponStyle(int weaponStyle) {
		this.weaponStyle = weaponStyle;
	}
	public int getIsTrade() {
		return isTrade;
	}
	public void setIsTrade(int isTrade) {
		this.isTrade = isTrade;
	}
	public int getTradeMinPrice() {
		return tradeMinPrice;
	}
	public void setTradeMinPrice(int tradeMinPrice) {
		this.tradeMinPrice = tradeMinPrice;
	}
	public int getTradeMaxPrice() {
		return tradeMaxPrice;
	}
	public void setTradeMaxPrice(int tradeMaxPrice) {
		this.tradeMaxPrice = tradeMaxPrice;
	}
	public int getAddAttrNum() {
		return addAttrNum;
	}
	public void setAddAttrNum(int addAttrNum) {
		this.addAttrNum = addAttrNum;
	}
	public int getMaxHole() {
		return maxHole;
	}
	public void setMaxHole(int maxHole) {
		this.maxHole = maxHole;
	}
	public int getAddAttrLevel() {
		return addAttrLevel;
	}
	public void setAddAttrLevel(int addAttrLevel) {
		this.addAttrLevel = addAttrLevel;
	}
	public int getIsPkDrop() {
		return isPkDrop;
	}
	public void setIsPkDrop(int isPkDrop) {
		this.isPkDrop = isPkDrop;
	}
	public int getMinHole() {
		return minHole;
	}
	public void setMinHole(int minHole) {
		this.minHole = minHole;
	}
	public String getComposeCost() {
		return composeCost;
	}
	public void setComposeCost(String composeCost) {
		this.composeCost = composeCost;
	}
	public List<Reward> getComposeCostList() {
		return composeCostList;
	}
	public void setComposeCostList(List<Reward> composeCostList) {
		this.composeCostList = composeCostList;
	}
	public int getComposeEquip() {
		return composeEquip;
	}
	public void setComposeEquip(int composeEquip) {
		this.composeEquip = composeEquip;
	}
	
}
