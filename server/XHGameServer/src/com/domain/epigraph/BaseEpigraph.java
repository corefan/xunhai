package com.domain.epigraph;

import java.io.Serializable;
import java.util.List;


/**
 * 铭文基础数据
 * @author jiangqin
 * @date 2017-2-25
 */
public class BaseEpigraph implements Serializable{

	private static final long serialVersionUID = -499570644037248963L;
	
	/** 铭文编号*/
	private Integer id;
	/** 铭文名称*/
	private String name;
	/** 随机效果类型 [类型，概率]  1:技能  2：属性*/
	private String randTypes;
	private List<List<Integer>> randTypeList;
	
	/** 随机技能  [技能下标， 技能基础编号， 概率]*/
	private String randSkills;	
	private List<List<Integer>> randSkillList;
	
	/** 随机属性 [属性编号， 属性值， 概率]*/
	private String randAttrs;	
	private List<List<Integer>> randAttrList;
	
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
	public String getRandTypes() {
		return randTypes;
	}
	public void setRandTypes(String randTypes) {
		this.randTypes = randTypes;
	}
	public List<List<Integer>> getRandTypeList() {
		return randTypeList;
	}
	public void setRandTypeList(List<List<Integer>> randTypeList) {
		this.randTypeList = randTypeList;
	}
	public String getRandSkills() {
		return randSkills;
	}
	public void setRandSkills(String randSkills) {
		this.randSkills = randSkills;
	}
	public List<List<Integer>> getRandSkillList() {
		return randSkillList;
	}
	public void setRandSkillList(List<List<Integer>> randSkillList) {
		this.randSkillList = randSkillList;
	}
	public String getRandAttrs() {
		return randAttrs;
	}
	public void setRandAttrs(String randAttrs) {
		this.randAttrs = randAttrs;
	}
	public List<List<Integer>> getRandAttrList() {
		return randAttrList;
	}
	public void setRandAttrList(List<List<Integer>> randAttrList) {
		this.randAttrList = randAttrList;
	}
	
}
