package com.domain.buff;

import java.io.Serializable;
import java.util.List;

/**
 * buff配置表
 * @author ken
 * @date 2017-3-31
 */
public class BaseBuff implements Serializable {

	private static final long serialVersionUID = -642509873909249124L;

	/** buff编号*/
	private int buffId;
	/** buff组 */
	private int groupId;
	/** buff名称*/
	private String name;
	/** 类型   @BuffConstant*/
	private int type;
	/** buff等级*/
	private int buffLv;
	/** 0 = 其他类型 1 = 增益 2 = 减益*/
	private int eBuffType;
	/** 持续时间   -1：永久*/
	private int remainTime;
	/** 间隔时间*/
	private int periodTime;
	/** 影响属性组*/
	private String effectPro;
	private List<List<Integer>> effectProList;
	
	/** 移除类型1：打断 2：死亡 3：下线 */
	private String removeType;
	private List<Integer> removeTypeList;
	
	/** 是否可被高等级buff替换*/
	private int replace;
	
	/** 是否免疫debuff*/	
	private int immuneDebuff;

	public int getBuffId() {
		return buffId;
	}

	public void setBuffId(int buffId) {
		this.buffId = buffId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getBuffLv() {
		return buffLv;
	}

	public void setBuffLv(int buffLv) {
		this.buffLv = buffLv;
	}

	public int geteBuffType() {
		return eBuffType;
	}

	public void seteBuffType(int eBuffType) {
		this.eBuffType = eBuffType;
	}

	public int getRemainTime() {
		return remainTime;
	}

	public void setRemainTime(int remainTime) {
		this.remainTime = remainTime;
	}

	public int getPeriodTime() {
		return periodTime;
	}

	public void setPeriodTime(int periodTime) {
		this.periodTime = periodTime;
	}

	public String getEffectPro() {
		return effectPro;
	}

	public void setEffectPro(String effectPro) {
		this.effectPro = effectPro;
	}

	public List<List<Integer>> getEffectProList() {
		return effectProList;
	}

	public void setEffectProList(List<List<Integer>> effectProList) {
		this.effectProList = effectProList;
	}

	public String getRemoveType() {
		return removeType;
	}

	public void setRemoveType(String removeType) {
		this.removeType = removeType;
	}

	public List<Integer> getRemoveTypeList() {
		return removeTypeList;
	}

	public void setRemoveTypeList(List<Integer> removeTypeList) {
		this.removeTypeList = removeTypeList;
	}

	public int getReplace() {
		return replace;
	}

	public void setReplace(int replace) {
		this.replace = replace;
	}

	public int getImmuneDebuff() {
		return immuneDebuff;
	}

	public void setImmuneDebuff(int immuneDebuff) {
		this.immuneDebuff = immuneDebuff;
	}
	
}
