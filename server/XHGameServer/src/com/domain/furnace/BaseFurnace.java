package com.domain.furnace;

import java.io.Serializable;
import java.util.List;

/**
 * 熔炉配置
 * @author ken
 * @date 2018年4月23日
 */
public class BaseFurnace implements Serializable {

	private static final long serialVersionUID = -266238972470001634L;
	
	/** 自动编号*/
	private int id;
	/** 熔炉编号*/
	private int furnaceId;
	/** 名称*/
	private String furnaceName;
	/** 阶段*/
	private int stage;
	/** 星级*/	
	private int star;
	/** 升星所需碎片*/
	private int needPiece;
	/** 当前属性*/
	private String curProperty;
	private List<List<Integer>> curPropertyList;
	/** 升级属性*/
	private String nextProperty;
	private List<List<Integer>> nextPropertyList;
	/** 描述*/
	private String des;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getFurnaceId() {
		return furnaceId;
	}
	public void setFurnaceId(int furnaceId) {
		this.furnaceId = furnaceId;
	}
	public String getFurnaceName() {
		return furnaceName;
	}
	public void setFurnaceName(String furnaceName) {
		this.furnaceName = furnaceName;
	}
	public int getStar() {
		return star;
	}
	public void setStar(int star) {
		this.star = star;
	}
	public int getNeedPiece() {
		return needPiece;
	}
	public void setNeedPiece(int needPiece) {
		this.needPiece = needPiece;
	}
	public String getNextProperty() {
		return nextProperty;
	}
	public void setNextProperty(String nextProperty) {
		this.nextProperty = nextProperty;
	}
	public List<List<Integer>> getNextPropertyList() {
		return nextPropertyList;
	}
	public void setNextPropertyList(List<List<Integer>> nextPropertyList) {
		this.nextPropertyList = nextPropertyList;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public int getStage() {
		return stage;
	}
	public void setStage(int stage) {
		this.stage = stage;
	}
	public String getCurProperty() {
		return curProperty;
	}
	public void setCurProperty(String curProperty) {
		this.curProperty = curProperty;
	}
	public List<List<Integer>> getCurPropertyList() {
		return curPropertyList;
	}
	public void setCurPropertyList(List<List<Integer>> curPropertyList) {
		this.curPropertyList = curPropertyList;
	}
}
