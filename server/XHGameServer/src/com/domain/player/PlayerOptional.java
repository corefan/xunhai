package com.domain.player;

import java.util.ArrayList;
import java.util.List;

import com.domain.GameEntity;
import com.util.SplitStringUtil;

/**
 * 角色终身设置记录
 * @author ken
 * @date 2017-6-24
 */
public class PlayerOptional extends GameEntity {

	private static final long serialVersionUID = 4983645681988999414L;

	/** 玩家编号 */
	private long playerId;

	/** 玩家首冲id列表*/
	private List<Integer> fristPayIdList = new ArrayList<Integer>();
	/** 用于存库*/
	private String fristPayIdStr;
	
	/** 手机绑定奖励(0:未领， 1:已领)*/	
	private int bindRewardState;
	
	/** 实名认证 0:未认证  1：未领取  2：已领取*/
	private int icRewardState;
	
	/** 是否接收陌生人信息*/
	private int isAcceptChat;

	/** 是否接受好友申请*/
	private int isAcceptApply;
	
	/** 是否已购买成长基金*/
	private int isBuyGrowthFund;
	
	/** 首冲奖励状态（0:不可领 1:可领取，2:已领取）*/
	private int fristPayRewardState;
	
	/** 是否已购买神器*/
	private int isBuyArtifact;
	
	/** 已领取累计充值奖励列表*/
	private List<Integer> trRewardIdList = new ArrayList<Integer>();
	private String trRewardIdStr;
	
	/** 已领取累计消费奖励列表*/
	private List<Integer> tsRewardIdList = new ArrayList<Integer>();
	private String tsRewardIdStr;

	/** 已领取成长基金ID列表*/
	private List<Integer> gfRewardIdList = new ArrayList<Integer>();
	private String gfRewardIdStr;	

	/** 已领取全民福利ID列表*/
	private List<Integer> nwRewardIdList = new ArrayList<Integer>();
	private String nwRewardIdStr;		
	
	/** 已领取开服七天乐ID列表*/
	private List<Integer> osRewardIdList = new ArrayList<Integer>();
	private String osRewardIdStr;	

	
	/** 已领取七天累计充值奖励ID列表*/
	private List<Integer> spRewardIdList = new ArrayList<Integer>();
	private String spRewardIdStr;	

	/** 已领取冲级奖励列表*/
	private List<Integer> levelRewardList = new ArrayList<Integer>();
	private String levelRewardStr;
	
	/** 已领取战力奖励列表 (battleValue)*/
	private List<Integer> bvRewardList = new ArrayList<Integer>();
	private String bvRewardStr;
	
	@Override
	public String getInsertSql() {
		return null;
	}
	
	/** 得到更新sql*/
	public String getUpdateSql() {

		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE player_optional SET ");
		sql.append(" fristPayIdStr=");
		if(fristPayIdStr == null){
			sql.append(fristPayIdStr);	
		}else{
			sql.append("'");
			sql.append(fristPayIdStr);
			sql.append("'");
		}		
		sql.append(",");	
		sql.append(" bindRewardState=");	
		sql.append(bindRewardState);		
		sql.append(",");	
		sql.append(" icRewardState=");	
		sql.append(icRewardState);		
		sql.append(",");
		sql.append(" isAcceptChat=");	
		sql.append(isAcceptChat);
		sql.append(",");	
		sql.append(" isAcceptApply=");	
		sql.append(isAcceptApply);
		sql.append(",");	
		sql.append(" isBuyGrowthFund=");	
		sql.append(isBuyGrowthFund);
		sql.append(",");
		sql.append(" fristPayRewardState=");	
		sql.append(fristPayRewardState);
		sql.append(",");
		sql.append(" isBuyArtifact=");	
		sql.append(isBuyArtifact);	
		sql.append(",");	
		sql.append(" trRewardIdStr=");
		if(trRewardIdStr == null){
			sql.append(trRewardIdStr);	
		}else{
			sql.append("'");
			sql.append(trRewardIdStr);
			sql.append("'");
		}
		sql.append(",");
		sql.append(" gfRewardIdStr=");
		if(gfRewardIdStr == null){
			sql.append(gfRewardIdStr);	
		}else{
			sql.append("'");
			sql.append(gfRewardIdStr);
			sql.append("'");
		}
		sql.append(",");
		sql.append(" nwRewardIdStr=");
		if(nwRewardIdStr == null){
			sql.append(nwRewardIdStr);	
		}else{
			sql.append("'");
			sql.append(nwRewardIdStr);
			sql.append("'");
		}		
		sql.append(",");
		sql.append(" tsRewardIdStr=");
		if(tsRewardIdStr == null){
			sql.append(tsRewardIdStr);	
		}else{
			sql.append("'");
			sql.append(tsRewardIdStr);
			sql.append("'");
		}
		sql.append(",");
		sql.append(" osRewardIdStr=");
		if(osRewardIdStr == null){
			sql.append(osRewardIdStr);	
		}else{
			sql.append("'");
			sql.append(osRewardIdStr);
			sql.append("'");
		}	
		sql.append(",");
		sql.append(" spRewardIdStr=");
		if(spRewardIdStr == null){
			sql.append(spRewardIdStr);	
		}else{
			sql.append("'");
			sql.append(spRewardIdStr);
			sql.append("'");
		}
		sql.append(",");
		sql.append(" levelRewardStr=");
		if(levelRewardStr == null){
			sql.append(levelRewardStr);	
		}else{
			sql.append("'");
			sql.append(levelRewardStr);
			sql.append("'");
		}		
		sql.append(",");
		sql.append(" bvRewardStr=");
		if(bvRewardStr == null){
			sql.append(bvRewardStr);	
		}else{
			sql.append("'");
			sql.append(bvRewardStr);
			sql.append("'");
		}	
		sql.append(" WHERE playerId = ");
		sql.append(playerId);

		return sql.toString();
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public List<Integer> getFristPayIdList() {
		return fristPayIdList;
	}

	public void setFristPayIdList(List<Integer> fristPayIdList) {
		this.fristPayIdList = fristPayIdList;
		
		this.fristPayIdStr = fristPayIdList.toString();
	}

	public String getFristPayIdStr() {
		return fristPayIdStr;
	}

	public void setFristPayIdStr(String fristPayIdStr) {
		this.fristPayIdStr = fristPayIdStr;
		
		List<Integer> list = SplitStringUtil.getIntList(fristPayIdStr);
		if(list != null){
			this.fristPayIdList = list;
		}else{
			this.fristPayIdList.clear();
		}
	}

	public int getBindRewardState() {
		return bindRewardState;
	}

	public void setBindRewardState(int bindRewardState) {
		this.bindRewardState = bindRewardState;
	}
	
	public int getIsAcceptChat() {
		return isAcceptChat;
	}

	public void setIsAcceptChat(int isAcceptChat) {
		this.isAcceptChat = isAcceptChat;
	}

	public int getIsAcceptApply() {
		return isAcceptApply;
	}

	public void setIsAcceptApply(int isAcceptApply) {
		this.isAcceptApply = isAcceptApply;
	}

	public int getFristPayRewardState() {
		return fristPayRewardState;
	}

	public void setFristPayRewardState(int fristPayRewardState) {
		this.fristPayRewardState = fristPayRewardState;
	}
	
	public List<Integer> getTrRewardIdList() {
		return trRewardIdList;
	}

	public String getTrRewardIdStr() {
		return trRewardIdStr;
	}

	public List<Integer> getGfRewardIdList() {
		return gfRewardIdList;
	}

	public String getGfRewardIdStr() {
		return gfRewardIdStr;
	}

	public List<Integer> getNwRewardIdList() {
		return nwRewardIdList;
	}

	public String getNwRewardIdStr() {
		return nwRewardIdStr;
	}
	
	public void setNwRewardIdList(List<Integer> nwRewardIdList) {
		this.nwRewardIdList = nwRewardIdList;
		
		this.nwRewardIdStr = nwRewardIdList.toString();
	}
	
	public void setNwRewardIdStr(String nwRewardIdStr) {
		this.nwRewardIdStr = nwRewardIdStr;
		
		List<Integer> list = SplitStringUtil.getIntList(nwRewardIdStr);
		if(list != null){
			this.nwRewardIdList = list;
		}else{
			this.nwRewardIdList.clear();
		}		
	}

	public int getIsBuyGrowthFund() {
		return isBuyGrowthFund;
	}

	public void setTrRewardIdList(List<Integer> trRewardIdList) {
		this.trRewardIdList = trRewardIdList;
		
		this.trRewardIdStr = trRewardIdList.toString();
	}

	public void setTrRewardIdStr(String trRewardIdStr) {
		this.trRewardIdStr = trRewardIdStr;
		
		List<Integer> list = SplitStringUtil.getIntList(trRewardIdStr);
		if(list != null){
			this.trRewardIdList = list;
		}else{
			this.trRewardIdList.clear();
		}		
	}

	public void setGfRewardIdList(List<Integer> gfRewardIdList) {
		this.gfRewardIdList = gfRewardIdList;
		
		this.gfRewardIdStr = gfRewardIdList.toString();
	}
	
	public void setGfRewardIdStr(String gfRewardIdStr) {
		this.gfRewardIdStr = gfRewardIdStr;
		
		List<Integer> list = SplitStringUtil.getIntList(gfRewardIdStr);
		if(list != null){
			this.gfRewardIdList = list;
		}else{
			this.gfRewardIdList.clear();
		}		
	}

	public void setIsBuyGrowthFund(int isBuyGrowthFund) {
		this.isBuyGrowthFund = isBuyGrowthFund;
	}	
	
	public String getTsRewardIdStr() {
		return tsRewardIdStr;
	}

	public void setTsRewardIdStr(String tsRewardIdStr) {
		this.tsRewardIdStr = tsRewardIdStr;
		
		List<Integer> list = SplitStringUtil.getIntList(tsRewardIdStr);
		if(list != null){
			this.tsRewardIdList = list;
		}else{
			this.tsRewardIdList.clear();
		}	
	}

	public List<Integer> getTsRewardIdList() {
		return tsRewardIdList;
	}

	public void setTsRewardIdList(List<Integer> tsRewardIdList) {
		this.tsRewardIdList = tsRewardIdList;
		
		this.tsRewardIdStr = tsRewardIdList.toString();
	}
	
	public int getIsBuyArtifact() {
		return isBuyArtifact;
	}

	public void setIsBuyArtifact(int isBuyArtifact) {
		this.isBuyArtifact = isBuyArtifact;
	}

	public List<Integer> getOsRewardIdList() {
		return osRewardIdList;
	}

	public void setOsRewardIdList(List<Integer> osRewardIdList) {
		this.osRewardIdList = osRewardIdList;
		
		this.osRewardIdStr = osRewardIdList.toString();
	}

	public String getOsRewardIdStr() {
		return osRewardIdStr;
	}

	public void setOsRewardIdStr(String osRewardIdStr) {
		this.osRewardIdStr = osRewardIdStr;
		
		List<Integer> list = SplitStringUtil.getIntList(osRewardIdStr);
		if(list != null){
			this.osRewardIdList = list;
		}else{
			this.osRewardIdList.clear();
		}		
	}

	public List<Integer> getSpRewardIdList() {
		return spRewardIdList;
	}

	public String getSpRewardIdStr() {
		return spRewardIdStr;
	}

	public void setSpRewardIdList(List<Integer> spRewardIdList) {
		this.spRewardIdList = spRewardIdList;
		
		this.spRewardIdStr = spRewardIdList.toString();
	}

	public void setSpRewardIdStr(String spRewardIdStr) {
		this.spRewardIdStr = spRewardIdStr;
		
		List<Integer> list = SplitStringUtil.getIntList(spRewardIdStr);
		if(list != null){
			this.spRewardIdList = list;
		}else{
			this.spRewardIdList.clear();
		}	
	}

	public List<Integer> getLevelRewardList() {
		return levelRewardList;
	}

	public String getLevelRewardStr() {
		return levelRewardStr;
	}

	public List<Integer> getBvRewardList() {
		return bvRewardList;
	}

	public String getBvRewardStr() {
		return bvRewardStr;
	}

	public void setLevelRewardList(List<Integer> levelRewardList) {
		this.levelRewardList = levelRewardList;
		
		this.levelRewardStr = levelRewardList.toString();
	}

	public void setLevelRewardStr(String levelRewardStr) {
		this.levelRewardStr = levelRewardStr;
		
		List<Integer> list = SplitStringUtil.getIntList(levelRewardStr);
		if(list != null){
			this.levelRewardList = list;
		}else{
			this.levelRewardList.clear();
		}	
	}

	public void setBvRewardList(List<Integer> bvRewardList) {
		this.bvRewardList = bvRewardList;
		
		this.bvRewardStr = bvRewardList.toString();
	}

	public void setBvRewardStr(String bvRewardStr) {
		this.bvRewardStr = bvRewardStr;
		
		List<Integer> list = SplitStringUtil.getIntList(bvRewardStr);
		if(list != null){
			this.bvRewardList = list;
		}else{
			this.bvRewardList.clear();
		}	
	}

	public int getIcRewardState() {
		return icRewardState;
	}

	public void setIcRewardState(int icRewardState) {
		this.icRewardState = icRewardState;
	}
	
}
