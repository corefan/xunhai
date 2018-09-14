package com.core;

import com.common.ActionCollection;
import com.common.Config;
import com.common.GameContext;
import com.common.GameSocketService;
import com.domain.GameException;
import com.domain.MessageObj;
import com.google.protobuf.InvalidProtocolBufferException;
import com.message.ExceptionProto.S_Exception;
import com.message.MessageProto.MessageEnum.MessageID;
import com.util.LogUtil;


/**
 * 游戏命令service
 * @author ken
 * @date 2018年4月24日
 */
public class GameCommandService {

	
	private ActionCollection actionCollection = GameContext.getInstance().getActionCollection();
	
	public GameCommandService() {
	}

	public void executeDisruptorCommand(GameMessage gameMessage) {
		
		if(gameMessage.getData() == null){
			LogUtil.error("数据错乱！！！！！！！！！！！！！！！！！！ msgId="+gameMessage.getMsgID());
			return;
		}
		
		try {

			int msgID = gameMessage.getMsgID();

			// 测试接口
			if (msgID > 100000) {
				if (!Config.TEST_SWITCH) {
					return;
				}
			}			
			
			switch (msgID) {
			/**--------------------游戏指令-----------------------------*/
			case MessageID.C_GetServerTime_VALUE:
				actionCollection.getGameServerAction().getServerTime(gameMessage);
				break;
			case MessageID.C_Test_VALUE:
				actionCollection.getTestAction().test(gameMessage);
				break;
				
			/**--------------------登录-----------------------------*/
			case MessageID.C_ExitGame_VALUE:
				actionCollection.getLoginAction().logout(gameMessage.getConnection());
				break;
			case MessageID.C_LoginGame_VALUE:
				actionCollection.getLoginAction().login(gameMessage);
				break;
			case MessageID.C_CreatePlayer_VALUE:
				actionCollection.getLoginAction().createPlayer(gameMessage);
				break;
			case MessageID.C_EnterGame_VALUE:
				actionCollection.getLoginAction().enterGame(gameMessage);
				break;
			case MessageID.C_EnterComplete_VALUE:
				actionCollection.getLoginAction().enterComplete(gameMessage);
				break;
			case MessageID.C_DeletePlayer_VALUE:
				actionCollection.getLoginAction().deletePlayer(gameMessage);
				break;
			case MessageID.C_LoginAgain_VALUE:
				actionCollection.getLoginAction().loginAgain(gameMessage);
				break;
			
			/**--------------------场景-----------------------------*/
			case MessageID.C_EnterScene_VALUE:
				actionCollection.getSceneAction().enterScene(gameMessage);
				break;
			case MessageID.C_SynPosition_VALUE:
				actionCollection.getSceneAction().synPosition(gameMessage);
				break;
			case MessageID.C_UpdatePosition_VALUE:
				actionCollection.getSceneAction().updatePosition(gameMessage);
				break;
			case MessageID.C_GetSceneElementList_VALUE:
				actionCollection.getSceneAction().getSceneElementList(gameMessage);
				break;
			case MessageID.C_CheckPuppets_VALUE:
				actionCollection.getSceneAction().checkPuppets(gameMessage);
				break;
			case MessageID.C_SynMonsterState_VALUE:
				actionCollection.getSceneAction().synMonsterState(gameMessage);
				break;
			case MessageID.C_Transfer_VALUE:
				actionCollection.getSceneAction().transfer(gameMessage);
				break;
			
			/**--------------------玩家-----------------------------*/
			case MessageID.C_ShowPlayer_VALUE:
				actionCollection.getPlayerAction().getShowPlayer(gameMessage);
				break;
			case MessageID.C_Pay_VALUE:
				actionCollection.getPlayerAction().getPayInfo(gameMessage);
				break;
			case MessageID.C_GetFristPayIdList_VALUE:
				actionCollection.getPlayerAction().getFristPayIdList(gameMessage);
				break;
			case MessageID.C_QuickTips_VALUE:
				actionCollection.getPlayerAction().quickTips(gameMessage);
				break;
			case MessageID.C_SetIsAcceptChat_VALUE:
				actionCollection.getPlayerAction().setIsAcceptChat(gameMessage);
				break;
			case MessageID.C_SetIsAcceptApply_VALUE:
				actionCollection.getPlayerAction().setIsAcceptApply(gameMessage);
				break;
			case MessageID.C_GetPlayerOptional_VALUE:
				actionCollection.getPlayerAction().getPlayerOptional(gameMessage);
				break;
			
			/**--------------------副本-----------------------------*/
			case MessageID.C_GetOpenMapList_VALUE:
				actionCollection.getInstanceAction().getOpenMapList(gameMessage);
				break;
			case MessageID.C_EnterInstance_VALUE:
				actionCollection.getInstanceAction().enterInstance(gameMessage);
				break;
			case MessageID.C_AgreeEnter_VALUE:
				actionCollection.getInstanceAction().agreeEnter(gameMessage);
				break;
			case MessageID.C_QuitInstance_VALUE:
				actionCollection.getInstanceAction().quitInstance(gameMessage);
				break;
			
			/**--------------------大荒塔-----------------------------*/
			case MessageID.C_EnterTower_VALUE:
				actionCollection.getTowerAction().enterTower(gameMessage);
				break;
			case MessageID.C_QuitTower_VALUE:
				actionCollection.getTowerAction().quitTower(gameMessage);
				break;
			case MessageID.C_ResetTower_VALUE:
				actionCollection.getTowerAction().resetTower(gameMessage);
				break;
			case MessageID.C_GetShenjingData_VALUE:
				actionCollection.getTowerAction().getShenjingData(gameMessage);
				break;
			
			/**--------------------背包-----------------------------*/
			case MessageID.C_TidyBag_VALUE:
				actionCollection.getBagAction().tidyBag(gameMessage);
				break;
			case MessageID.C_SellItem_VALUE:
				actionCollection.getBagAction().sellItem(gameMessage);
				break;
			case MessageID.C_UseItem_VALUE:
				actionCollection.getBagAction().useItem(gameMessage);
				break;
			case MessageID.C_PutonDrug_VALUE:
				actionCollection.getBagAction().putonDrug(gameMessage);
				break;
			case MessageID.C_PutdownDrug_VALUE:
				actionCollection.getBagAction().putdownDrug(gameMessage);
				break;
			case MessageID.C_Compose_VALUE:
				actionCollection.getFuseAction().compose(gameMessage);
				break;
			case MessageID.C_Decompose_VALUE:
				actionCollection.getFuseAction().decompose(gameMessage);
				break;
			case MessageID.C_AutoDecompose_VALUE:
				actionCollection.getFuseAction().autoDecompose(gameMessage);
				break;
			case MessageID.C_Refine_VALUE:
				actionCollection.getFuseAction().refine(gameMessage);
				break;
			case MessageID.C_AutoRefine_VALUE:
				actionCollection.getFuseAction().autoRefine(gameMessage);
				break;
			
			/**--------------------装备-----------------------------*/
			case MessageID.C_PutOnEquipment_VALUE:
				actionCollection.getEquipmentAction().putOnEquipment(gameMessage);
				break;
			case MessageID.C_PutDownEquipment_VALUE:
				actionCollection.getEquipmentAction().putDownEquipment(gameMessage);
				break;
			case MessageID.C_ShowEquipment_VALUE:
				actionCollection.getEquipmentAction().showEquipment(gameMessage);
				break;
			case MessageID.C_StrongEquip_VALUE:
				actionCollection.getEquipmentAction().strongEquip(gameMessage);
				break;
			case MessageID.C_ComposeEquip_VALUE:
				actionCollection.getEquipmentAction().composeEquip(gameMessage);
				break;
			case MessageID.C_InheritEquip_VALUE:
				actionCollection.getEquipmentAction().inheritEquip(gameMessage);
				break;
			
			/**--------------------战斗-----------------------------*/
			case MessageID.C_SynSkill_VALUE:
				actionCollection.getBattleAction().synSkill(gameMessage);
				break;
			case MessageID.C_SkillResult_VALUE:
				actionCollection.getBattleAction().skillResult(gameMessage);
				break;
			case MessageID.C_Pickup_VALUE:
				actionCollection.getBattleAction().pickup(gameMessage);
				break;
			case MessageID.C_Revive_VALUE:
				actionCollection.getBattleAction().revive(gameMessage);
				break;
			case MessageID.C_ChangePkModel_VALUE:
				actionCollection.getBattleAction().changePkModel(gameMessage);
				break;
			
			/**--------------------技能-----------------------------*/
			case MessageID.C_CreatePlayerSkill_VALUE:
				actionCollection.getSkillAction().studyPlayerSkill(gameMessage);
				break;
			case MessageID.C_UpgradePlayerSkill_VALUE:
				actionCollection.getSkillAction().upgradePlayerSkill(gameMessage);
				break;
			case MessageID.C_AddSkillMastery_VALUE:
				actionCollection.getSkillAction().addSkillMastery(gameMessage);
				break;
			case MessageID.C_GetPlayerSkills_VALUE:
				actionCollection.getSkillAction().getPlayerSkills(gameMessage);
				break;
			
			/**--------------------时装-----------------------------*/
			case MessageID.C_GetFashionList_VALUE:
				actionCollection.getFashionAction().getFashionList(gameMessage);
				break;
			case MessageID.C_PutonFashion_VALUE:
				actionCollection.getFashionAction().putonFashion(gameMessage);
				break;
			case MessageID.C_PutdownFashion_VALUE:
				actionCollection.getFashionAction().putdownFashion(gameMessage);
				break;
			
			/**--------------------邮件-----------------------------*/
			case MessageID.C_GetMailPageList_VALUE:
				actionCollection.getMailAction().getMailPageList(gameMessage);
				break;
			case MessageID.C_ReadMail_VALUE:
				actionCollection.getMailAction().readMail(gameMessage);
				break;
			case MessageID.C_ReceiveAttachment_VALUE:
				actionCollection.getMailAction().receiveAttachment(gameMessage);
				break;
			case MessageID.C_DeleteMail_VALUE:
				actionCollection.getMailAction().deleteMail(gameMessage);
				break;
			
			/**--------------------好友-----------------------------*/
			case MessageID.C_ApplyAddFriend_VALUE:
				actionCollection.getFriendAction().applyFriend(gameMessage);
				break;
			case MessageID.C_ApplyDeal_VALUE:
				actionCollection.getFriendAction().applyDeal(gameMessage);
				break;
			case MessageID.C_ApplyMsgList_VALUE:
				actionCollection.getFriendAction().getApplyMsgList(gameMessage);
				break;
			case MessageID.C_DeleteFriend_VALUE:
				actionCollection.getFriendAction().deleteFriend(gameMessage);
				break;
			case MessageID.C_FriendList_VALUE:
				actionCollection.getFriendAction().getFriendList(gameMessage);
				break;
			case MessageID.C_SerachFriend_VALUE:
				actionCollection.getFriendAction().searchFriend(gameMessage);
				break;
			case MessageID.C_AgreeAllApply_VALUE:
				actionCollection.getFriendAction().agreeAllApply(gameMessage);
				break;
			case MessageID.C_DeleteAllApply_VALUE:
				actionCollection.getFriendAction().deleteAllApply(gameMessage);
				break;

			/**--------------------聊天-----------------------------*/
			case MessageID.C_Chat_VALUE:
				actionCollection.getChatAction().chat(gameMessage);
				break;
			case MessageID.C_PostVoice_VALUE:
				actionCollection.getChatAction().postVoice(gameMessage);
				break;
			case MessageID.C_GetVoice_VALUE:
				actionCollection.getChatAction().getVoice(gameMessage);
				break;
			case MessageID.C_GetOfflineInfo_VALUE:
				actionCollection.getChatAction().getOfflineInfo(gameMessage);
				break;
			
			/**--------------------注灵-----------------------------*/
			case MessageID.C_WakanList_VALUE:
				actionCollection.getWakanAction().wakanList(gameMessage);
				break;
			case MessageID.C_TakeWakan_VALUE:
				actionCollection.getWakanAction().takeWakan(gameMessage);
				break;
			
			/**--------------------任务-----------------------------*/
			case MessageID.C_SubmitTask_VALUE:
				actionCollection.getTaskAction().submitTask(gameMessage);
				break;
			case MessageID.C_CompleteTask_VALUE:
				actionCollection.getTaskAction().completeTask(gameMessage);
				break;
			case MessageID.C_GetDailyTaskList_VALUE:
				actionCollection.getTaskAction().getDailyTaskList(gameMessage);
				break;
			case MessageID.C_RefrshDailyTask_VALUE:
				actionCollection.getTaskAction().refrshDailyTask(gameMessage);
				break;
			case MessageID.C_AcceptDailyTask_VALUE:
				actionCollection.getTaskAction().acceptDailyTask(gameMessage);
				break;
			case MessageID.C_AbandonTask_VALUE:
				actionCollection.getTaskAction().abandonTask(gameMessage);
				break;
			case MessageID.C_AcceptWeekTask_VALUE:
				actionCollection.getTaskAction().acceptWeekTask(gameMessage);
				break;
			
			/**--------------------铭文-----------------------------*/
			case MessageID.C_Epigraph_VALUE:
				actionCollection.getEpigraphAction().epigraph(gameMessage);
				break;
			
			/**--------------------组队-----------------------------*/
			case MessageID.C_GetTeamList_VALUE:
				actionCollection.getTeamAction().getTeamList(gameMessage);
				break;
			case MessageID.C_CreateTeam_VALUE:
				actionCollection.getTeamAction().createTeam(gameMessage);
				break;
			case MessageID.C_GetInviteList_VALUE:
				actionCollection.getTeamAction().getInviteList(gameMessage);
				break;
			case MessageID.C_Invite_VALUE:
				actionCollection.getTeamAction().invite(gameMessage);
				break;
			case MessageID.C_AgreeInvite_VALUE:
				actionCollection.getTeamAction().agreeInvite(gameMessage);
				break;
			case MessageID.C_ClearTeamApplyList_VALUE:
				actionCollection.getTeamAction().clearTeamApplyList(gameMessage);
				break;
			case MessageID.C_ChangeTarget_VALUE:
				actionCollection.getTeamAction().changeTarget(gameMessage);
				break;
			case MessageID.C_QuitTeam_VALUE:
				actionCollection.getTeamAction().quitTeam(gameMessage);
				break;
			case MessageID.C_KickTeamPlayer_VALUE:
				actionCollection.getTeamAction().kickTeamPlayer(gameMessage);
				break;
			case MessageID.C_ChangeCaptain_VALUE:
				actionCollection.getTeamAction().changeCaptain(gameMessage);
				break;
			case MessageID.C_ApplyJoinTeam_VALUE:
				actionCollection.getTeamAction().applyJoinTeam(gameMessage);
				break;
			case MessageID.C_GetTeamApplyList_VALUE:
				actionCollection.getTeamAction().getTeamApplyList(gameMessage);
				break;
			case MessageID.C_ApplyJoinTeamDeal_VALUE:
				actionCollection.getTeamAction().applyJoinTeamDeal(gameMessage);
				break;
			case MessageID.C_PlayerAutoMatch_VALUE:
				actionCollection.getTeamAction().playerAutoMatch(gameMessage);
				break;
			case MessageID.C_TeamAutoMatch_VALUE:
				actionCollection.getTeamAction().teamAutoMatch(gameMessage);
				break;
			case MessageID.C_Follow_VALUE:
				actionCollection.getTeamAction().follow(gameMessage);
				break;
			case MessageID.C_AutoAgreeApply_VALUE:
				actionCollection.getTeamAction().autoAgreeApply(gameMessage);
				break;
			case MessageID.C_GetCaptainPostion_VALUE:
				actionCollection.getTeamAction().getCaptainPostion(gameMessage);
				break;
			
			/**--------------------采集-----------------------------*/
			case MessageID.C_StartCollect_VALUE:
				actionCollection.getCollectAction().startCollect(gameMessage);
				break;
			case MessageID.C_InterruptCollect_VALUE:
				actionCollection.getCollectAction().interruptCollect(gameMessage);
				break;
			
			/**--------------------交易行-----------------------------*/			
			case MessageID.C_GetTradeList_VALUE:
				actionCollection.getTradeAction().getTradeList(gameMessage);
				break;
			case MessageID.C_TradeBuy_VALUE:
				actionCollection.getTradeAction().tradeBuy(gameMessage);
				break;
			case MessageID.C_TradeSell_VALUE:
				actionCollection.getTradeAction().tradeSell(gameMessage);
				break;
			case MessageID.C_ExtendGrid_VALUE:
				actionCollection.getTradeAction().extendGrid(gameMessage);
				break;
			case MessageID.C_OffShelf_VALUE:
				actionCollection.getTradeAction().offShelf(gameMessage);
				break;
			case MessageID.C_SystemItemBuy_VALUE:
				actionCollection.getTradeAction().systemItemBuy(gameMessage);
				break;
			case MessageID.C_GetPlayerTradeList_VALUE:
				actionCollection.getTradeAction().getPlayerTradeList(gameMessage);
				break;
			case MessageID.C_ReUpShelf_VALUE:
				actionCollection.getTradeAction().reUpShelf(gameMessage);
				break;
			
			/**--------------------天梯-----------------------------*/	
			case MessageID.C_GetTianti_VALUE:
				actionCollection.getTiantiAction().getTiantiPanelData(gameMessage);
				break;
			case MessageID.C_GetRankPageList_VALUE:
				actionCollection.getTiantiAction().getRankPageList(gameMessage);
				break;
			case MessageID.C_GiveUp_VALUE:
				actionCollection.getTiantiAction().giveUp(gameMessage);
				break;
			case MessageID.C_UseTiantiItem_VALUE:
				actionCollection.getTiantiAction().useTiantiItem(gameMessage);
				break;
			case MessageID.C_GetStageReward_VALUE:
				actionCollection.getTiantiAction().getStageReward(gameMessage);
				break;
			case MessageID.C_Match_VALUE:
				actionCollection.getTiantiAction().match(gameMessage);
				break;
			case MessageID.C_CancelMatch_VALUE:
				actionCollection.getTiantiAction().calcelMatch(gameMessage);
				break;
			
			/**--------------------家族-----------------------------*/			
			case MessageID.C_GetFamilyInfo_VALUE:
				actionCollection.getFamilyAction().getFamilyData(gameMessage);
				break;
			case MessageID.C_CreateFamily_VALUE:
				actionCollection.getFamilyAction().createFamily(gameMessage);
				break;
			case MessageID.C_DisbandFamily_VALUE:
				actionCollection.getFamilyAction().disbandFamily(gameMessage);
				break;
			case MessageID.C_ChangeFamilyLeader_VALUE:
				actionCollection.getFamilyAction().changeFamilyLeader(gameMessage);
				break;
			case MessageID.C_ChangeFamilyNotice_VALUE:
				actionCollection.getFamilyAction().changeFamilyNotice(gameMessage);
				break;
			case MessageID.C_ChangeFamilyPlayerTitle_VALUE:
				actionCollection.getFamilyAction().changeFamilyPlayerTitle(gameMessage);
				break;
			case MessageID.C_ChangeFamilySortId_VALUE:
				actionCollection.getFamilyAction().changeFamilySortId(gameMessage);
				break;
			case MessageID.C_ExitFamily_VALUE:
				actionCollection.getFamilyAction().exitFamily(gameMessage);
				break;
			case MessageID.C_InviteJoinFamily_VALUE:
				actionCollection.getFamilyAction().inviteJoinFamily(gameMessage);
				break;
			case MessageID.C_InviteMsgDeal_VALUE:
				actionCollection.getFamilyAction().inviteMsgDeal(gameMessage);
				break;
			case MessageID.C_KickFamilyPlayer_VALUE:
				actionCollection.getFamilyAction().kickFamilyPlayer(gameMessage);
				break;
			case MessageID.C_FamilyFB_VALUE:
				actionCollection.getFamilyAction().familyFB(gameMessage);
				break;
			
			/**--------------------商城-----------------------------*/	
			case MessageID.C_GetMarketItemList_VALUE:
				actionCollection.getMarketAction().GetMaketItemList(gameMessage);
				break;
			case MessageID.C_MarketBuy_VALUE:
				actionCollection.getMarketAction().MaketBuyItem(gameMessage);
				break;
			
			/**--------------------签到-----------------------------*/	
			case MessageID.C_Sign_VALUE:
				actionCollection.getSignAction().sign(gameMessage);
				break;
			case MessageID.C_GetConSignReward_VALUE:
				actionCollection.getSignAction().getConSignReward(gameMessage);
				break;
			
			/**--------------------Buff-----------------------------*/	
			case MessageID.C_AutoAddHpMp_VALUE:
				actionCollection.getBuffAction().AutoAddHPMP(gameMessage);
				break;
			case MessageID.C_BreakAddHpMp_VALUE:
				actionCollection.getBuffAction().BreakAddHPMP(gameMessage);
				break;
			
			/**--------------------运营活动-----------------------------*/	
			case MessageID.C_GetReward_VALUE:
				actionCollection.getActivityAction().getOnlineReward(gameMessage);
				break;
			case MessageID.C_GetRewardList_VALUE:
				actionCollection.getActivityAction().getRewardList(gameMessage);
				break;
			case MessageID.C_GetTotalRrechargeReward_VALUE:
				actionCollection.getActivityAction().getTotalRrechargeReward(gameMessage);
				break;
			case MessageID.C_GetDailyRrechargeReward_VALUE:
				actionCollection.getActivityAction().getDailyRrechargeReward(gameMessage);
				break;
			case MessageID.C_GetGrowthFund_VALUE:
				actionCollection.getActivityAction().getGrowthFund(gameMessage);
				break;
			case MessageID.C_GetNationalWelfare_VALUE:
				actionCollection.getActivityAction().getNationalWelfare(gameMessage);
				break;
			case MessageID.C_GetPayActData_VALUE:
				actionCollection.getActivityAction().getPayActData(gameMessage);
				break;
			case MessageID.C_GetFristPayReward_VALUE:
				actionCollection.getActivityAction().getFristPayReward(gameMessage);
				break;
			case MessageID.C_GetTurnRecList_VALUE:
				actionCollection.getActivityAction().getTurnRecList(gameMessage);
				break;
			case MessageID.C_GetTurntableData_VALUE:
				actionCollection.getActivityAction().getTurntableData(gameMessage);
				break;
			case MessageID.C_TurntableDraw_VALUE:
				actionCollection.getActivityAction().turntableDraw(gameMessage);
				break;
			case MessageID.C_GetTombData_VALUE:
				actionCollection.getActivityAction().getTombData(gameMessage);
				break;
			case MessageID.C_Tomb_VALUE:
				actionCollection.getActivityAction().tomb(gameMessage);
				break;
			case MessageID.C_ChangeTomb_VALUE:
				actionCollection.getActivityAction().changeTomb(gameMessage);
				break;
			case MessageID.C_GetTotalSpendReward_VALUE:
				actionCollection.getActivityAction().getTotalSpendReward(gameMessage);
				break;
			case MessageID.C_BuyGrowthFound_VALUE:
				actionCollection.getActivityAction().buyGrowthFund(gameMessage);
				break;
			case MessageID.C_GetOpenServerReward_VALUE:
				actionCollection.getActivityAction().getOpenServerReward(gameMessage);
				break;
			case MessageID.C_GetOpenServerData_VALUE:
				actionCollection.getActivityAction().getOpenServerData(gameMessage);
				break;
			case MessageID.C_GetSevenPayData_VALUE:
				actionCollection.getActivityAction().getSevenPayData(gameMessage);
				break;
			case MessageID.C_BuyArtifactData_VALUE:
				actionCollection.getActivityAction().buyArtifactData(gameMessage);
				break;
			case MessageID.C_GetFristPayData_VALUE:
				actionCollection.getActivityAction().getFristPayData(gameMessage);
				break;
			case MessageID.C_GetLevelAward_VALUE:
				actionCollection.getActivityAction().getLevelAward(gameMessage);
				break;
			case MessageID.C_GetLevelAwardData_VALUE:
				actionCollection.getActivityAction().getLevelAwardData(gameMessage);
				break;
			case MessageID.C_GetBattleValueAward_VALUE:
				actionCollection.getActivityAction().getBattleValueAward(gameMessage);
				break;
			case MessageID.C_GetBVAwardData_VALUE:
				actionCollection.getActivityAction().getBVAwardData(gameMessage);
				break;
			case MessageID.C_GetGiftAward_VALUE:
				actionCollection.getActivityAction().useActCode(gameMessage);
				break;
			case MessageID.C_GetIdentCheckInfo_VALUE:
				actionCollection.getActivityAction().getIdentCheckInfo(gameMessage);
				break;
			case MessageID.C_IdentityCheck_VALUE:
				actionCollection.getActivityAction().identityCheck(gameMessage);
				break;
			case MessageID.C_GetIdCheckAward_VALUE:
				actionCollection.getActivityAction().getIdCheckAward(gameMessage);
				break;
				
			/**------------------- 排行榜-----------------------------*/	
			case MessageID.C_GetRankList_VALUE:
				actionCollection.getRankAction().GetRankList(gameMessage);
				break;
			
			/**------------------- 仇敌-----------------------------*/	
			case MessageID.C_GetEnemyList_VALUE:
				actionCollection.getEnemyAction().getEnemyList(gameMessage);
				break;
			case MessageID.C_DeleteEnemy_VALUE:
				actionCollection.getEnemyAction().deleteEnemy(gameMessage);
				break;
			case MessageID.C_TrackEnemy_VALUE:
				actionCollection.getEnemyAction().trackEnemy(gameMessage);
				break;
			
			/**------------------- 羽翼-----------------------------*/	
			case MessageID.C_GetWingList_VALUE:
				actionCollection.getWingAction().getWingList(gameMessage);
				break;
			case MessageID.C_PutonWing_VALUE:
				actionCollection.getWingAction().putOnWing(gameMessage);
				break;
			case MessageID.C_PutdownWing_VALUE:
				actionCollection.getWingAction().putDownWing(gameMessage);
				break;
			case MessageID.C_Evolve_VALUE:
				actionCollection.getWingAction().evolve(gameMessage);
				break;
			case MessageID.C_UnEvolve_VALUE:
				actionCollection.getWingAction().unEvolve(gameMessage);
				break;
			
			/**------------------- 周活动-----------------------------*/	
			case MessageID.C_GetActivityList_VALUE:
				actionCollection.getWeekActivityAction().getActivityList(gameMessage);
				break;
			case MessageID.C_EnterActivity_VALUE:
				actionCollection.getWeekActivityAction().enterActivity(gameMessage);
				break;
			
			/**------------------- vip-----------------------------*/			
			case MessageID.C_GetVipActReward_VALUE:
				actionCollection.getVipAction().GetVipActReward(gameMessage);
				break;
			case MessageID.C_GetDailyReward_VALUE:
				actionCollection.getVipAction().GetDailyReward(gameMessage);
				break;
			case MessageID.C_GetPlayerVip_VALUE:
				actionCollection.getVipAction().getPlayerVip(gameMessage);
				break;
			case MessageID.C_GetDailyRewardState_VALUE:
				actionCollection.getVipAction().getGetDailyRewardState(gameMessage);
				break;
			case MessageID.C_GetVipWelfare_VALUE:
				actionCollection.getVipAction().GetVipWelfare(gameMessage);
				break;
			case MessageID.C_GetVipWelfareState_VALUE:
				actionCollection.getVipAction().GetVipWelfareState(gameMessage);
				break;
		
			/**------------------- 手机绑定-----------------------------*/	
			case MessageID.C_BindPhone_VALUE:
				actionCollection.getSmsAction().bindPhone(gameMessage);
				break;
			case MessageID.C_GetBindInfo_VALUE:
				actionCollection.getSmsAction().getBindInfo(gameMessage);
				break;
			case MessageID.C_GetValidateCode_VALUE:
				actionCollection.getSmsAction().getValidateCode(gameMessage);
				break;
			case MessageID.C_GetBindReward_VALUE:
				actionCollection.getSmsAction().getBindReward(gameMessage);
				break;
			
			/**------------------- 帮派-----------------------------*/	
			case MessageID.C_AgreeApply_VALUE:
				actionCollection.getGuildAction().agreeApply(gameMessage);
				break;
			case MessageID.C_AgreeInviteJoin_VALUE:
				actionCollection.getGuildAction().agreeInvite(gameMessage);
				break;
			case MessageID.C_ApplyGuild_VALUE:
				actionCollection.getGuildAction().applyGuild(gameMessage);
				break;
			case MessageID.C_AutoApply_VALUE:
				actionCollection.getGuildAction().autoAgreeApply(gameMessage);
				break;
			case MessageID.C_ChangeGuilder_VALUE:
				actionCollection.getGuildAction().changeGuilder(gameMessage);
				break;
			case MessageID.C_ChangeGuildRole_VALUE:
				actionCollection.getGuildAction().changeGuildRole(gameMessage);
				break;
			case MessageID.C_ClearApplys_VALUE:
				actionCollection.getGuildAction().clearApplys(gameMessage);
				break;
			case MessageID.C_CreateGuild_VALUE:
				actionCollection.getGuildAction().createGuild(gameMessage);
				break;
			case MessageID.C_GetApplyList_VALUE:
				actionCollection.getGuildAction().getApplyList(gameMessage);
				break;
			case MessageID.C_GetGuild_VALUE:
				actionCollection.getGuildAction().getGuild(gameMessage);
				break;
			case MessageID.C_GetGuildList_VALUE:
				actionCollection.getGuildAction().getGuildList(gameMessage);
				break;
			case MessageID.C_GetGuildPlayerList_VALUE:
				actionCollection.getGuildAction().getGuildPlayerList(gameMessage);
				break;
			case MessageID.C_InviteJoin_VALUE:
				actionCollection.getGuildAction().inviteJoin(gameMessage);
				break;
			case MessageID.C_KickGuild_VALUE:
				actionCollection.getGuildAction().kickGuild(gameMessage);
				break;
			case MessageID.C_QuickApply_VALUE:
				actionCollection.getGuildAction().quickApply(gameMessage);
				break;
			case MessageID.C_QuitGuild_VALUE:
				actionCollection.getGuildAction().quitGuild(gameMessage);
				break;
			case MessageID.C_RefuseApply_VALUE:
				actionCollection.getGuildAction().refuseApply(gameMessage);
				break;
			case MessageID.C_UpgradeGuild_VALUE:
				actionCollection.getGuildAction().upgradeGuild(gameMessage);
				break;
			case MessageID.C_ModifyNotice_VALUE:
				actionCollection.getGuildAction().modifyNotice(gameMessage);
				break;
			case MessageID.C_GetDonateTimes_VALUE:
				actionCollection.getGuildAction().getDonateTimes(gameMessage);
				break;
			case MessageID.C_Donate_VALUE:
				actionCollection.getGuildAction().donate(gameMessage);
				break;
			case MessageID.C_GetGuildWarList_VALUE:
				actionCollection.getGuildAction().getGuildWarList(gameMessage);
				break;
			case MessageID.C_GuildWar_VALUE:
				actionCollection.getGuildAction().guildWar(gameMessage);
				break;
			case MessageID.C_UpgradeGuildSkill_VALUE:
				actionCollection.getGuildAction().upgradeGuildSkill(gameMessage);
				break;
			case MessageID.C_StudyGuildSkill_VALUE:
				actionCollection.getGuildAction().studyGuildSkill(gameMessage);
				break;
			case MessageID.C_GetGuildSkills_VALUE:
				actionCollection.getGuildAction().getGuildSkills(gameMessage);
				break;
				
			case MessageID.C_GetGuildFightData_VALUE:
				actionCollection.getGuildAction().getGuildFightData(gameMessage);
				break;
			case MessageID.C_GetGuildFights_VALUE:
				actionCollection.getGuildAction().getGuildFights(gameMessage);
				break;
			case MessageID.C_GetUnions_VALUE:
				actionCollection.getGuildAction().getUnions(gameMessage);
				break;
			case MessageID.C_ApplyGuildFight_VALUE:
				actionCollection.getGuildAction().applyGuildFight(gameMessage);
				break;
			case MessageID.C_CreateUnion_VALUE:
				actionCollection.getGuildAction().createUnion(gameMessage);
				break;
			case MessageID.C_ApplyUnion_VALUE:
				actionCollection.getGuildAction().applyUnion(gameMessage);
				break;
			case MessageID.C_AgreeJoinUnion_VALUE:
				actionCollection.getGuildAction().agreeJoinUnion(gameMessage);
				break;
			case MessageID.C_SubmitItem_VALUE:
				actionCollection.getGuildAction().submitItem(gameMessage);
				break;
			case MessageID.C_EnterGuildFight_VALUE:
				actionCollection.getGuildAction().enterGuildFight(gameMessage);
				break;
			case MessageID.C_GetRevenueData_VALUE:
				actionCollection.getGuildAction().getRevenueData(gameMessage);
				break;
			case MessageID.C_ReceiveRevenue_VALUE:
				actionCollection.getGuildAction().receiveRevenue(gameMessage);
				break;
			case MessageID.C_ReceiveSalary_VALUE:
				actionCollection.getGuildAction().receiveSalary(gameMessage);
				break;
			case MessageID.C_ReceiveGift_VALUE:
				actionCollection.getGuildAction().receiveGift(gameMessage);
				break;
			case MessageID.C_GetGuildBuyData_VALUE:
				actionCollection.getGuildAction().getGuildBuyData(gameMessage);
				break;
			case MessageID.C_GuildBuy_VALUE:
				actionCollection.getGuildAction().guildBuy(gameMessage);
				break;
			case MessageID.C_GuildFB_VALUE:
				actionCollection.getGuildAction().guildFB(gameMessage);
				break;
			case MessageID.C_GetManorData_VALUE:
				actionCollection.getGuildAction().getManorData(gameMessage);
				break;
			case MessageID.C_GuildManor_VALUE:
				actionCollection.getGuildAction().guildManor(gameMessage);
				break;
			case MessageID.C_CallManorBoss_VALUE:
				actionCollection.getGuildAction().callManorBoss(gameMessage);
				break;
			case MessageID.C_FeedManorBoss_VALUE:
				actionCollection.getGuildAction().feedManorBoss(gameMessage);
				break;
			
			/**------------------- 熔炉-----------------------------*/	
			case MessageID.C_GetPlayerFurnaceList_VALUE:
				actionCollection.getFurnaceAction().getPlayerFurnaceList(gameMessage);
				break;
			case MessageID.C_UpgradeFurnace_VALUE:
				actionCollection.getFurnaceAction().upgradeFurnace(gameMessage);
				break;	
				
			default:
				LogUtil.error("消息不存在 msgId="+msgID);
				break;
			}
			

		} catch (GameException ge) {
			this.excuteFaild(gameMessage, ge.getCodeID());
		} catch (InvalidProtocolBufferException proe) {
			LogUtil.warn("消息解析异常:userId:"+gameMessage.getConnection().getUserId()+" playerId:"+gameMessage.getConnection().getPlayerId()+" msgId:"+gameMessage.getMsgID(), proe);
		} catch (Exception e) {
			LogUtil.error("其他异常:userId:"+gameMessage.getConnection().getUserId()+" playerId:" + gameMessage.getConnection().getPlayerId()+" msgId:"+gameMessage.getMsgID(), e);
		}
	}
	
	/**
	 * 发送失败消息
	 * */
	private void excuteFaild(GameMessage gameMessage, int codeId) {
		
		S_Exception.Builder builder = S_Exception.newBuilder();
		builder.setCode(codeId);
		
		GameSocketService gameSocketService = GameContext.getInstance().getServiceCollection().getGameSocketService();
		gameSocketService.sendData(gameMessage.getConnection(), new MessageObj(MessageID.S_Exception_VALUE,builder.build().toByteArray()));
	}
}
