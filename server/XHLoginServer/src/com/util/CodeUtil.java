package com.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.eclipse.jetty.util.security.Credential.MD5;

import com.common.Config;
import com.common.GCCContext;
import com.dao.ActCodeDAO;
import com.domain.ActCode;
import com.domain.GameEntity;
import com.service.IBatchExcuteService;

/**
 * 生成激活码
 * @author ken
 * @date 2018年8月9日
 */
public class CodeUtil {

	private static Random random = new Random();

	/**
	 * 生成激活码
	 * */
	public static ActCode buildCode(String agent, int type, int rewardId, int exclusive, String randomChar) {

		long id = IDUtil.geneteId(ActCode.class);
		String newCode = MD5.digest(randomChar+agent+id);

		ActCode actCode = new ActCode();
		actCode.setId(id);
		actCode.setCode(newCode.substring(24));
		actCode.setAgent(agent);
		actCode.setType(type);
		actCode.setRewardId(rewardId);
		actCode.setExclusive(exclusive);
		actCode.setCreateTime(new Date());

		return actCode;
	}

	/**
	 * 生成短激活码
	 * */
	public static ActCode buildCodeShort(String agent, int type, int rewardId, int exclusive, String randomChar) {

		int first = 1; 
		char end = (char) (65 + random.nextInt(26));  

		ActCode actCode = new ActCode();
		actCode.setId(IDUtil.geneteId(ActCode.class));
		actCode.setCode(first + randomChar + end);
		actCode.setAgent(agent);
		actCode.setType(type);
		actCode.setRewardId(rewardId);
		actCode.setExclusive(exclusive);
		actCode.setCreateTime(new Date());

		return actCode;
	}

	/**
	 * 获得随机字母数字
	 * */
	public static String getRandomChar() {

		String val = "";     

		for(int i = 0; i < 6; i++)     
		{     
			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num"; // 输出字母还是数字     

			if("char".equalsIgnoreCase(charOrNum)) // 字符串     
			{     
				int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; //取得大写字母还是小写字母     
				val += (char) (choice + random.nextInt(26));     
			}     
			else if("num".equalsIgnoreCase(charOrNum)) // 数字     
			{     
				val += String.valueOf(random.nextInt(10));     
			}     
		}     

		return val;   
	}

	/** 生成激活码 */
	public static void genCode() {
		
		try {
			ActCodeDAO actCodeDAO = new ActCodeDAO();
			actCodeDAO.deleteActCode();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		try {
			Thread.sleep(3*1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int num = 10;
		System.out.println("激活码表清理完毕！准备生成新的"+num+"条激活码");
		
		List<GameEntity> objList = new ArrayList<GameEntity>();
		for (int i=1;i<=num;i++) {
			ActCode actCode = buildCode("xh", 20, 2001, 1, getRandomChar());
			objList.add(actCode);
		}
		
		IBatchExcuteService batchExcuteService = GCCContext.getInstance().getServiceCollection().getBatchExcuteService();
		batchExcuteService.batchInsert(objList);
		
		try {
			Thread.sleep(5*1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("激活码生产完毕！ 请把生成的激活插入到服务器激活表上actcode");

	}

	public static void main(String[] args) {

		try {
			Config.init();
		} catch (Exception e) {
			e.printStackTrace();
		}

		genCode();
		
	}

}
