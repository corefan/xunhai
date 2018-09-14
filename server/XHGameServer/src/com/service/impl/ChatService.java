package com.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import com.cache.BaseCacheService;
import com.cache.CacheService;
import com.common.GameContext;
import com.common.GameSocketService;
import com.common.LockService;
import com.common.ServiceCollection;
import com.constant.CacheConstant;
import com.constant.ChatConstant;
import com.constant.ConfigConstant;
import com.constant.ExceptionConstant;
import com.constant.LockConstant;
import com.constant.TaskConstant;
import com.dao.chat.BaseNoticeDAO;
import com.domain.GameException;
import com.domain.MessageObj;
import com.domain.chat.BaseNotice;
import com.domain.chat.Notice;
import com.domain.chat.OfflineInfo;
import com.domain.chat.Voice;
import com.domain.family.PlayerFamily;
import com.domain.guild.Guild;
import com.domain.guild.PlayerGuild;
import com.domain.player.Player;
import com.domain.player.PlayerExt;
import com.domain.player.PlayerOptional;
import com.domain.player.PlayerProperty;
import com.domain.puppet.PlayerPuppet;
import com.domain.team.Team;
import com.domain.vip.PlayerVip;
import com.google.protobuf.ByteString;
import com.message.ChatProto.ChatInfoMsg;
import com.message.ChatProto.OfflineMsg;
import com.message.ChatProto.S_Chat;
import com.message.ChatProto.S_GetOfflineInfo;
import com.message.ChatProto.S_SynNotic;
import com.message.MessageProto.MessageEnum.MessageID;
import com.service.IChatService;
import com.service.IFamilyService;
import com.service.IFriendService;
import com.service.IGuildService;
import com.service.IPlayerService;
import com.service.IRewardService;
import com.service.ISceneService;
import com.service.ITaskService;
import com.service.ITeamService;
import com.service.IVipService;
import com.util.IDUtil;
import com.util.SplitStringUtil;


/**
 * 聊天系统
 * 
 * @author jiangqin
 * @date 2017-2-18
 */
public class ChatService implements IChatService {
		BaseNoticeDAO baseNoticeDAO = new BaseNoticeDAO();
		
		@Override
		public void initBaseCache() {
			Map<Integer, BaseNotice> baseNoticeMap = new HashMap<Integer, BaseNotice>();
			List<BaseNotice> listBaseNotice = baseNoticeDAO.listBaseNotice();
			for(BaseNotice baseNotice : listBaseNotice){							
				baseNoticeMap.put(baseNotice.getMsgId(), baseNotice);
			}	
			BaseCacheService.putToBaseCache(CacheConstant.BASE_NOTICE, baseNoticeMap);		
		}
		
		@SuppressWarnings({ "unchecked" })
		public BaseNotice getBaseNotice(int msgId){
			Map<Integer, BaseNotice> baseNoticeMap = (Map<Integer, BaseNotice>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_NOTICE);	
		
			return baseNoticeMap.get(msgId);
		}
	
		/**
		 * 世界聊天
		 *  
		 * @param content
		 * @param playerId
		 */
		@Override
		public void chat(long playerId, int msgId, String content, int type, long toPlayerId, String param) throws Exception{	
  			if(playerId < 1 || type < 1) throw new GameException(ExceptionConstant.ERROR_10000);
			
			ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();	
			GameSocketService gameSocketService = serviceCollection.getGameSocketService();
			IPlayerService playerService = serviceCollection.getPlayerService();
			IRewardService rewardService = serviceCollection.getRewardService();		
			IVipService vipService = serviceCollection.getVipService();
			IFamilyService familyService = serviceCollection.getFamilyService();
			ITaskService taskService = serviceCollection.getTaskService();
			IFriendService friendService = serviceCollection.getFriendService();
			IGuildService guildService = serviceCollection.getGuildService();
			
			if(content.trim().isEmpty() || content == null || content.equals("")) throw new GameException(ExceptionConstant.CHAT_2005);	

			// 消息内容过滤				
			if(content.length() > 192) throw new GameException(ExceptionConstant.CHAT_2002);
			if(content.length() <= 0) throw new GameException(ExceptionConstant.CHAT_2005);	
			
			PlayerProperty playerProperty = null;
			S_Chat.Builder builder = S_Chat.newBuilder();
			if(playerId > 0){
				// 任务触发
				taskService.executeTask(playerId, TaskConstant.TYPE_21, null);
				
				// 玩家信息
				Player player = playerService.getPlayerByID(playerId);			
				playerProperty = playerService.getPlayerPropertyById(playerId);				
				builder.setSendPlayerCareer(player.getCareer());				
				builder.setSendPlayerLevel(playerProperty.getLevel());
				builder.setSendPlayerName(player.getPlayerName());	
				
				PlayerVip playerVip = vipService.getPlayerVip(playerId);				
				if(playerVip != null && playerVip.getLevel() > 0){
					builder.setSendPlayerVip(playerVip.getLevel());
				}
			}
			
			builder.setMsgId(msgId);
			builder.setContent(content);
			builder.setType(type);		
			builder.setSendPlayerId(playerId);
			builder.setParam(param);
			if(toPlayerId > 0){
				builder.setToPlayerId(toPlayerId);
				
				Player toPlayer = playerService.getPlayerByID(toPlayerId);
				if(toPlayer == null) throw new GameException(ExceptionConstant.PLAYER_1115);
				builder.setToPlayerName(toPlayer.getPlayerName());
			}
			
			MessageObj msg = new MessageObj(MessageID.S_Chat_VALUE, builder.build().toByteArray());				
			if (type == ChatConstant.CHAT_WORLD){
				// 世界
				if(playerProperty.getLevel() < 10)throw new GameException(ExceptionConstant.CHAT_2000);
				
				gameSocketService.sendDataToAllOnline(msg);
			}else if (type == ChatConstant.CHAT_FAMILY){
				// 家族
				PlayerFamily playerFamily = familyService.getPlayerFamily(playerId);
				if(playerFamily.getPlayerFamilyId() < 1) throw new GameException(ExceptionConstant.FAMILY_2601);
				
				List<Long> playerIds = familyService.getFamilyPlayerIds(playerFamily.getPlayerFamilyId());
				MessageObj msgf = new MessageObj(MessageID.S_Chat_VALUE, builder.build().toByteArray());
				gameSocketService.sendDataToPlayerList(playerIds, msgf);				
			}else if (type == ChatConstant.CHAT_GUILD){
				// 帮派
				PlayerGuild playerGuild = guildService.getPlayerGuild(playerId);
				
				if(playerGuild.getGuildId() < 1) throw new GameException(ExceptionConstant.GUILD_3709);
				
				Guild guild = guildService.getGuildById(playerGuild.getGuildId());
				if(guild == null){
					throw new GameException(ExceptionConstant.GUILD_3706);	
				}
				
				List<Long> playerIds = new ArrayList<Long>();
				playerIds.addAll(guild.getPlayerIds());
				MessageObj msgf = new MessageObj(MessageID.S_Chat_VALUE, builder.build().toByteArray());
				gameSocketService.sendDataToPlayerList(playerIds, msgf);				
			}else if (type == ChatConstant.CHAT_TEAM){
				// 组队				
				PlayerExt playerExt = playerService.getPlayerExtById(playerId);
				if(playerExt.getTeamId() <= 0) throw new GameException(ExceptionConstant.TEAM_2301);
				
				ITeamService teamService = serviceCollection.getTeamService();
				Team team = teamService.getTeam(playerExt.getTeamId());
				if(team == null) throw new GameException(ExceptionConstant.TEAM_2301);
				
				List<Long> playerIds = teamService.getOnlineTeamPlayerIds(team);
				gameSocketService.sendDataToPlayerList(playerIds, msg);
			}else if(type == ChatConstant.CHAT_PRIVATE){	
				// 私聊
				if(toPlayerId <= 0) throw new GameException(ExceptionConstant.ERROR_10000);	
				
				// 判断玩家等级  (玩家等级限制需配置)
				if(playerProperty.getLevel() < 10)throw new GameException(ExceptionConstant.CHAT_2000);				
				PlayerProperty toPlayerProperty = playerService.getPlayerPropertyById(toPlayerId);				
				if(toPlayerProperty.getLevel() < 10)throw new GameException(ExceptionConstant.CHAT_2001);
				
				// 判断玩家和聊天对象是否为好友
				boolean isFriend = friendService.isFriend(playerId, toPlayerId);
				if(!isFriend){
					PlayerOptional toPlayerOptional = playerService.getPlayerOptionalById(toPlayerId);
					if(toPlayerOptional.getIsAcceptChat() == 1) throw new GameException(ExceptionConstant.CHAT_2007);
				}
				
				boolean isOnline = gameSocketService.checkOnLine(toPlayerId);
				if(!isFriend && !isOnline) throw new GameException(ExceptionConstant.CHAT_2009);
				
				// 任务触发
				taskService.executeTask(toPlayerId, TaskConstant.TYPE_21, null);
				gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
				
				// 检测聊天对象是否在线
				if(isOnline){
					MessageObj msg1 = new MessageObj(MessageID.S_Chat_VALUE, builder.build().toByteArray());	
					gameSocketService.sendDataToPlayerByPlayerId(toPlayerId, msg1);
				}else{
					
					// 接受者离线信息缓存
					 Map<Long, LinkedBlockingQueue<OfflineInfo>> offlineMap= this.getOfflineInfoMap(toPlayerId);
					
					// 获取发送者是否已有离线信息在接收者玩家的离线信息列表中
					LinkedBlockingQueue<OfflineInfo> offlineQueue = offlineMap.get(playerId);
					if(offlineQueue == null){
						offlineQueue = new LinkedBlockingQueue<OfflineInfo>();
						offlineMap.put(playerId, offlineQueue);
					}				
					
					
					OfflineInfo offlineInfo = null;
					if(offlineQueue.size() > ChatConstant.CHAT_OFFLINE_LIMIT){
						offlineInfo = offlineQueue.poll();
					}
					
					if(offlineInfo == null){
						offlineInfo = new OfflineInfo();
					}				
					
					offlineInfo.setId(IDUtil.geneteId(OfflineInfo.class));
					offlineInfo.setContent(content);
					offlineInfo.setParam(param);
					offlineInfo.setCreateTime(System.currentTimeMillis());
					
					offlineQueue.offer(offlineInfo);
				}				
				
			}else if (type == ChatConstant.CHAT_SYSTEM){			
				// 系统 所有人
				gameSocketService.sendDataToAllOnline(msg);	
			
				
			}else if (type == ChatConstant.CHAT_NEARBY){
				// 附近
				
				ISceneService sceneService = serviceCollection.getSceneService();
				PlayerPuppet playerPuppet = sceneService.getPlayerPuppet(playerId);
				if(playerPuppet !=null){
					List<Long>  playerIDList = sceneService.getNearbyPlayerIds(playerPuppet);				
					gameSocketService.sendDataToPlayerList(playerIDList, msg);
				}
				
			}else if (type == ChatConstant.CHAT_BIG_LOUDSPEAKER){
				// 喇叭
				int BIG_LOUD_ITEM = serviceCollection.getCommonService().getConfigValue(ConfigConstant.BIG_LOUD_ITEM);
				rewardService.deductItem(playerId, BIG_LOUD_ITEM, 1, true);
				MessageObj msgBigLoud = new MessageObj(MessageID.S_Chat_VALUE, builder.build().toByteArray());	
				gameSocketService.sendDataToAllOnline(msgBigLoud);
			}			
		}	
		
		@Override
		public void synNotice(int msgId, List<Notice> paramList, List<Long> playerIds){
			ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();	
			GameSocketService gameSocketService = serviceCollection.getGameSocketService();
			
			S_SynNotic.Builder builder = S_SynNotic.newBuilder();
			builder.setMsgId(msgId);
			builder.setParam(SplitStringUtil.getStringByNoticeList(paramList));		
		
			MessageObj msg = new MessageObj(MessageID.S_SynNotic_VALUE, builder.build().toByteArray());	
			gameSocketService.sendDataToPlayerList(playerIds, msg);
		}		
		
		@Override
		public void setIsAcceptChat(long playerId, int state) {
			IPlayerService playerService = GameContext.getInstance().getServiceCollection().getPlayerService();
			PlayerOptional playerOptional = playerService.getPlayerOptionalById(playerId);
			
			playerOptional.setIsAcceptChat(state);	
			playerService.updatePlayerOptional(playerOptional);		
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public void postVoice(String id, ByteString voice){		
			
 			LinkedBlockingQueue<Voice> voiceQueue= (LinkedBlockingQueue<Voice>)CacheService.getFromCache(CacheConstant.VOICE);
			if(voiceQueue == null){
				voiceQueue = new LinkedBlockingQueue<Voice>(ChatConstant.CHAT_VOICE_LIMIT);
				CacheService.putToCache(CacheConstant.VOICE, voiceQueue);
			}
			
			Voice vo = null;
			if(voiceQueue.size() >= ChatConstant.CHAT_VOICE_LIMIT){
				// 排序,删除最早的语音
				vo = voiceQueue.poll();
			}
			
			if(vo == null){
				vo = new Voice();
			}
			
			vo.setId(id);
			vo.setVoice(voice);			
			voiceQueue.offer(vo);		
		}

		@SuppressWarnings("unchecked")
		@Override
		public Voice getVoice(String id){
			LinkedBlockingQueue<Voice> voiceQueue= (LinkedBlockingQueue<Voice>)CacheService.getFromCache(CacheConstant.VOICE);
			if(voiceQueue == null) return null;
			
			Iterator<Voice> iterator = voiceQueue.iterator();
			while(iterator.hasNext()){
				Voice voice = iterator.next();				
				
				if(voice.getId().equals(id)){
					return voice;
				}
			}
			
			return null;
		}

		@Override
		public void getOfflineInfo(long playerId) {
			if(playerId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
			ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();	
			GameSocketService gameSocketService = serviceCollection.getGameSocketService();
					
			synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_CHAT)) {
				
				S_GetOfflineInfo.Builder builder = S_GetOfflineInfo.newBuilder();
				
				Map<Long, LinkedBlockingQueue<OfflineInfo>> offlineMap = this.getOfflineInfoMap(playerId);				
				for(Map.Entry<Long, LinkedBlockingQueue<OfflineInfo>> entry : offlineMap.entrySet()){
					 LinkedBlockingQueue<OfflineInfo> offlineQueue = entry.getValue();
					 
					 for(OfflineInfo offlineInfo : offlineQueue){
						 builder.addOfflines(buildOfflineMsg(entry.getKey(), offlineInfo));
					 }			
				}
				
				MessageObj msg = new MessageObj(MessageID.S_GetOfflineInfo_VALUE, builder.build().toByteArray());
				gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
				
				offlineMap.clear();
			}			
		}	
		
		/** 获取玩家离线离线信息*/
		@SuppressWarnings("unchecked")
		private Map<Long, LinkedBlockingQueue<OfflineInfo>> getOfflineInfoMap(long playerId){
			
			Map<Long, LinkedBlockingQueue<OfflineInfo>> offlineMap = (Map<Long, LinkedBlockingQueue<OfflineInfo>>)CacheService.getFromCache(CacheConstant.PLAYER_OFFLINE_INFO + playerId);
			if(offlineMap == null){
				offlineMap = new ConcurrentHashMap<Long, LinkedBlockingQueue<OfflineInfo>>();
				CacheService.putToCache(CacheConstant.PLAYER_OFFLINE_INFO + playerId, offlineMap);
			}
			
			return offlineMap;
		}
		
		
		private OfflineMsg.Builder buildOfflineMsg(long sendPlayerId, OfflineInfo offlineInfo){
			ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();	
			IPlayerService playerService = serviceCollection.getPlayerService();
			IVipService vipService = serviceCollection.getVipService();
			
			Player player = playerService.getPlayerByID(sendPlayerId);
			PlayerProperty playerProperty = playerService.getPlayerPropertyById(sendPlayerId);
			
			OfflineMsg.Builder builder = OfflineMsg.newBuilder();
			builder.setSendPlayerId(sendPlayerId);
			builder.setSendPlayerCareer(player.getCareer());
			builder.setSendPlayerLevel(playerProperty.getLevel());
			builder.setSendPlayerName(player.getPlayerName());
			
			PlayerVip playerVip = vipService.getPlayerVip(sendPlayerId);				
			if(playerVip != null && playerVip.getLevel() > 0){
				builder.setSendPlayerVip(playerVip.getLevel());
			}
		
			builder.addChatInfos(buildChatInfoMsg(offlineInfo));
			
			return builder;
		}
		
		private ChatInfoMsg.Builder buildChatInfoMsg(OfflineInfo offlineInfo){
			
			ChatInfoMsg.Builder msg = ChatInfoMsg.newBuilder();			
			msg.setCerateTime(offlineInfo.getCreateTime());
			msg.setContent(offlineInfo.getContent());
			msg.setParam(offlineInfo.getParam());
			
			return msg;
		}
	}
