package com.domain.fuse;

import java.io.Serializable;
import java.util.List;

import com.domain.Reward;

/**
 * 合成/分解基础表
 * @author jiangqin
 * @date 2017-5-3
 */
public class BaseCompose implements Serializable{

	private static final long serialVersionUID = -9185605564644153971L;
	
	/** 合成的物品ID*/
	private int id;		
	/** 是否可合成 0=否1=是*/
	private int isCompose;	
	/** 是否可分解 0=否1=是*/
	private int isDecompose;
	/** 是否可提炼 0=否1=是*/
	private int isRefine;
	/** 合成成功率*/
	private int success;
	/** 合成消耗*/
	private String  composeStr;
	/** 合成消耗*/
	private List<Reward> composeList;	
	/** 分解获得*/
	private String  decomposeStr;
	/** 分解获得*/
	private List<Reward> decomposeList;
	/** 提炼获得*/
	private String  refineStr;
	/** 提炼获得*/
	private List<Reward> refineList;
	/** 分类id*/
	private int tradeType;
	
	public int getId() {
		return id;
	}
	public int getIsCompose() {
		return isCompose;
	}
	public int getIsDecompose() {
		return isDecompose;
	}
	public int getSuccess() {
		return success;
	}
	public String getComposeStr() {
		return composeStr;
	}
	public List<Reward> getComposeList() {
		return composeList;
	}
	public String getDecomposeStr() {
		return decomposeStr;
	}
	public int getTradeType() {
		return tradeType;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setIsCompose(int isCompose) {
		this.isCompose = isCompose;
	}
	public void setIsDecompose(int isDecompose) {
		this.isDecompose = isDecompose;
	}
	public void setSuccess(int success) {
		this.success = success;
	}
	public void setComposeStr(String composeStr) {
		this.composeStr = composeStr;
	}
	public void setComposeList(List<Reward> composeList) {
		this.composeList = composeList;
	}
	public void setDecomposeStr(String decomposeStr) {
		this.decomposeStr = decomposeStr;
	}
	public List<Reward> getDecomposeList() {
		return decomposeList;
	}
	public void setDecomposeList(List<Reward> decomposeList) {
		this.decomposeList = decomposeList;
	}
	public void setTradeType(int tradeType) {
		this.tradeType = tradeType;
	}
	public int getIsRefine() {
		return isRefine;
	}
	public void setIsRefine(int isRefine) {
		this.isRefine = isRefine;
	}
	public String getRefineStr() {
		return refineStr;
	}
	public void setRefineStr(String refineStr) {
		this.refineStr = refineStr;
	}
	public List<Reward> getRefineList() {
		return refineList;
	}
	public void setRefineList(List<Reward> refineList) {
		this.refineList = refineList;
	}

}
