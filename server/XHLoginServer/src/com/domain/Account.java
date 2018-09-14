package com.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.util.SplitStringUtil;

/**
 * 账号
 * @author ken
 * @date 2017-6-22
 */
public class Account extends GameEntity {

	private static final long serialVersionUID = 4210277908633691522L;
	
	/** 唯一编号*/
	private long userId;
	/** 账号*/
	private String userName;
	/** 密码*/
	private String passWord;
	/** 电话*/
	private String telephone;
	/** 是否游客  1：是*/
	private int tourist;
	/** 已创号的服务器列表*/
	private  List<Integer> serverList = new ArrayList<Integer>();	
	private String serverListStr;
	/** 真实姓名*/
	private String realName;
	/** 身份证ID*/
	private String identity;
	/** 创建时间*/
	private Date createTime;
	/** 更新时间*/
	private Date updateTime;
	
	public String getInsertSql() {
		
		StringBuilder sql = new StringBuilder(1 << 8);
		
		sql.append("INSERT INTO account ");
		sql.append("(userId, userName, passWord, telephone, tourist, serverListStr, createTime, updateTime) VALUES");
		sql.append(" (");
		sql.append(userId);
		sql.append(",");
		sql.append("'");
		sql.append(userName);
		sql.append("'");
		sql.append(",");
		if(passWord == null){
			sql.append(passWord);
		}else{
			sql.append("'");
			sql.append(passWord);
			sql.append("'");
		}
		sql.append(",");
		if(telephone == null){
			sql.append(telephone);
		}else{
			sql.append("'");
			sql.append(telephone);
			sql.append("'");
		}
		sql.append(",");
		sql.append(tourist);
		sql.append(",");
		if (serverListStr == null) {
			sql.append(serverListStr);
		} else {
			sql.append("'");
			sql.append(serverListStr);
			sql.append("'");
		}
		sql.append(",");
		if (createTime == null) {
			sql.append(createTime);
		} else {
			sql.append("'");
			sql.append(new Timestamp(createTime.getTime()));
			sql.append("'");
		}
		sql.append(",");
		if (updateTime == null) {
			sql.append(updateTime);
		} else {
			sql.append("'");
			sql.append(new Timestamp(updateTime.getTime()));
			sql.append("'");
		}
		sql.append(")");
		
		return sql.toString();
	}
	
	/**
	 * 获得更新sql
	 * */
	public String getUpdateSql() {
		
		StringBuilder sql = new StringBuilder(1 << 8);			
		sql.append("UPDATE account SET ");
		sql.append(" userName=");
		sql.append("'");
		sql.append(userName);
		sql.append("'");
		sql.append(",");
		sql.append(" passWord=");
		if(passWord == null){
			sql.append(passWord);
		}else{
			sql.append("'");
			sql.append(passWord);
			sql.append("'");
		}
		sql.append(",");	
		sql.append(" telephone=");
		if(telephone == null){
			sql.append(telephone);
		}else{
			sql.append("'");
			sql.append(telephone);
			sql.append("'");
		}
		sql.append(",");			
		sql.append(" tourist=");
		sql.append(tourist);
		sql.append(",");	
		sql.append(" serverListStr=");
		if(serverListStr == null){
			sql.append(serverListStr);
		}else{
			sql.append("'");
			sql.append(serverListStr);
			sql.append("'");
		}
		sql.append(",");
		sql.append(" realName=");
		if(realName == null){
			sql.append(realName);
		}else{
			sql.append("'");
			sql.append(realName);
			sql.append("'");
		}
		sql.append(",");
		sql.append(" identity=");
		if(identity == null){
			sql.append(identity);
		}else{
			sql.append("'");
			sql.append(identity);
			sql.append("'");
		}
		sql.append(",");			
		sql.append(" updateTime = ");
		if(updateTime == null){
			sql.append(updateTime);
		}else{
			sql.append("'");
			sql.append(new java.sql.Timestamp(updateTime.getTime()));
			sql.append("'");
		}
		sql.append(" WHERE userId=");
		sql.append(userId);
		
		return sql.toString();
	}


	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public int getTourist() {
		return tourist;
	}

	public void setTourist(int tourist) {
		this.tourist = tourist;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public List<Integer> getServerList() {
		return serverList;
	}

	public void setServerList(List<Integer> serverList) {
		this.serverList = serverList;
		
		this.serverListStr = serverList.toString();
	}

	public String getServerListStr() {
		return serverListStr;
	}

	public void setServerListStr(String serverListStr) {
		this.serverListStr = serverListStr;
		
		List<Integer> list = SplitStringUtil.getIntList(serverListStr);
		if(list != null){
			this.serverList = list;
		}else{
			this.serverList.clear();
		}
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	} 
	
}
