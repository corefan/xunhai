package com.service;

import java.util.List;
import com.domain.family.Family;
import com.domain.family.PlayerFamily;


/**
 * 家族系统
 * @author jiangqin
 * @date 2017-4-6
 */
public interface IFamilyService {
	
	/** 初始缓存*/
	void initCache();
	
	/** 根据家族唯一ID获取家族成员*/
	List<Long> getFamilyPlayerIds(long playerFamilyId);
	
	/** 获取家族信息*/
	void getFamilyData(long playerId) throws Exception;
	
	/** 创建家族*/
	void createFamily(long playerId, String familyName) throws Exception;
	
	/** 解散家族*/
	void disbandFamily(long playerId) throws Exception;
	
	/** 邀请加入家族*/
	void inviteJoinFamily(long playerId, long friendId) throws Exception;
	
	/** 退出家族*/
	void exitFamily(Long playerId) throws Exception;
	
	/** 族长转让*/
	void changeFamilyLeader(long playerId, long targetPlayerId) throws Exception;
	
	/** 调整家族成员排位*/
	void changeFamilySortId(long playerId, List<Integer> sortList) throws Exception;
	
	/** 编辑家族公告*/
	void changeFamilyNotice(long playerId, String msg) throws Exception;
	
	/** 踢出成员*/
	void kickFamilyPlayer(long playerId, Long targetPlayerId) throws Exception;
	
	/** 修改成员称谓*/
	void changeFamilyPlayerTitle(long playerId, long targetPlayerId, String title) throws Exception;
	
	/** 邀请信息处理*/
	void inviteMsgDeal(long playerId, long playerFamilyId, int state) throws Exception;
	
	/**
	 * 家族副本  1：开始 2：进入
	 */
	void familyFB(long playerId, int type) throws Exception;
	
	/**
	 * 家族副本结束
	 */
	void endFamilyFB(long familyId);
	
	/** 调度删除家族信息*/
	void quartzDaily();
	
	/** 获取玩家家族信息*/
	PlayerFamily getPlayerFamily(long playerId);
	
	/** 获取家族信息*/
	Family getFamily(long playerFamilyId);
	
	/** 处理上线*/
	void dealLogin(long playerId);
	
	/** 处理下线*/
	void dealExit(long playerId);
	
	/** 调度删除缓存*/
	void deleteCache(long playerId);

	/** 调度处理家族信息*/
	void quartzDealFamily();
}
