package com.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.eclipse.jetty.util.security.Credential.MD5;

import com.common.CodeContext;
import com.common.Config;
import com.constant.CodeConstant;
import com.domain.ActCode;
import com.service.IBatchExcuteService;

/**
 * @author barsk
 * 2014-5-10
 * 生成激活码	
 */
public class CodeUtil {

	private static Random random = new Random();
	
	/** 激活码生产在哪个表 */
	public static String GEN_CODE_TABLE_NAME = "T_ACT_CODE_Z";

	/**
	 * 生成激活码
	 * */
	public static ActCode buildCode(String site, int type, int rewardID, int num, int exclusive, String randomChar) {

		String newCode = MD5.digest(randomChar+site+type+num);

		ActCode actCode = new ActCode();
		actCode.setCode(newCode.substring(4));
		actCode.setSite(site);
		actCode.setType(type);
		actCode.setRewardID(rewardID);
		actCode.setTypeNum(num);
		actCode.setExclusive(exclusive);
		actCode.setCreateTime(new Date());

		return actCode;
	}

	/**
	 * 生成短激活码
	 * */
	public static ActCode buildCodeShort(String site, int type, int rewardID, int num, int exclusive, String randomChar) {

		int first = (num-1)/500 + 1; 
		char end = (char) (65 + random.nextInt(26));  

		ActCode actCode = new ActCode();
		actCode.setCode(first + randomChar + end);
		actCode.setSite(site);
		actCode.setType(type);
		actCode.setRewardID(rewardID);
		actCode.setTypeNum(num);
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
		
		GEN_CODE_TABLE_NAME = "T_ACT_CODE_Z";
		//for (int type=400;type<=420;type += 10) {
			List<ActCode> objList = new ArrayList<ActCode>();
			for (int i=1;i<=2;i++) {
				ActCode actCode = buildCode("zgame", CodeConstant.CODE_60, CodeConstant.CODE_60/10, i, 1, getRandomChar());
				objList.add(actCode);
			}
			
			IBatchExcuteService batchExcuteService = CodeContext.getInstance().getServiceCollection().getBatchExcuteService();
			batchExcuteService.batchInsert(objList);
			
			try {
				Thread.sleep(5*1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			System.out.println("激活码生产完毕！");
		//}

	}

	public static void main(String[] args) {

		// 新生成激活码放在t_act_code_new里面
		try {
			Config.init();
		} catch (Exception e) {
			e.printStackTrace();
		}

		genCode();
		
		/**
		 * 插入激活码:
		 * 
		   insert into t_act_code(CODE,site,type,reward_id,type_num,EXCLUSIVE,state,CREATE_TIME, USE_TIME) 
		   select CODE,site,type,reward_id,type_num,EXCLUSIVE,state,CREATE_TIME, USE_TIME from t_act_code_z;
		 * */
	}

}
