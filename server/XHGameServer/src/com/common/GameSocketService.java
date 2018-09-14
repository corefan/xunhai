package com.common;

import java.nio.BufferOverflowException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.cache.CacheService;
import com.constant.CacheConstant;
import com.core.Connection;
import com.core.Connection.ConnectionState;
import com.domain.MessageObj;
import com.util.LogUtil;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.ImmediateEventExecutor;

/**
 * 2013-10-30
 */
public class GameSocketService {

	public static final String KEY = "Glv8PzQQ4fa8P2017";

	public static final byte[] KEY_BYTE = KEY.getBytes();
	
	/**
	 * 进入游戏后连接缓存
	 * */
	public void initChannelCache() {
		CacheService.putToCache(CacheConstant.PLAYER_CONNECTION_MAP, new ConcurrentHashMap<Integer, Connection>());
	}

	/**
	 * 获得玩家连接Map缓存
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<Long, Connection> getPlayerConnectionMapCache(){
		return (Map<Long, Connection>) CacheService.getFromCache(CacheConstant.PLAYER_CONNECTION_MAP);

	}

	/**
	 * 添加玩家连接
	 */
	public void addConnectionCache(long userId, Connection connection) {
		if (userId == 0 || connection == null) {
			return;
		}

		connection.setUserId(userId);
		connection.setState(ConnectionState.LOGIN);

		Map<Long, Connection> playerConnectionMap = getPlayerConnectionMapCache();
		playerConnectionMap.put(userId, connection);

	}
	
	/**
	 * 更新连接状态
	 * @param userId
	 * @param state
	 */
	public void updateConnectionState(int userId, ConnectionState state){
		Connection conn = getConnection(userId);
		if(conn == null) return;
		
		conn.setState(state);
	}

	/**
	 * 根据账号编号获得连接
	 */
	public Connection getConnection(long userId) {
		return getPlayerConnectionMapCache().get(userId);
	}

	/**
	 * 销毁连接
	 */
	public void destroyConnection(Connection connection) {
		Connection con = getConnection(connection.getUserId());
		if (con != null && con.equals(connection)) {
			getPlayerConnectionMapCache().remove(connection.getUserId());
			
		}
		
		connection.destroy();
	}

	/**
	 * 是否在游戏中
	 */
	public boolean checkOnLine(long playerId) {
		for(Map.Entry<Long, Connection> playerMap : getPlayerConnectionMapCache().entrySet()){
			Connection conn = playerMap.getValue();
			if(conn.getPlayerId() == playerId){
				return conn.getState().equals(ConnectionState.INGAME);
			}
		}
		return false;
	}
	
	/**
	 * 获得在线玩家编号列表
	 * */
	public List<Long> getOnLinePlayerIDList() {
		List<Long> playerIDList = new ArrayList<Long>();
		for(Map.Entry<Long, Connection> playerMap : getPlayerConnectionMapCache().entrySet()){
			Connection conn = playerMap.getValue();
			if(conn.getState().equals(ConnectionState.INGAME) && conn.getPlayerId() > 0){
				playerIDList.add(conn.getPlayerId());
			}
		}

		return playerIDList;
	}
	
	
	/**
	 * 发送数据
	 */
	private void sendData_netty4(Channel con, MessageObj msg) {
		try {
			if (con != null && con.isOpen()) {
				con.writeAndFlush(msg.getBuf().retain());
			}
		} catch (BufferOverflowException e) {
			LogUtil.error("Buffer Overflow : ", e);
		} catch (Exception e) {
			LogUtil.error("Write data occur error : ", e);
		}
	}

	/**
	 * 发送数据
	 */
	public void sendData(Connection con, MessageObj msg) {
		sendData_netty4(con.getNetty4(), msg);

		if (msg.getBuf() != null) {
			msg.getBuf().release();
		}
		msg.clear();
		msg = null;
	}

	/**
	 * 发送数据至指定玩家 (根据账户编号)
	 */
	public void sendDataToPlayerByUserId(long userId, MessageObj msg) {
		Connection connection = getPlayerConnectionMapCache().get(userId);
		if (connection != null) {
			sendData_netty4(connection.getNetty4(), msg);
		}
		
		if (msg.getBuf() != null) {
			msg.getBuf().release();
		}
		msg.clear();
		msg = null;
	}
	
	/**
	 * 发送数据至指定玩家 (根据玩家编号)
	 */
	public void sendDataToPlayerByPlayerId(long playerId, MessageObj msg){
		for(Map.Entry<Long, Connection> playerMap : getPlayerConnectionMapCache().entrySet() ){
			if(playerMap.getValue().getPlayerId() == playerId){
				sendData_netty4(playerMap.getValue().getNetty4(), msg);
				break;
			}

		}

		if (msg.getBuf() != null) {
			msg.getBuf().release();
		}
		msg.clear();
		msg = null;
	}
	
	/**
	 * 发送数据至所有玩家
	 */
	public void sendDataToAll(MessageObj msg) {
//		for(Map.Entry<Integer, Connection> playerMap : getPlayerConnectionMapCache().entrySet() ){
//			sendData_netty4(playerMap.getValue().getNetty4(), msg);
//		}

		ChannelGroup group = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
		for (Map.Entry<Long, Connection> playerMap : getPlayerConnectionMapCache().entrySet() ) {
			group.add(playerMap.getValue().getNetty4());
		}
		group.writeAndFlush(msg.getBuf().retain());
		
		if (msg.getBuf() != null) {
			msg.getBuf().release();
		}
		msg.clear();
		msg = null;
	}
	
	/**
	 * 发送数据至所有在线玩家
	 */
	public void sendDataToAllOnline(MessageObj msg) {
//		for(Map.Entry<Integer, Connection> playerMap : getPlayerConnectionMapCache().entrySet()){
//			Connection conn = playerMap.getValue();
//			if(conn.getState().equals(ConnectionState.INGAME)){
//				sendData_netty4(conn.getNetty4(), msg);
//			}
//		}

		ChannelGroup group = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
		for(Map.Entry<Long, Connection> playerMap : getPlayerConnectionMapCache().entrySet() ){
			Connection conn = playerMap.getValue();
			if(conn.getState().equals(ConnectionState.INGAME)){
				group.add(playerMap.getValue().getNetty4());
			}
	
		}
		group.writeAndFlush(msg.getBuf().retain());
		
		if (msg.getBuf() != null) {
			msg.getBuf().release();
		}
		msg.clear();
		msg = null;
	}

	/**
	 * 发送数据至指定在线玩家
	 */
	public void sendDataToPlayerList(List<Long> playerIDList, MessageObj msg) {
//		for(Map.Entry<Integer, Connection> playerMap : getPlayerConnectionMapCache().entrySet() ){
//			Connection conn = playerMap.getValue();
//			if(conn.getState().equals(ConnectionState.INGAME) && playerIDList.contains(conn.getPlayerId())){
//				sendData_netty4(conn.getNetty4(), msg);
//			}
//	
//		}

		ChannelGroup group = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
		for(Map.Entry<Long, Connection> playerMap : getPlayerConnectionMapCache().entrySet() ){
			Connection conn = playerMap.getValue();
			if(conn.getState().equals(ConnectionState.INGAME) && playerIDList.contains(conn.getPlayerId())){
				group.add(playerMap.getValue().getNetty4());
			}
	
		}
		group.writeAndFlush(msg.getBuf().retain());
		
		if (msg.getBuf() != null) {
			msg.getBuf().release();
		}
		msg.clear();
		msg = null;
	}
	
	/**
	 * 位加密
	 */
	public static byte[] encryptForDis (byte[] bytes){
		byte[] mes = new byte[KEY_BYTE.length+bytes.length];
		System.arraycopy(KEY_BYTE,0,mes,0,KEY_BYTE.length);
		System.arraycopy(bytes,0,mes,KEY_BYTE.length,bytes.length);

		byte buff;
		for(int i=0; i<mes.length; i+=5){
			if(i + 3 > mes.length - 1) break;
			buff = (byte) ~mes[i + 2];
			mes[i + 2] = mes[i + 3];
			mes[i + 3] = buff;
		}
		return mes;
	}

	/**
	 * 位解密
	 */
	public static byte[] decryptForDis (byte[] bytes){
		byte buff;
		for(int i=0; i<bytes.length; i+=5){
			if(i + 3 > bytes.length - 1) break;
			buff = bytes[i + 2];
			bytes[i + 2] = (byte) ~bytes[i + 3];
			bytes[i + 3] = buff;
		}
		byte[] mes = new byte[bytes.length-KEY_BYTE.length];
		System.arraycopy(bytes,KEY_BYTE.length,mes,0,bytes.length-KEY_BYTE.length);
		return mes;
	}
}
