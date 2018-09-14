package com.domain.wakan;

import java.util.List;
import java.util.Map;
/**
 * 注灵配置表
 * @author jiangqin
 * @date 2017-2-21
 */
public class BaseWakan {
	
	private	int id; 			//编号    									
    private int level;			//等级
    private String attHead;     //头
    private String attNecklace; //项链
    private String  attClothes; //上衣
    private String attCuff; 	//护腕
    private String attTrousers; //裤子
    private String attRing; 	//戒指
    private String attArm1; 	//主武器
    private String attArm2; 	//副武器
    private int needMana;  		//需求灵力
    private int expGold; 		// 消耗金币    
    private int needLevel; 		// 需求等级
	private Map<Integer, List<List<Integer>>> typeToAttrMap;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getAttHead() {
		return attHead;
	}
	public void setAttHead(String attHead) {
		this.attHead = attHead;
	}
	public String getAttNecklace() {
		return attNecklace;
	}
	public void setAttNecklace(String attNecklace) {
		this.attNecklace = attNecklace;
	}
	public String getAttClothes() {
		return attClothes;
	}
	public void setAttClothes(String attClothes) {
		this.attClothes = attClothes;
	}
	public String getAttCuff() {
		return attCuff;
	}
	public void setAttCuff(String attCuff) {
		this.attCuff = attCuff;
	}
	public String getAttTrousers() {
		return attTrousers;
	}
	public void setAttTrousers(String attTrousers) {
		this.attTrousers = attTrousers;
	}
	public String getAttRing() {
		return attRing;
	}
	public void setAttRing(String attRing) {
		this.attRing = attRing;
	}
	public String getAttArm1() {
		return attArm1;
	}
	public void setAttArm1(String attArm1) {
		this.attArm1 = attArm1;
	}
	public String getAttArm2() {
		return attArm2;
	}
	public void setAttArm2(String attArm2) {
		this.attArm2 = attArm2;
	}
	public int getNeedMana() {
		return needMana;
	}
	public void setNeedMana(int needMana) {
		this.needMana = needMana;
	}
	public Map<Integer, List<List<Integer>>> getTypeToAttrMap() {
		return typeToAttrMap;
	}
	public void setTypeToAttrMap(Map<Integer, List<List<Integer>>> typeToAttrMap) {
		this.typeToAttrMap = typeToAttrMap;
	}
	public int getExpGold() {
		return expGold;
	}
	public void setExpGold(int expGold) {
		this.expGold = expGold;
	}
	public int getNeedLevel() {
		return needLevel;
	}
	public void setNeedLevel(int needLevel) {
		this.needLevel = needLevel;
	}
}
