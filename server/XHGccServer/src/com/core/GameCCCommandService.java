package com.core;

import com.command.ExceptionAction;
import com.command.SystemNoticeAction;
import com.common.ActionCollection;
import com.common.GCCContext;
import com.constant.OptTypeConstant;
import com.domain.MessageObj;
import com.util.LogUtil;



/**
 * @author ken
 * 2013-10-29
 * 游戏命令service
 */
public class GameCCCommandService {


	public GameCCCommandService() {
	}

	public void executeCommand(MessageObj msgObj, Connection connection) {

		try {
			
			int msgID = msgObj.getMsgID();
			ActionCollection actionCollection = GCCContext.getInstance().getActionCollection();
			switch (msgID) {
			case OptTypeConstant.LOGIN:
				//登陆
				actionCollection.getLoginAction().loginGame(msgObj, connection);
				break;
			/**-----------------------------------------------用户-----------------------------------------------------*/
			case OptTypeConstant.SYSTEM_1:
				//创建用户
				actionCollection.getUserAction().createUser(msgObj, connection);
				break;
			case OptTypeConstant.SYSTEM_2:
				//更新用户
				actionCollection.getUserAction().updateUser(msgObj, connection);
				break;
			case OptTypeConstant.SYSTEM_3:
				//删除用户
				actionCollection.getUserAction().deleteUser(msgObj, connection);
				break;
			case OptTypeConstant.SYSTEM_4:
				//获得用户列表
				actionCollection.getUserAction().getUserList(msgObj, connection);
				break;
			case OptTypeConstant.OPT_LOG_1:
				//最近操作日志
				actionCollection.getUserAction().recentOptLogList(msgObj, connection);
				break;
			case OptTypeConstant.OPT_LOG_2:
				//用户操作日志
				actionCollection.getUserAction().getRecentOptLogListByUserName(msgObj, connection);
				break;
			/**-----------------------------------------------角色-----------------------------------------------------*/
			case OptTypeConstant.SYSTEM_5:
				//创建角色
				actionCollection.getRoleAction().createRole(msgObj, connection);
				break;
			case OptTypeConstant.SYSTEM_6:
				//更改角色
				actionCollection.getRoleAction().updateRole(msgObj, connection);
				break;
			case OptTypeConstant.SYSTEM_7:
				//删除角色
				actionCollection.getRoleAction().deleteRole(msgObj, connection);
				break;
			case OptTypeConstant.SYSTEM_8:
				//获得角色列表
				actionCollection.getRoleAction().getRoleList(msgObj, connection);
				break;
			/**-----------------------------------------------权限-----------------------------------------------------*/
			case OptTypeConstant.SYSTEM_14:
				//获得权限列表
				actionCollection.getAuthorityAction().getAuthorityList(msgObj, connection);
				break;
			case OptTypeConstant.SYSTEM_11:
				//创建权限
				actionCollection.getAuthorityAction().createAuthority(msgObj, connection);
				break;
			case OptTypeConstant.SYSTEM_12:
				//更新权限
				actionCollection.getAuthorityAction().updateAuthority(msgObj, connection);
				break;
			case OptTypeConstant.SYSTEM_13:
				//删除权限
				actionCollection.getAuthorityAction().deleteAuthority(msgObj, connection);
				break;
				/**-----------------------------------------------玩家-----------------------------------------------------*/
			case OptTypeConstant.PLAYER_1:
				//查看实时在线
				actionCollection.getPlayerAction().showOnLineNum(msgObj, connection);
				break;
			case OptTypeConstant.PLAYER_2:
				//查看玩家信息
				actionCollection.getPlayerAction().searchPlayer(msgObj, connection);
				break;
			case OptTypeConstant.PLAYER_3:
				//封号
				actionCollection.getPlayerAction().fenghao(msgObj, connection);
				break;
			case OptTypeConstant.PLAYER_4:
				//解封
				actionCollection.getPlayerAction().jiefeng(msgObj, connection);
				break;
			case OptTypeConstant.PLAYER_5:
				//删除玩家缓存
				actionCollection.getPlayerAction().deletePlayerCache(msgObj, connection);
				break;
			case OptTypeConstant.PLAYER_6:
				//玩家背包物品查询
				actionCollection.getPlayerAction().showPlayerBackpack(msgObj, connection);
				break;
			case OptTypeConstant.PLAYER_7:
				//玩家登陆日志查询
				actionCollection.getPlayerAction().showPlayerLoginLog(msgObj, connection);
				break;
			case OptTypeConstant.PLAYER_8:
				//玩家消费日志查询
				actionCollection.getPlayerAction().showPlayerBuyLog(msgObj, connection);
				break;
			case OptTypeConstant.PLAYER_9:
				//玩家充值日志查询
				actionCollection.getPlayerAction().showPlayerRechargeLog(msgObj, connection);
				break;
			case OptTypeConstant.PLAYER_10:
				//查询玩家信息
				actionCollection.getPlayerAction().searchPlayerInfo(msgObj, connection);
				break;
			case OptTypeConstant.PLAYER_15:
				//修改玩家类型
				actionCollection.getPlayerAction().updatePlayerType(msgObj, connection);
				break;
			case OptTypeConstant.PLAYER_17:
				//禁言
				actionCollection.getPlayerAction().banTalk(msgObj, connection);
				break;
			case OptTypeConstant.PLAYER_18:
				//解禁
				actionCollection.getPlayerAction().unBanTalk(msgObj, connection);
				break;
				
				/**-----------------------------------------------IP-----------------------------------------------------*/
			case OptTypeConstant.BAN_IP_OPERATE:
				//IP操作
				actionCollection.getIpAction().banIpOperate(msgObj, connection);
				break;
			case OptTypeConstant.BAN_IP_LIST:
				//IP列表
				actionCollection.getIpAction().getBanIpList(msgObj, connection);
				break;
				/**-----------------------------------------------IP-----------------------------------------------------*/
			case OptTypeConstant.ITEM_8:
				//发送邮件(不带附件)
				actionCollection.getMailAction().sendMailNoAttachment(msgObj, connection);
				break;
				/**-----------------------------------------------物品-----------------------------------------------------*/
			case OptTypeConstant.ITEM_1:
				//获得道具装备缓存
				actionCollection.getItemAction().getItemEquipmentList(msgObj, connection);
				break;
			case OptTypeConstant.ITEM_4:
				//申请发放物品
				actionCollection.getItemAction().applySendItem(msgObj, connection);
				break;
			case OptTypeConstant.ITEM_6:
				//获得申请发放列表
				actionCollection.getItemAction().getApplyList(msgObj, connection);
				break;
			case OptTypeConstant.ITEM_9:
				//获得发放日志列表
				actionCollection.getItemAction().getApplyLogList(msgObj, connection);
				break;
			case OptTypeConstant.ITEM_7:
				//查询物品
				actionCollection.getItemAction().showItemInfo(msgObj, connection);
				break;
			case OptTypeConstant.ITEM_5:
				//审核发送物品
				actionCollection.getItemAction().verifyAppItem(msgObj, connection);
				break;
				/**-----------------------------------------------数据处理-----------------------------------------------------*/
			case OptTypeConstant.DEAL_DATA_1:
				//刷新基础缓存数据
				actionCollection.getDealDataAction().refreshBaseCache(msgObj, connection);
				break;
			case OptTypeConstant.DEAL_DATA_2:
				//刷新配置文件数据
				actionCollection.getDealDataAction().refreshConfigCache(msgObj, connection);
				break;
			case OptTypeConstant.DEAL_DATA_3:
				//同步缓存数据
				actionCollection.getDealDataAction().synCacheData(msgObj, connection);
				break;
			case OptTypeConstant.DEAL_DATA_8:
				//热更新class
				actionCollection.getDealDataAction().hotUpdateClass(msgObj, connection);
				break;
			case OptTypeConstant.DEAL_DATA_10:
				//停服维护
				actionCollection.getDealDataAction().stopServer(msgObj, connection);
				break;
			case OptTypeConstant.CHAT_MONITOR:
				//聊天监控
				actionCollection.getChatAction().reqChatInfo(msgObj, connection);
				break;
				/**-----------------------------------------------系统公告-----------------------------------------------------*/
			case OptTypeConstant.NOTICE_1:
				//发送公告
				SystemNoticeAction systemNoticeAction = GCCContext.getInstance().getActionCollection().getSystemNoticeAction();
				systemNoticeAction.sendNotice(msgObj, connection);
				break;
			case OptTypeConstant.NOTICE_2:
				//添加公告
				systemNoticeAction = GCCContext.getInstance().getActionCollection().getSystemNoticeAction();
				systemNoticeAction.addNotice(msgObj, connection);
				break;
			case OptTypeConstant.NOTICE_3:
				//删除公告
				systemNoticeAction = GCCContext.getInstance().getActionCollection().getSystemNoticeAction();
				systemNoticeAction.deleteNotice(msgObj, connection);
				break;
			case OptTypeConstant.NOTICE_4:
				//获得公告列表
				systemNoticeAction = GCCContext.getInstance().getActionCollection().getSystemNoticeAction();
				systemNoticeAction.getNoticeList(msgObj, connection);
				break;
		/**-----------------------------------------------服务器-----------------------------------------------------*/
			case OptTypeConstant.SERVER_1:
				//创建服务器
				actionCollection.getServerAction().createServer(msgObj, connection);
				break;
			case OptTypeConstant.SERVER_2:
				//更新服务器
				actionCollection.getServerAction().updateServer(msgObj, connection);
				break;
			case OptTypeConstant.SERVER_3:
				//删除服务器
				actionCollection.getServerAction().deleteServer(msgObj, connection);
				break;
			case OptTypeConstant.SERVER_4:
				//获得服务器列表
				actionCollection.getServerAction().getServerList(msgObj, connection);
				break;
			}
			

		} catch (Exception e) {
			e.printStackTrace();
			//异常推送
			ExceptionAction exceptionAction = GCCContext.getInstance().getActionCollection().getExceptionAction();
			exceptionAction.sendException(e, connection);
			LogUtil.error("异常:", e);
		}
	}
	
}
