package com.dao.mail;

import java.util.List;

import com.db.GameSqlSessionTemplate;
import com.domain.mail.MailInbox;

/**
 * 收件箱DAO
 * @author ken
 * @date 2017-2-14
 */
public class MailInboxDAO extends GameSqlSessionTemplate {
	

	public void createMailInBox(MailInbox mailInbox) {
		this.insert_noreturn(mailInbox.getInsertSql());
	}
	
	/** 
	 * 全服创建邮件（带附件）
	 * 注：7天未上线不发
	 * */
	public void createMailBox(String theme, String content, String attachment){
		
//		String sql = "insert into mail_inbox(mailInboxID, mailType, senderID, senderName, receiverID, theme, content, haveAttachment, haveReceiveAttachment, attachment, state, receiveTime, remainDays, fromType, deleteFlag, deleteTime) " +
//				"SELECT "+ IDUtil.geneteId(MailInbox.class)+", 1,p.playerId,'"+MailConstant.SYSTEM_MAIL_SENDER_NAME+"',p.playerId,'" + theme +"','" +content + "',1,0,'" +attachment + "',0,now(),10,27,0 " +
//				"FROM player p, player_ext pe WHERE p.deleteFlag = 0 and p.player_id = pe.player_id and DATE_SUB(NOW(),INTERVAL 7 DAY) < exitTime";
//		
//		this.insert(sql);		

	}
	
	/** 
	 * 全服创建邮件（不带附件）
	 * 注：5天未上线不发
	 * */
	public void batchCreateMailBox(List<String> param){
		
//		String sql = "insert into t_mail_inbox(MAIL_TYPE,SENDER_ID,SENDER_NAME,RECEIVER_ID,THEME,CONTENT,HAVE_ATTACHMENT," +
//				"HAVE_RECEIVE_ATTACHMENT,ATTACHMENT,STATE,RECEIVE_TIME,REMAIN_DAYS,FROM_TYPE,DELETE_FLAG) " +
//				"SELECT 1,p.PLAYER_ID,'"+ResourceUtil.getValue("mail_1")+"',p.PLAYER_ID,'" + param.get(0) +"','" +param.get(1) + "',0,0,'" +param.get(2) + "',0,now(),10,27,0 " +
//				"FROM T_PLAYER p, t_player_ext pe WHERE p.TYPE !=5 and p.player_id = pe.player_id and DATE_SUB(NOW(),INTERVAL 5 DAY) < EXIT_TIME";
//		this.insert(sql);		

	}
	
	/**
	 * 调度删除已删除的邮件
	 * */
	public void quartzDeleteMailInbox() {
		this.delete("DELETE FROM mail_inbox WHERE deleteFlag = 1");
	}
	
	/**
	 * 调度删除邮件
	 * */
	public void quartzDeleteMailInBox_ext() {
		
		// 删除7天前创建的没附件的邮件
		String sql3 = "DELETE FROM mail_inbox WHERE haveAttachment = 0 AND DATE_SUB(NOW(),INTERVAL 7 DAY) > receiveTime";
		this.delete(sql3);
		
		// 一周未登陆,删除3天前创建的没附件的邮件
		String sql1 = "DELETE FROM mail_inbox WHERE haveAttachment = 0 AND DATE_SUB(NOW(),INTERVAL 3 DAY) > receiveTime AND receiverID IN (SELECT playerId FROM player_ext WHERE DATE_SUB(NOW(),INTERVAL 7 DAY) > exitTime)";
		this.delete(sql1);
		
		// 一个月未登陆,删除7天前创建的邮件
		String sql2 = "DELETE FROM mail_inbox WHERE DATE_SUB(NOW(),INTERVAL 7 DAY) > receiveTime AND receiverID IN (SELECT playerId FROM player_ext WHERE DATE_SUB(NOW(),INTERVAL 30 DAY) > exitTime)";
		this.delete(sql2);
		
	}
	
	/**
	 * 获取玩家邮件
	 */
	public List<MailInbox> getMailInboxListByplayerId(long playerId) {
		String sql = "SELECT * FROM mail_inbox WHERE receiverID="+playerId+" AND deleteFlag=0 ORDER BY receiveTime DESC LIMIT 30";
		
		return this.selectList(sql, MailInbox.class);
	}

}
