package com.domain.mail;

import java.sql.Timestamp;
import java.util.Date;

import com.domain.GameEntity;

/**
 * 收件箱
 * @author ken
 * @date 2017-2-14
 */
public class MailInbox extends GameEntity  {

	private static final long serialVersionUID = 7351760004411753733L;
	
	/** 收件箱编号 */
	private long mailInboxID;
	/** 邮件类型(1:系统2:玩家) */
	private int mailType;
	/** 发送玩家编号 */
	private long senderID;
	/** 发送玩家名称 */
	private String senderName;
	/** 接收玩家编号 */
	private long receiverID;
	/** 邮件主题 */
	private String theme;
	/** 邮件内容 */
	private String content;
	/** 是否含有附件 */
	private int haveAttachment;
	/** 是否领取附件 */
	private int haveReceiveAttachment;
	/** 附件(类型:编号:数量) */
	private String attachment;
	/** 状态(0.未读 1.已读 2.保存) */
	private int state;
	/** 邮件接收时间 */
	private Date receiveTime;
	/** 剩余天数 */
	private int remainDays;
	/** 来源类型(区分不同操作) */
	private int fromType;
	/** 是否已删除(1.已删 0.未删) */
	private int deleteFlag;
	/** 删除时间 */
	private Date deleteTime;
	
	public String getInsertSql() {
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO mail_inbox(mailInboxID, mailType, senderID, senderName, receiverID, theme, content, haveAttachment, haveReceiveAttachment, attachment, state, receiveTime, remainDays, fromType, deleteFlag, deleteTime)");
		sql.append(" VALUES ");
		sql.append("(");
		sql.append(mailInboxID);
		sql.append(",");
		sql.append(mailType);
		sql.append(",");
		sql.append(senderID);
		sql.append(",");
		if(senderName == null){
			sql.append(senderName);
		}else{
			sql.append("'");
			sql.append(senderName);
			sql.append("'");
		}
		sql.append(",");
		sql.append(receiverID);
		sql.append(",");
		if(theme == null){
			sql.append(theme);
		}else{
			sql.append("'");
			sql.append(theme);
			sql.append("'");
		}
		sql.append(",");
		if(content == null){
			sql.append(content);
		}else{
			sql.append("'");
			sql.append(content);
			sql.append("'");
		}
		sql.append(",");
		sql.append(haveAttachment);
		sql.append(",");
		sql.append(haveReceiveAttachment);
		sql.append(",");
		if(attachment == null){
			sql.append(attachment);
		}else{
			sql.append("'");
			sql.append(attachment);
			sql.append("'");
		}
		sql.append(",");
		sql.append(state);
		sql.append(",");
		if(receiveTime == null){
			sql.append(receiveTime);
		}else{
			sql.append("'");
			sql.append(new Timestamp(receiveTime.getTime()));
			sql.append("'");
		}
		sql.append(",");
		sql.append(remainDays);
		sql.append(",");
		sql.append(fromType);
		sql.append(",");
		sql.append(deleteFlag);
		sql.append(",");
		if (deleteTime == null) {
			sql.append(deleteTime);
		} else {
			sql.append("'");
			sql.append(new Timestamp(deleteTime.getTime()));
			sql.append("'");
		}
		sql.append(")");
		
		return sql.toString();
	}
	
	public String getUpdateSql() {
		StringBuilder sql = new StringBuilder(1 << 9);
		
		sql.append("UPDATE mail_inbox SET ");
		sql.append("mailType = ");
		sql.append(mailType);
		sql.append(",");
		sql.append("senderID = ");
		sql.append(senderID);
		sql.append(",");
		sql.append("senderName = ");
		if(senderName == null){
			sql.append(senderName);
		}else{
			sql.append("'");
			sql.append(senderName);
			sql.append("'");
		}
		sql.append(",");
		sql.append("receiverID = ");
		sql.append(receiverID);
		sql.append(",");
		sql.append("theme = ");
		if(theme == null){
			sql.append(theme);
		}else{
			sql.append("'");
			sql.append(theme);
			sql.append("'");
		}
		sql.append(",");
		sql.append("content = ");
		if(content == null){
			sql.append(content);
		}else{
			sql.append("'");
			sql.append(content);
			sql.append("'");
		}
		sql.append(",");
		sql.append("haveAttachment = ");
		sql.append(haveAttachment);
		sql.append(",");
		sql.append("haveReceiveAttachment = ");
		sql.append(haveReceiveAttachment);
		sql.append(",");
		sql.append("attachment = ");
		if(attachment == null){
			sql.append(attachment);
		}else{
			sql.append("'");
			sql.append(attachment);
			sql.append("'");
		}
		sql.append(",");
		sql.append("state = ");
		sql.append(state);
		sql.append(",");
		sql.append("receiveTime = ");
		if(receiveTime == null){
			sql.append(receiveTime);
		}else{
			sql.append("'");
			sql.append(new java.sql.Timestamp(receiveTime.getTime()));
			sql.append("'");
		}
		sql.append(",");
		sql.append("remainDays = ");
		sql.append(remainDays);
		sql.append(",");
		sql.append("deleteFlag = ");
		sql.append(deleteFlag);
		sql.append(",");
		sql.append(" deleteTime = ");
		if (deleteTime == null) {
			sql.append(deleteTime);
		} else {
			sql.append("'");
			sql.append(new Timestamp(deleteTime.getTime()));
			sql.append("'");
		}
		
		sql.append(" WHERE mailInboxID = ");
		sql.append(mailInboxID);
		
		return sql.toString();
	}


	public long getMailInboxID() {
		return mailInboxID;
	}

	public void setMailInboxID(long mailInboxID) {
		this.mailInboxID = mailInboxID;
	}

	public int getMailType() {
		return mailType;
	}

	public void setMailType(int mailType) {
		this.mailType = mailType;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public long getSenderID() {
		return senderID;
	}

	public void setSenderID(long senderID) {
		this.senderID = senderID;
	}

	public long getReceiverID() {
		return receiverID;
	}

	public void setReceiverID(long receiverID) {
		this.receiverID = receiverID;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getHaveAttachment() {
		return haveAttachment;
	}

	public void setHaveAttachment(int haveAttachment) {
		this.haveAttachment = haveAttachment;
	}

	public int getHaveReceiveAttachment() {
		return haveReceiveAttachment;
	}

	public void setHaveReceiveAttachment(int haveReceiveAttachment) {
		this.haveReceiveAttachment = haveReceiveAttachment;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Date getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}

	public int getRemainDays() {
		return remainDays;
	}

	public void setRemainDays(int remainDays) {
		this.remainDays = remainDays;
	}

	public int getFromType() {
		return fromType;
	}

	public void setFromType(int fromType) {
		this.fromType = fromType;
	}

	public int getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(int deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public Date getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}
	
}
