package com.robot;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

import com.common.GameSocketService;
import com.common.RandomService;
import com.constant.ChatConstant;
import com.domain.MessageObj;
import com.domain.player.Player;
import com.domain.puppet.PuppetState;
import com.google.protobuf.InvalidProtocolBufferException;
import com.message.ChatProto.C_Chat;
import com.message.LoginProto.C_CreatePlayer;
import com.message.LoginProto.C_EnterComplete;
import com.message.LoginProto.C_EnterGame;
import com.message.LoginProto.C_LoginGame;
import com.message.LoginProto.LoginMsg;
import com.message.LoginProto.S_CreatePlayer;
import com.message.LoginProto.S_EnterGame;
import com.message.MessageProto.MessageEnum.MessageID;
import com.message.PlayerProto.PlayerMsg;
import com.message.SceneProto.C_EnterScene;
import com.message.SceneProto.C_GetSceneElementList;
import com.message.SceneProto.C_SynPosition;
import com.message.SceneProto.Vector3Msg;
import com.util.IDUtil;

public class RobotTest {

	private Channel channel = null;
	
	public RobotTest(Channel channel) {
		this.channel = channel;
	}
	
	public void mainTest(int userId) {
		login_c(userId);
	}
	
	private MessageObj setMessageObj(int msgID, byte[] data) {
		MessageObj obj = new MessageObj();
		obj.setMsgID(msgID);
		// 加密
		if (data != null) {
			obj.setData(GameSocketService.encryptForDis(data));
			initBuffer(obj);
		}
		return obj;
	}
	
	private void initBuffer(MessageObj obj) {

		byte[] packetBytes = new byte[obj.getData().length + 8];
		
		// 添加包长
		int length = obj.getData().length + 4;
		
		//int length = packetBytes.length;
		packetBytes[0] = (byte) (length >> 24);
		packetBytes[1] = (byte) (length >> 16);
		packetBytes[2] = (byte) (length >> 8);
		packetBytes[3] = (byte) (length);
		
		//添加消息编号
		packetBytes[4] = (byte) (obj.getMsgID() >> 24);
		packetBytes[5] = (byte) (obj.getMsgID() >> 16);
		packetBytes[6] = (byte) (obj.getMsgID() >> 8);
		packetBytes[7] = (byte) (obj.getMsgID());
		System.arraycopy(obj.getData(), 0, packetBytes, 8, obj.getData().length);
		
		// 1024*1024 = 1048576;
		//this.buf = Unpooled.copiedBuffer(packetBytes);
		ByteBuf buf = Unpooled.directBuffer(128,1048576).writeBytes(packetBytes);
		obj.setBuf(buf);
	}
	
	/** 登陆 */
	public void login_c(int userId) {
		
		C_LoginGame.Builder builder = C_LoginGame.newBuilder();
		builder.setUserId(userId);
		builder.setKey("DGAMEASDWDSHEN-16TQASDEDE-W33TT");
		builder.setServerNo(GameClient.serverNo);
		
		MessageObj msg = this.setMessageObj(MessageID.C_LoginGame_VALUE, builder.build().toByteArray());
		channel.writeAndFlush(msg.getBuf().retain());
		msg.getBuf().release();
	}

	/** 创建角色 */
	public void createPlayer() {
		
		C_CreatePlayer.Builder builder = C_CreatePlayer.newBuilder();
		
		builder.setCareer(RandomService.getRandomNum(1, 3));
		String random = String.valueOf(IDUtil.geneteId(Player.class));
		builder.setPlayerName("R"+random.substring(random.length() - 6));
		builder.setServerNo(GameClient.serverNo);
		
		//System.out.println(builder.getPlayerName()+" 准备登陆 ");
		
		MessageObj msg = this.setMessageObj(MessageID.C_CreatePlayer_VALUE, builder.build().toByteArray());
		channel.writeAndFlush(msg.getBuf().retain());
		msg.getBuf().release();
	}
	
	/** 创建角色返回 */
	public void s_createPlayer(MessageObj msg) {
		try {
			S_CreatePlayer builder = S_CreatePlayer.parseFrom(msg.getData());
			
			PlayerMsg playerMsg = builder.getPlayerMsg();
			
			long playerId = playerMsg.getPlayerId();
			
			this.enterGame(playerId);
		
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
	}
	
	/** 进入游戏*/
	public void enterGame(long playerId){
		C_EnterGame.Builder builder = C_EnterGame.newBuilder();
		builder.setPlayerId(playerId);
		MessageObj msg = this.setMessageObj(MessageID.C_EnterGame_VALUE, builder.build().toByteArray());
		channel.writeAndFlush(msg.getBuf().retain());
		msg.getBuf().release();
	}
	
	/** 进入游戏返回*/	
	public void s_enterGame(MessageObj msg){
		
		try {
			S_EnterGame builder = S_EnterGame.parseFrom(msg.getData());
			
			LoginMsg loginMsg = builder.getLoginMsg();
			
			int mapId = loginMsg.getMapId();
			
			this.enterScene(mapId);
		
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
	}
	
	/** 进入场景*/
	public void enterScene(int mapId){
		C_EnterScene.Builder builder = C_EnterScene.newBuilder();
		builder.setMapId(mapId);
		MessageObj msg = this.setMessageObj(MessageID.C_EnterScene_VALUE, builder.build().toByteArray());
		channel.writeAndFlush(msg.getBuf().retain());
		msg.getBuf().release();
	}
	
	
	/** 进入场景返回*/	
	public void s_enterScene(MessageObj msg){
		this.getSceneElementList();
	}
	
	/** 获取周边元素*/
	public void getSceneElementList(){
		C_GetSceneElementList.Builder builder = C_GetSceneElementList.newBuilder();
		MessageObj msg = this.setMessageObj(MessageID.C_GetSceneElementList_VALUE, builder.build().toByteArray());
		channel.writeAndFlush(msg.getBuf().retain());
		msg.getBuf().release();
	}
	
	/** 获取周边元素返回*/
	public void s_getSceneElementList(MessageObj msg){
		this.enterComplete();
	}
	
	/** 登录完成*/
	public void enterComplete(){
		C_EnterComplete.Builder builder = C_EnterComplete.newBuilder();
		MessageObj msg = this.setMessageObj(MessageID.C_EnterComplete_VALUE, builder.build().toByteArray());
		channel.writeAndFlush(msg.getBuf().retain());
		msg.getBuf().release();
	}
	
	/** 登录完成返回*/
	public void s_enterComplete(MessageObj msg){
	//	this.sendChat();
		
	//	this.synPosition();
	}
	
	/**
	 * 发送聊天
	 * */
	public void sendChat() {
		C_Chat.Builder builder = C_Chat.newBuilder();
		builder.setType(ChatConstant.CHAT_SYSTEM);
		builder.setContent("机器人， 欢迎欢迎！");
		MessageObj msg = this.setMessageObj(MessageID.C_Chat_VALUE, builder.build().toByteArray());
		channel.writeAndFlush(msg.getBuf().retain());
		msg.getBuf().release();
	}
	
	/** 走动*/
	public void synPosition(){
		C_SynPosition.Builder builder = C_SynPosition.newBuilder();
		
		
		String guid = "sk_0001_2_123";
		int state = PuppetState.MOVING.ordinal();
		
		Vector3Msg.Builder position = Vector3Msg.newBuilder();
		int newX = 7359;
		int newY = 174;
		int newZ = 8757;
		
		position.setX(newX);
		position.setY(newY);
		position.setZ(newZ);
		
		builder.setGuid(guid);
		builder.setState(state);
		builder.setPosition(position);
		
		MessageObj msg = this.setMessageObj(MessageID.C_SynPosition_VALUE, builder.build().toByteArray());
		channel.writeAndFlush(msg.getBuf().retain());
		msg.getBuf().release();
	}
}
