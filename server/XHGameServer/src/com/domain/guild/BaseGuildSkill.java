package com.domain.guild;

import java.io.Serializable;
import java.util.List;

/**
 * 帮派技能配置
 * @author ken
 * @date 2018年7月5日
 */
public class BaseGuildSkill implements Serializable {

	private static final long serialVersionUID = 7318614656513756816L;

	/** 编号*/
	private int id;
	/** 技能类型*/
	private int type;
	/** 技能等级*/
	private int level;
	/** 技能名称*/
	private String skillName;
	/** 学习所需玩家等级*/
	private int playerLv;
	/** 学习所需金币*/
	private int money;
	/** 学习所需贡献*/
	private int contribute;
	/** 研发所需帮派等级*/
	private int guildLv;
	/** 研发所需帮派建设度*/
	private int guildBuildNum;
	/** 研发所需帮派资金*/
	private int guildMoney;
	/** 学习获得属性*/
	private String addProperty;
	private List<Integer> addPropertyList;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getSkillName() {
		return skillName;
	}
	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}
	public int getPlayerLv() {
		return playerLv;
	}
	public void setPlayerLv(int playerLv) {
		this.playerLv = playerLv;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public int getContribute() {
		return contribute;
	}
	public void setContribute(int contribute) {
		this.contribute = contribute;
	}
	public int getGuildLv() {
		return guildLv;
	}
	public void setGuildLv(int guildLv) {
		this.guildLv = guildLv;
	}
	public int getGuildBuildNum() {
		return guildBuildNum;
	}
	public void setGuildBuildNum(int guildBuildNum) {
		this.guildBuildNum = guildBuildNum;
	}
	public int getGuildMoney() {
		return guildMoney;
	}
	public void setGuildMoney(int guildMoney) {
		this.guildMoney = guildMoney;
	}
	public String getAddProperty() {
		return addProperty;
	}
	public void setAddProperty(String addProperty) {
		this.addProperty = addProperty;
	}
	public List<Integer> getAddPropertyList() {
		return addPropertyList;
	}
	public void setAddPropertyList(List<Integer> addPropertyList) {
		this.addPropertyList = addPropertyList;
	}
	
}
