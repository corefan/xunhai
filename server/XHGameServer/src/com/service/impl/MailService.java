package com.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.cache.CacheService;
import com.cache.CacheSynDBService;
import com.common.DateService;
import com.common.GameContext;
import com.common.GameSocketService;
import com.common.LockService;
import com.common.ServiceCollection;
import com.constant.CacheConstant;
import com.constant.CacheSynConstant;
import com.constant.ConfigConstant;
import com.constant.ExceptionConstant;
import com.constant.LockConstant;
import com.constant.MailConstant;
import com.dao.mail.MailInboxDAO;
import com.domain.GameEntity;
import com.domain.GameException;
import com.domain.MessageObj;
import com.domain.Reward;
import com.domain.mail.MailInbox;
import com.domain.player.PlayerExt;
import com.message.MailProto.S_DeleteMail;
import com.message.MailProto.S_NewMail;
import com.message.MessageProto.MessageEnum.MessageID;
import com.service.IGMService;
import com.service.IMailService;
import com.service.IRewardService;
import com.util.IDUtil;
import com.util.SplitStringUtil;

/**
 * 邮件系统
 * @author ken
 * @date 2017-2-14
 */
public class MailService implements IMailService {

	private MailInboxDAO mailInboxDAO = new MailInboxDAO();
	
	@Override
	public void initMailCache() {
		CacheService.putToCache(CacheConstant.MAIL_INBOX, new ConcurrentHashMap<Integer, List<MailInbox>>());
		
		CacheService.putToCache(CacheConstant.SERVER_MAIL_INBOX, new ConcurrentHashMap<Long, List<List<String>>>());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void removeMailCache(long playerId) {
		Map<Long, List<MailInbox>> mailInboxMapCache = (Map<Long, List<MailInbox>>)CacheService.getFromCache(CacheConstant.MAIL_INBOX);
		mailInboxMapCache.remove(playerId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MailInbox> getPlayerMailInboxList(long playerId) {
		Map<Long, List<MailInbox>> mailInboxMapCache = (Map<Long, List<MailInbox>>)CacheService.getFromCache(CacheConstant.MAIL_INBOX);
		List<MailInbox> mailInboxList = mailInboxMapCache.get(playerId);
		if(mailInboxList == null){
			mailInboxList = mailInboxDAO.getMailInboxListByplayerId(playerId);
			mailInboxMapCache.put(playerId, mailInboxList);
		}
		return mailInboxList;
	}
	
	@Override
	public void deleteMailInboxByID(long playerId, long mailInboxID)throws Exception{
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.MAIL_INBOX)) {
			MailInbox mailInbox = this.getMailInboxByID(playerId, mailInboxID);
			if(mailInbox == null) throw new GameException(ExceptionConstant.MAIL_1900);
			
			if(mailInbox.getDeleteFlag() == 1) throw new GameException(ExceptionConstant.MAIL_1904);
			
			mailInbox.setDeleteFlag(1);
			mailInbox.setDeleteTime(DateService.getCurrentUtilDate());
			this.updateMailInbox(mailInbox);
			
			this.getPlayerMailInboxList(playerId).remove(mailInbox);
		}

	}

	@Override
	public List<MailInbox> getMailInboxPageListByplayerId(long playerId, int start, int offset){
		List<MailInbox> lists = this.getPlayerMailInboxList(playerId);
		
		if(start <= lists.size()) {
			int fromIndex = start - 1;
			if(fromIndex < 0) fromIndex=0;
			int toIndex = fromIndex + offset;
			if(toIndex > lists.size()) toIndex = lists.size();
			
			return lists.subList(fromIndex, toIndex);
		}
		
		return new ArrayList<MailInbox>();
	}

	/**
	 * 根据收件箱编号得到收件
	 */
	private MailInbox getMailInboxByID(long playerId, long mailInboxID){
		List<MailInbox> mailInboxList = this.getPlayerMailInboxList(playerId);
		
		for(MailInbox mailInbox : mailInboxList){
			if(mailInbox.getMailInboxID() == mailInboxID){
				return mailInbox;
			}
		}
		
		return null;
		
	}

	@Override
	public MailInbox readInboxMail(long playerId, long mailInboxID) throws Exception{
		if(playerId < 1 || mailInboxID < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.MAIL_INBOX)) {
			MailInbox mailInbox = this.getMailInboxByID(playerId, mailInboxID);
			if(mailInbox == null) throw new GameException(ExceptionConstant.MAIL_1900);
			
			if(mailInbox.getState() == MailConstant.MAIL_STATE_READED) throw new GameException(ExceptionConstant.MAIL_1901);
			
			mailInbox.setState(MailConstant.MAIL_STATE_READED);
			this.updateMailInbox(mailInbox);
			return mailInbox;
		}

	}

	@Override
	public void receiveAttachment(long playerId, long mailInboxID) throws Exception{
		if(playerId < 1 || mailInboxID < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		IRewardService rewardService = GameContext.getInstance().getServiceCollection().getRewardService();
		MailInbox mailInbox = this.getMailInboxByID(playerId, mailInboxID);
		if(mailInbox == null) throw new GameException(ExceptionConstant.MAIL_1900);
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.MAIL_INBOX)) {
			if(mailInbox.getHaveAttachment() == 0) throw new GameException(ExceptionConstant.MAIL_1902);
			
			if(mailInbox.getHaveReceiveAttachment()==MailConstant.HAD_RECEIVE_ATTACHMENT){
				throw new GameException(ExceptionConstant.MAIL_1903);
			}
			List<Reward> rewardList = SplitStringUtil.getRewardInfo(mailInbox.getAttachment());
			if(rewardList == null) throw new GameException(ExceptionConstant.MAIL_1902);
			
			//获取附件
			rewardService.fetchRewardList(mailInbox.getReceiverID(), rewardList);
			
			if(mailInbox.getState() == MailConstant.MAIL_STATE_NEW){
				mailInbox.setState(MailConstant.MAIL_STATE_READED);
			}
			mailInbox.setHaveReceiveAttachment(MailConstant.HAD_RECEIVE_ATTACHMENT);
			
			this.updateMailInbox(mailInbox);
		}
	}
	
	@Override
	public void systemSendMail(long receiverID, String theme, String content, String attachment, int fromType){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		
		synchronized (LockService.getPlayerLockByType(receiverID, LockConstant.MAIL_INBOX)) {
			MailInbox mailInbox = this.createMail(receiverID, theme, content, attachment, new Date(), fromType);
			
			//推送新邮件通知 
			if (gameSocketService.checkOnLine(receiverID)) {

				S_NewMail.Builder builder = S_NewMail.newBuilder();
				builder.setMailInboxID(mailInbox.getMailInboxID());
				
				MessageObj msg = new MessageObj(MessageID.S_NewMail_VALUE, builder.build().toByteArray());
				gameSocketService.sendDataToPlayerByPlayerId(receiverID, msg);
			}
		}


	}

	/**
	 * 创建一封邮件
	 */
	private MailInbox createMail(long receiverID, String theme, String content, String attachment, Date receiveTime, int fromType){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		
		MailInbox mailInbox = new MailInbox();
		mailInbox.setMailInboxID(IDUtil.geneteId(MailInbox.class));
		mailInbox.setMailType(MailConstant.MAIL_TYPE_SYSTEM);
		mailInbox.setSenderID(receiverID);
		mailInbox.setSenderName(MailConstant.SYSTEM_MAIL_SENDER_NAME);
		mailInbox.setReceiverID(receiverID);
		mailInbox.setTheme(theme);
		mailInbox.setContent(content);
		if(attachment != null && !attachment.trim().equals("")) {
			mailInbox.setHaveAttachment(MailConstant.HAVE_ATTACHMENT);
			mailInbox.setAttachment(attachment);
		} else {
			mailInbox.setHaveAttachment(MailConstant.NOT_HAVE_ATTACHMENT);
		}
		mailInbox.setHaveReceiveAttachment(MailConstant.NOT_RECEIVE_ATTACHMENT);
		mailInbox.setState(MailConstant.MAIL_STATE_NEW);
		mailInbox.setReceiveTime(new Date());
		mailInbox.setFromType(fromType);
		mailInboxDAO.createMailInBox(mailInbox);
		
		List<MailInbox> lists = this.getPlayerMailInboxList(mailInbox.getReceiverID());
		lists.add(mailInbox);
		
		int MAX_MAIL_NUM = serviceCollection.getCommonService().getConfigValue(ConfigConstant.MAX_MAIL_NUM);
		if(lists.size() > MAX_MAIL_NUM){
			MailInbox model = null;
			for(MailInbox m : lists){
				if(m.getHaveAttachment() == MailConstant.NOT_HAVE_ATTACHMENT || 
						(m.getHaveAttachment() == MailConstant.HAVE_ATTACHMENT && 
						m.getHaveReceiveAttachment() == MailConstant.HAD_RECEIVE_ATTACHMENT)){
					model = m;
					break;
				}
			}
			
			if(model == null){
				model = lists.remove(0);
			}else{
				lists.remove(model);
			}
			
			model.setDeleteFlag(1);
			model.setDeleteTime(DateService.getCurrentUtilDate());
			this.updateMailInbox(model);
			
			S_DeleteMail.Builder builder = S_DeleteMail.newBuilder();
			builder.setMailInboxID(model.getMailInboxID());
			
			MessageObj msg = new MessageObj(MessageID.S_DeleteMail_VALUE, builder.build().toByteArray());
			serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(receiverID, msg);
		}
		return mailInbox;
	}

	/**
	 * 根据状态获得玩家收件列表
	 */
	@Override
	public List<MailInbox> getPlayerMailInboxByState(long playerId, int state) {
		
		List<MailInbox> playerMailInboxList = this.getPlayerMailInboxList(playerId);
		
		List<MailInbox> returnList = new ArrayList<MailInbox>();
		
		for(MailInbox mailInbox : playerMailInboxList){
			if(mailInbox.getState() == state){
				returnList.add(mailInbox);
			}
		}
		
		return returnList;
		
	}
	
	/**
	 * 更新玩家收件
	 */
	private void updateMailInbox(MailInbox mailInbox) {
		Set<GameEntity> lists =  CacheSynDBService.getFromFiveUpdateThreeCache(CacheSynConstant.MAIL_INBOX);
		if (!lists.contains(mailInbox)) {
			lists.add(mailInbox);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public int isHaveNewMail(long playerId) {
		int isHaveMail = 0;
		
		List<MailInbox> playerMailInboxList = this.getPlayerMailInboxList(playerId);
		for(MailInbox mailInbox : playerMailInboxList){
			if(mailInbox.getState() == MailConstant.MAIL_STATE_NEW){
				isHaveMail = 1;
				break;
			}
		}
		
		//获取全服邮件
		Map<Long, List<List<String>>> serverMailMap = (Map<Long, List<List<String>>>)CacheService.getFromCache(CacheConstant.SERVER_MAIL_INBOX);
		
		List<List<String>> lists = serverMailMap.get(playerId);
		if(lists != null){
			for(List<String> l : lists){
				long sendTime = Long.parseLong(l.get(3));
				this.createMail(playerId, l.get(0), l.get(1), l.get(2), new Date(sendTime), 0);
				
				isHaveMail = 1;
			}
			serverMailMap.remove(playerId);
		}
		return isHaveMail;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void sendItemToAll(String theme, String content, String attachment) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		
		IGMService gmService = serviceCollection.getGmService();
		List<PlayerExt> playerExts = gmService.listPlayerIdByExitTime(7);
		
		List<String> mailStr = new ArrayList<String>();
		mailStr.add(theme);
		mailStr.add(content);
		mailStr.add(attachment);
		mailStr.add(String.valueOf(System.currentTimeMillis()));
		
		Map<Long, List<List<String>>> serverMailMap = (Map<Long, List<List<String>>>)CacheService.getFromCache(CacheConstant.SERVER_MAIL_INBOX);
		for(PlayerExt pe : playerExts){
			boolean online = gameSocketService.checkOnLine(pe.getPlayerId());
			if(online){
				this.systemSendMail(pe.getPlayerId(), theme, content, attachment, 0);
			}else{
				List<List<String>> list = serverMailMap.get(pe.getPlayerId());
				if(list == null){
					list = new ArrayList<List<String>>();
					serverMailMap.put(pe.getPlayerId(), list);
				}
				list.add(mailStr);
			}
		}
		
		//mailInboxDAO.createMailBox(theme, content, attachment);
		
	}

	@Override
	public void batchCreateMailInBox(List<String> param) {
		mailInboxDAO.batchCreateMailBox(param);
	}
	
	public void quartzDeleteMailInbox() {
		mailInboxDAO.quartzDeleteMailInbox();
		//mailInboxDAO.quartzDeleteMailInBox_ext();
		//initMailCache();
	}

}
