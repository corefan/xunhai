package com.domain.guild;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import com.domain.GameEntity;
import com.util.SplitStringUtil;

/**
 * 帮派信息
 * @author ken
 * @date 2018年3月31日
 */
public class Guild extends GameEntity {

	private static final long serialVersionUID = 5899110151663250741L;

	/** 帮派人员进退锁*/
	private Object lock = new Object();
	/** 帮派操作锁*/
	private Object oprLock = new Object();
	
	/** 帮会编号*/
	private long guildId;
	/** 帮会名称*/
	private String guildName;
	/** 帮会公告*/
	private String notice;
	/** 帮主*/
	private long headerId;
	/** 帮主名称*/
	private String headerName;
	/** 帮会等级*/
	private int level;
	/** 最大人数*/
	private int maxNum;
	/** 当前副都护人数*/
	private int assistantNum;
	/** 战斗力 */
	private int battleValue;
	/** 是否自动加入*/
	private int autoJoin;
	/** 最小设定*/
	private int autoMinLv;
	/** 最大设定*/
	private int autoMaxLv;
	/** 帮派资金*/
	private volatile int money;
	/** 建设度*/
	private volatile int buildNum;
	/** 已研发技能*/
	private String skillInfo;
	private Map<Integer, Integer> skillMap = new HashMap<Integer, Integer>();
	
	/** 创建时间 */
	private Date createTime;
	/** 删除标识*/
	private int deleteFlag;
	
	/** 申请列表*/
	private LinkedBlockingQueue<Long> applyList = new LinkedBlockingQueue<Long>();
	/** 帮派成员*/
	private LinkedBlockingQueue<Long> playerIds = new LinkedBlockingQueue<Long>();
	/** 见习成员*/
	private LinkedBlockingQueue<Long> traineeIds = new LinkedBlockingQueue<Long>();
	/** 宣战列表*/
	private Map<Long, GuildWar> guildWarMap = new ConcurrentHashMap<Long, GuildWar>();
	
	/** 是否守城方*/
	private boolean defend;
	/** 是否已报名城战*/
	private boolean applyFlag;
	/** 所属联盟*/
	private long unionId;
	private String unionName;
	/** 已提交的攻城令*/
	private String itemInfo;
	private Map<Long, Integer> itemMap = new HashMap<Long, Integer>();
	private int allItemNum;
	
	/** 今日领地已召唤次数*/
	private int callNum;
	/** 当前喂养精华数量*/
	private int feedNum;
	
	public String getInsertSql() {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO guild ");
		sql.append("(guildId, guildName, notice, headerId, headerName, level, maxNum, assistantNum, battleValue, autoJoin, autoMinLv, autoMaxLv, money, buildNum, skillInfo, defend, applyFlag, unionId, unionName, itemInfo, allItemNum, createTime, deleteFlag) VALUES"); 
		sql.append(" (");	
		sql.append(guildId);
		sql.append(",");
		if (guildName == null) {
			sql.append(guildName);
		} else {
			sql.append("'");
			sql.append(guildName);
			sql.append("'");
		}
		sql.append(",");
		if (notice == null) {
			sql.append(notice);
		} else {
			sql.append("'");
			sql.append(notice);
			sql.append("'");
		}
		sql.append(",");
		sql.append(headerId);
		sql.append(",");
		if (headerName == null) {
			sql.append(headerName);
		} else {
			sql.append("'");
			sql.append(headerName);
			sql.append("'");
		}
		sql.append(",");
		sql.append(level);
		sql.append(",");
		sql.append(maxNum);
		sql.append(",");
		sql.append(assistantNum);
		sql.append(",");
		sql.append(battleValue);
		sql.append(",");
		sql.append(autoJoin);
		sql.append(",");
		sql.append(autoMinLv);
		sql.append(",");
		sql.append(autoMaxLv);
		sql.append(",");
		sql.append(money);
		sql.append(",");
		sql.append(buildNum);
		sql.append(",");
		if (skillInfo == null) {
			sql.append(skillInfo);
		} else {
			sql.append("'");
			sql.append(skillInfo);
			sql.append("'");
		}
		sql.append(",");
		sql.append(defend);
		sql.append(",");
		sql.append(applyFlag);
		sql.append(",");
		sql.append(unionId);
		sql.append(",");
		if (unionName == null) {
			sql.append(unionName);
		} else {
			sql.append("'");
			sql.append(unionName);
			sql.append("'");
		}
		sql.append(",");
		if (itemInfo == null) {
			sql.append(itemInfo);
		} else {
			sql.append("'");
			sql.append(itemInfo);
			sql.append("'");
		}
		sql.append(",");
		sql.append(allItemNum);
		sql.append(",");
		if(createTime == null){
			sql.append(createTime);
		}else{
			sql.append("'");
			sql.append(new Timestamp(createTime.getTime()));
			sql.append("'");
		}
		sql.append(",");
		sql.append(deleteFlag);
		sql.append(")");
		
		return sql.toString();
	}
	

	/**
	 * 得到更新sql
	 * */
	public String getUpdateSql() {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE guild SET ");
		sql.append("guildName = ");
		if (guildName == null) {
			sql.append(guildName);
		} else {
			sql.append("'");
			sql.append(guildName);
			sql.append("'");
		}
		sql.append(",");
		sql.append("notice = ");
		if (notice == null) {
			sql.append(notice);
		} else {
			sql.append("'");
			sql.append(notice);
			sql.append("'");
		}
		sql.append(",");
		sql.append("headerId = ");
		sql.append(headerId);
		sql.append(",");
		sql.append("headerName = ");
		if (headerName == null) {
			sql.append(headerName);
		} else {
			sql.append("'");
			sql.append(headerName);
			sql.append("'");
		}
		sql.append(",");
		sql.append("level = ");
		sql.append(level);
		sql.append(",");
		sql.append("maxNum = ");
		sql.append(maxNum);
		sql.append(",");
		sql.append("assistantNum = ");
		sql.append(assistantNum);
		sql.append(",");
		sql.append("battleValue = ");
		sql.append(battleValue);
		sql.append(",");
		sql.append("autoJoin = ");
		sql.append(autoJoin);
		sql.append(",");
		sql.append("autoMinLv = ");
		sql.append(autoMinLv);
		sql.append(",");
		sql.append("autoMaxLv = ");
		sql.append(autoMaxLv);
		sql.append(",");
		sql.append("money = ");
		sql.append(money);
		sql.append(",");
		sql.append("buildNum = ");
		sql.append(buildNum);
		sql.append(",");
		sql.append("skillInfo = ");
		if (skillInfo == null) {
			sql.append(skillInfo);
		} else {
			sql.append("'");
			sql.append(skillInfo);
			sql.append("'");
		}
		sql.append(",");
		sql.append("defend = ");
		sql.append(defend);
		sql.append(",");
		sql.append("applyFlag = ");
		sql.append(applyFlag);
		sql.append(",");
		sql.append("unionId = ");
		sql.append(unionId);
		sql.append(",");
		sql.append("unionName = ");
		if(unionName == null){
			sql.append(unionName);
		}else{
			sql.append("'");
			sql.append(unionName);
			sql.append("'");
		}
		sql.append(",");
		sql.append("itemInfo = ");
		if(itemInfo == null){
			sql.append(itemInfo);
		}else{
			sql.append("'");
			sql.append(itemInfo);
			sql.append("'");
		}
		sql.append(",");
		sql.append("allItemNum = ");
		sql.append(allItemNum);
		sql.append(",");
		sql.append("callNum = ");
		sql.append(callNum);
		sql.append(",");
		sql.append("feedNum = ");
		sql.append(feedNum);
		sql.append(",");
		sql.append("createTime = ");
		if(createTime == null){
			sql.append(createTime);
		}else{
			sql.append("'");
			sql.append(new Timestamp(createTime.getTime()));
			sql.append("'");
		}
		sql.append(",");
		sql.append("deleteFlag = ");
		sql.append(deleteFlag);
		sql.append(" WHERE guildId = ");
		sql.append(guildId);
		
		return sql.toString();
	}
	
	public long getGuildId() {
		return guildId;
	}
	public void setGuildId(long guildId) {
		this.guildId = guildId;
	}
	public String getGuildName() {
		return guildName;
	}
	public void setGuildName(String guildName) {
		this.guildName = guildName;
	}
	public String getNotice() {
		return notice;
	}
	public void setNotice(String notice) {
		this.notice = notice;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getBattleValue() {
		return battleValue;
	}
	public void setBattleValue(int battleValue) {
		this.battleValue = battleValue;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public int getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(int deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	public LinkedBlockingQueue<Long> getApplyList() {
		return applyList;
	}
	public void setApplyList(LinkedBlockingQueue<Long> applyList) {
		this.applyList = applyList;
	}
	public int getAutoJoin() {
		return autoJoin;
	}
	public void setAutoJoin(int autoJoin) {
		this.autoJoin = autoJoin;
	}
	public int getAutoMinLv() {
		return autoMinLv;
	}
	public void setAutoMinLv(int autoMinLv) {
		this.autoMinLv = autoMinLv;
	}
	public int getAutoMaxLv() {
		return autoMaxLv;
	}
	public void setAutoMaxLv(int autoMaxLv) {
		this.autoMaxLv = autoMaxLv;
	}
	public int getMaxNum() {
		return maxNum;
	}
	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}
	public LinkedBlockingQueue<Long> getPlayerIds() {
		return playerIds;
	}

	public void setPlayerIds(LinkedBlockingQueue<Long> playerIds) {
		this.playerIds = playerIds;
	}
	public Object getLock() {
		return lock;
	}
	public void setLock(Object lock) {
		this.lock = lock;
	}
	public int getAssistantNum() {
		return assistantNum;
	}

	public void setAssistantNum(int assistantNum) {
		if(assistantNum < 0) assistantNum = 0;
		
		this.assistantNum = assistantNum;
	}

	public long getHeaderId() {
		return headerId;
	}

	public void setHeaderId(long headerId) {
		this.headerId = headerId;
	}

	public String getHeaderName() {
		return headerName;
	}

	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		if(money < 0) money = 0;
		this.money = money;
	}

	public int getBuildNum() {
		return buildNum;
	}

	public void setBuildNum(int buildNum) {
		if(buildNum < 0) buildNum = 0;
		this.buildNum = buildNum;
	}


	public LinkedBlockingQueue<Long> getTraineeIds() {
		return traineeIds;
	}


	public void setTraineeIds(LinkedBlockingQueue<Long> traineeIds) {
		this.traineeIds = traineeIds;
	}


	public Map<Long, GuildWar> getGuildWarMap() {
		return guildWarMap;
	}


	public void setGuildWarMap(Map<Long, GuildWar> guildWarMap) {
		this.guildWarMap = guildWarMap;
	}


	public String getSkillInfo() {
		return skillInfo;
	}


	public void setSkillInfo(String skillInfo) {
		this.skillInfo = skillInfo;
		
		Map<Integer, Integer> map = SplitStringUtil.getIntIntMap(skillInfo);
		if(map != null){
			this.skillMap = map;
		}else{
			this.skillMap.clear();
		}
	}


	public Map<Integer, Integer> getSkillMap() {
		return skillMap;
	}


	public void setSkillMap(Map<Integer, Integer> skillMap) {
		this.skillMap = skillMap;
		
		this.skillInfo = SplitStringUtil.getStringByIntIntMap(skillMap);
	}


	public Object getOprLock() {
		return oprLock;
	}


	public void setOprLock(Object oprLock) {
		this.oprLock = oprLock;
	}


	public boolean isDefend() {
		return defend;
	}


	public void setDefend(boolean defend) {
		this.defend = defend;
	}

	public long getUnionId() {
		return unionId;
	}

	public void setUnionId(long unionId) {
		this.unionId = unionId;
	}

	public String getUnionName() {
		return unionName;
	}

	public void setUnionName(String unionName) {
		this.unionName = unionName;
	}

	public String getItemInfo() {
		return itemInfo;
	}

	public void setItemInfo(String itemInfo) {
		this.itemInfo = itemInfo;
		
		Map<Long, Integer> map = SplitStringUtil.getLongIntMap(itemInfo);
		if(map != null){
			this.itemMap = map;
		}else{
			this.itemMap.clear();
		}
	}

	public Map<Long, Integer> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<Long, Integer> itemMap) {
		this.itemMap = itemMap;
		
		this.itemInfo = SplitStringUtil.getStringByLongIntMap(itemMap);
	}

	public boolean isApplyFlag() {
		return applyFlag;
	}

	public void setApplyFlag(boolean applyFlag) {
		this.applyFlag = applyFlag;
	}


	public int getAllItemNum() {
		return allItemNum;
	}


	public void setAllItemNum(int allItemNum) {
		this.allItemNum = allItemNum;
	}


	public int getCallNum() {
		return callNum;
	}


	public void setCallNum(int callNum) {
		this.callNum = callNum;
	}


	public int getFeedNum() {
		return feedNum;
	}


	public void setFeedNum(int feedNum) {
		this.feedNum = feedNum;
	}

}
