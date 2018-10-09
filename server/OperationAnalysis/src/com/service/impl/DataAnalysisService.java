package com.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.common.CacheService;
import com.common.DateService;
import com.common.GCCContext;
import com.common.jedis.RedisUtil;
import com.constant.CacheConstant;
import com.dao.PayAnalysisDAO;
import com.domain.BaseGameStep;
import com.domain.ServerConf;
import com.domain.log.CostLog;
import com.domain.log.FiveOnlineLog;
import com.domain.log.LoginLog;
import com.domain.log.MarketLog;
import com.domain.log.OnlineTimeLog;
import com.domain.log.RegisterLog;
import com.google.gson.reflect.TypeToken;
import com.service.IBaseDataService;
import com.service.IDataAnalysisService;
import com.util.CommonUtil;
import com.util.DAOFactory;
import com.util.LogUtil;

/**
 * 数据分析
 * @author ken
 * @date 2018年3月29日
 */
public class DataAnalysisService implements IDataAnalysisService {
	private PayAnalysisDAO payAnalysisDAO = DAOFactory.getPayAnalysisDAO();

	public JSONObject shopSell(Date startTime, Date endTime, String gameSite, String agent) {
		
		JSONObject resultMap = new JSONObject();
		List<JSONObject> jsonList = new ArrayList<JSONObject>();

		IBaseDataService baseDataService = GCCContext.getInstance().getServiceCollection().getBaseDataService();
		List<String> gameSites = baseDataService.getGameSites(gameSite, agent);
		
		try {
			Map<Integer, Set<Long>> playerIds = new HashMap<Integer, Set<Long>>();
			
			Map<Integer, MarketLog> map = new HashMap<Integer, MarketLog>();
			
			int sum = 0;
			
			List<Date> findDates = DateService.findDates(startTime, endTime);
			for(Date findDate : findDates){
				for (String gSite : gameSites) {
					List<String> list = RedisUtil.getValueOfList("log@" + gSite + "@MarketLogList@"+DateService.dateFormatYMD(findDate), 0, -1);
					if (list != null) {
						for (String str : list) {
							MarketLog marketLog = RedisUtil.deserializelist(str, new TypeToken<MarketLog>() {});

							sum += marketLog.getPrice();
							
							Set<Long> ids = playerIds.get(marketLog.getItemId());
							if(ids == null){
								ids = new HashSet<Long>();
								playerIds.put(marketLog.getItemId(), ids);
							}
							
							ids.add(marketLog.getPlayerId());
							
							MarketLog model = map.get(marketLog.getItemId());
							if(model == null){
								map.put(marketLog.getItemId(), marketLog);
							}else{
								model.setNum(model.getNum() + marketLog.getNum());
								model.setPrice(model.getPrice() + marketLog.getPrice());
							}
							
						}
					}
				}	
			}

			
			for(Map.Entry<Integer, MarketLog> entry : map.entrySet()){
				int itemId = entry.getKey();
				MarketLog model = entry.getValue();
				
				JSONObject json = new JSONObject();
				json.put("itemID", itemId);
				json.put("name", model.getItemName());
				json.put("pnum", playerIds.get(itemId).size());
				json.put("totalNum", model.getNum());
				json.put("diamond", model.getPrice());

				jsonList.add(json);
			}
			
			resultMap.put("sum", sum);
			resultMap.put("dataList", jsonList);
		} catch (Exception e) {
			LogUtil.error("异常:", e);
		}
		
		return resultMap;
	}

	public JSONObject diamondCost(Date startTime, Date endTime, String gameSite, String agent) {

		JSONObject resultMap = new JSONObject();
		List<JSONObject> jsonList = new ArrayList<JSONObject>();

		IBaseDataService baseDataService = GCCContext.getInstance().getServiceCollection().getBaseDataService();
		List<String> gameSites = baseDataService.getGameSites(gameSite, agent);

		try {

			Map<String, Integer> reusltMap = new HashMap<String, Integer>();
			
			int totalNum = 0;
			
			List<Date> findDates = DateService.findDates(startTime, endTime);
			for(Date findDate : findDates){
				for (String gSite : gameSites) {
					List<String> list = RedisUtil.getValueOfList("log@" + gSite + "@CostLogList@"+DateService.dateFormatYMD(findDate), 0, -1);
					if (list != null) {
						for (String str : list) {
							CostLog costLog = RedisUtil.deserializelist(str, new TypeToken<CostLog>() {
							});

							if (costLog.getType() != 4)
								continue;

							totalNum += costLog.getValue();

							Integer num = reusltMap.get(costLog.getCostName());
							if (num == null) {
								num = 0;
							}
							num += costLog.getValue();
							reusltMap.put(costLog.getCostName(), num);
						}
					}
				}
			}


			for (Map.Entry<String, Integer> entry : reusltMap.entrySet()) {
				JSONObject json = new JSONObject();

				int num = entry.getValue();
				json.put("name", entry.getKey());
				json.put("num", num);
				json.put("rate", CommonUtil.formatDouble(num * 100f / Math.abs(totalNum), 2) + "%");

				jsonList.add(json);
			}

			resultMap.put("totalNum", Math.abs(totalNum));
			resultMap.put("dataList", jsonList);
		} catch (Exception e) {
			LogUtil.error("异常:", e);
		}

		return resultMap;
	}

	public JSONObject payOne(Date startTime, Date endTime, String gameSite, String agent) {
		JSONObject resultMap = new JSONObject();

		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		
		IBaseDataService baseDataService = GCCContext.getInstance().getServiceCollection().getBaseDataService();
		List<String> gameSites = baseDataService.getGameSites(gameSite, agent);
		
		try {
			
			List<Date> findDates = DateService.findDates(startTime, endTime);
			for(Date findDate : findDates){
				String findDateStr = DateService.dateFormatYMD(findDate);
				Set<Long> registerList = new HashSet<Long>();
				int num1= 0;
				int num2= 0;
				int num3= 0;
				int num4= 0;
				int num5= 0;
				int num6= 0;
				int num7= 0;
				for (String gSite : gameSites) {
					List<String> list = RedisUtil.getValueOfList("log@" + gSite + "@RegisterLogList@"+findDateStr, 0, -1);
					if (list != null) {
						for (String str : list) {
							RegisterLog registerLog = RedisUtil.deserializelist(str, new TypeToken<RegisterLog>() {});
							
							registerList.add(registerLog.getUserId());
						}
					}
				}
				
				int registerNum = registerList.size();
				if (registerNum > 0) {
					for(int i = 0; i < 7; i++){
						String date = DateService.dateFormatYMD(DateService.addDateByType(findDate, Calendar.DAY_OF_MONTH, i));
						List<Map<String, Object>> payMaps = payAnalysisDAO.sumNum(date, agent, gameSite);
						for(Map<String, Object> map : payMaps){
							long userId = Long.valueOf(map.get("userId").toString());
							if(registerList.contains(userId)){
								switch (i) {
								case 0:
									num1++;
									break;
								case 1:
									num2++;
									break;
								case 2:
									num3++;
									break;
								case 3:
									num4++;
									break;
								case 4:
									num5++;
									break;
								case 5:
									num6++;
									break;
								case 6:
									num7++;
									break;

								default:
									break;
								}
							}
						}
					}
					
					JSONObject json = new JSONObject();
					json.put("date", findDateStr);
					json.put("rnum", registerNum);
					json.put("num1", num1+"("+CommonUtil.formatDouble(num1 * 100f / registerNum, 2)+"%)");
					json.put("num2", num2+"("+CommonUtil.formatDouble(num2 * 100f / registerNum, 2)+"%)");
					json.put("num3", num3+"("+CommonUtil.formatDouble(num3 * 100f / registerNum, 2)+"%)");
					json.put("num4", num4+"("+CommonUtil.formatDouble(num4 * 100f / registerNum, 2)+"%)");
					json.put("num5", num5+"("+CommonUtil.formatDouble(num5 * 100f / registerNum, 2)+"%)");
					json.put("num6", num6+"("+CommonUtil.formatDouble(num6 * 100f / registerNum, 2)+"%)");
					json.put("num7", num7+"("+CommonUtil.formatDouble(num7 * 100f / registerNum, 2)+"%)");
					jsonList.add(json);
				}
			}

			resultMap.put("dataList", jsonList);
		} catch (Exception e) {
			LogUtil.error("异常:", e);
		}

		return resultMap;
	}

	public JSONObject payTwo(String gameSite, String agent) {
		JSONObject json = new JSONObject();
		List<JSONObject> jsonList = new ArrayList<JSONObject>();

		try {
			IBaseDataService baseDataService = GCCContext.getInstance().getServiceCollection().getBaseDataService();
			List<String> gameSites = baseDataService.getGameSites(gameSite, agent);
			
			Map<String, Integer> totalMap = new HashMap<String, Integer>();
			for (String gSite : gameSites) {
				Map<String, String> map = RedisUtil.getAllValueOfMap("log@"+gSite+"@FirstLvMap");
				if(map != null){
					for(Map.Entry<String, String> entry : map.entrySet()){
						String level = entry.getKey();
						int pnum = Integer.valueOf(entry.getValue());
						
						Integer pnums = totalMap.get(level);
						if(pnums == null){
							pnums = 0;
						}
						pnums += pnum;
						totalMap.put(level, pnums);
					}
				}
			}
			
 			for(Map.Entry<String, Integer> entry : totalMap.entrySet()){
				JSONObject obj = new JSONObject();
				obj.put("level", entry.getKey()+"级");
				obj.put("num", entry.getValue());
				jsonList.add(obj);
 			}
			json.put("dataList", jsonList);
		} catch (JSONException e) {
			LogUtil.error("异常:", e);
		}

		return json;
	}

	@Override
	public JSONObject fiveMinOnLine(Date startTime, Date endTime, String gameSite, String agent) {
		JSONObject resultMap = new JSONObject();
		List<JSONObject> jsonList = new ArrayList<JSONObject>();

		IBaseDataService baseDataService = GCCContext.getInstance().getServiceCollection().getBaseDataService();
		List<String> gameSites = baseDataService.getGameSites(gameSite, agent);
		
		try {

			Map<Long, Integer> fiveMap = new HashMap<Long, Integer>();
			Map<Long, Integer> endFiveMap = new HashMap<Long, Integer>();
			
			long maxTime = 0;
			
			for (String gSite : gameSites) {
				List<String> list = RedisUtil.getValueOfList("log@" + gSite + "@FiveOnlineLogList@"+DateService.dateFormatYMD(startTime), 0, -1);
				if (list != null) {
					for (String str : list) {
						FiveOnlineLog fiveOnlineLog = RedisUtil.deserializelist(str, new TypeToken<FiveOnlineLog>() {});

						long time = DateService.getFiveMinuteTime(fiveOnlineLog.getCreateTime());
						Integer num = fiveMap.get(time);
						if(num == null){
							num = 0;
						}
						num += fiveOnlineLog.getNum();
						fiveMap.put(time, num);
						
						if(maxTime == 0){
							maxTime = time;
						}
					}
				}
				
				List<String> list1 = RedisUtil.getValueOfList("log@" + gSite + "@FiveOnlineLogList@"+DateService.dateFormatYMD(endTime), 0, -1);
				if (list1 != null) {
					for (String str : list1) {
						FiveOnlineLog fiveOnlineLog = RedisUtil.deserializelist(str, new TypeToken<FiveOnlineLog>() {});

						long time = DateService.getFiveMinuteTime(fiveOnlineLog.getCreateTime());
						Integer num = endFiveMap.get(time);
						if(num == null){
							num = 0;
						}
						num += fiveOnlineLog.getNum();
						endFiveMap.put(time, num);
					}
				}

			}
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(startTime);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(endTime);
			endCal.set(Calendar.HOUR_OF_DAY, 0);
			endCal.set(Calendar.MINUTE, 0);
			endCal.set(Calendar.SECOND, 0);
			endCal.set(Calendar.MILLISECOND, 0);
			
			long curTime = 0;

			if (fiveMap.size() > 0) {
				for (int i = 0; i < 288; i++) {
					Date time = cal.getTime();

					curTime = cal.getTime().getTime() / 1000;

					Integer num = fiveMap.get(curTime);
					Integer num1 = endFiveMap.get(endCal.getTime().getTime() / 1000);

					if (num == null && curTime <= maxTime) {
						num = 0;
					}

					if (num1 == null)
						num1 = 0;
					
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("date", DateService.dateFormatHMS(time));

					if(num != null){
						jsonObject.put("num", num);
					}
					jsonObject.put("num1", num1);

					cal.add(Calendar.MINUTE, 5);
					endCal.add(Calendar.MINUTE, 5);

					jsonList.add(jsonObject);
				}
			}
			resultMap.put("dataList", jsonList);
		} catch (Exception e) {
			LogUtil.error("异常:", e);
		}

		return resultMap;
	}
	
	public JSONObject fiveRegister(Date startTime, Date endTime, String gameSite, String agent) {
		JSONObject resultMap = new JSONObject();
		List<JSONObject> jsonList = new ArrayList<JSONObject>();

		IBaseDataService baseDataService = GCCContext.getInstance().getServiceCollection().getBaseDataService();
		List<String> gameSites = baseDataService.getGameSites(gameSite, agent);
		
		try {

			Map<Long, Integer> fiveMap = new HashMap<Long, Integer>();
			Map<Long, Integer> endFiveMap = new HashMap<Long, Integer>();
			
			long maxTime = 0;
			
			for (String gSite : gameSites) {
				List<String> list = RedisUtil.getValueOfList("log@" + gSite + "@RegisterLogList@"+DateService.dateFormatYMD(startTime), 0, -1);
				if (list != null) {
					for (String str : list) {
						RegisterLog registerLog = RedisUtil.deserializelist(str, new TypeToken<RegisterLog>() {});

						long time = DateService.getFiveMinuteTime(registerLog.getCreateTime());
						Integer num = fiveMap.get(time);
						if(num == null){
							num = 0;
						}
						num += 1;
						fiveMap.put(time, num);
						
						if(maxTime == 0){
							maxTime = time;
						}
						
					}
				}
				
				List<String> list1 = RedisUtil.getValueOfList("log@" + gSite + "@RegisterLogList@"+DateService.dateFormatYMD(endTime), 0, -1);
				if (list1 != null) {
					for (String str : list1) {
						RegisterLog registerLog = RedisUtil.deserializelist(str, new TypeToken<RegisterLog>() {});

						long time = DateService.getFiveMinuteTime(registerLog.getCreateTime());
						Integer num = endFiveMap.get(time);
						if(num == null){
							num = 0;
						}
						num += 1;
						endFiveMap.put(time, num);
						
					}
				}

			}
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(startTime);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(endTime);
			endCal.set(Calendar.HOUR_OF_DAY, 0);
			endCal.set(Calendar.MINUTE, 0);
			endCal.set(Calendar.SECOND, 0);
			endCal.set(Calendar.MILLISECOND, 0);
			
			long curTime = 0;

			if (fiveMap.size() > 0) {
				for (int i = 0; i < 288; i++) {
					Date time = cal.getTime();

					curTime = cal.getTime().getTime() / 1000;

					Integer num = fiveMap.get(curTime);
					Integer num1 = endFiveMap.get(endCal.getTime().getTime() / 1000);

					if (num == null && curTime <= maxTime) {
						num = 0;
					}

					if (num1 == null)
						num1 = 0;
					
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("date", DateService.dateFormatHMS(time));

					if(num != null){
						jsonObject.put("num", num);
					}
					jsonObject.put("num1", num1);

					cal.add(Calendar.MINUTE, 5);
					endCal.add(Calendar.MINUTE, 5);

					jsonList.add(jsonObject);
				}
			}
			resultMap.put("dataList", jsonList);
		} catch (Exception e) {
			LogUtil.error("异常:", e);
		}
		
		return resultMap;
	}
	
	public JSONObject showRetain(Date startTime, Date endTime, String gameSite, String agent) {
		JSONObject resultMap = new JSONObject();

		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		
		IBaseDataService baseDataService = GCCContext.getInstance().getServiceCollection().getBaseDataService();
		List<String> gameSites = baseDataService.getGameSites(gameSite, agent);
		
		try {
			
			List<Date> findDates = DateService.findDates(startTime, endTime);
			for(Date findDate : findDates){
				String findDateStr = DateService.dateFormatYMD(findDate);
				Set<Long> registerList = new HashSet<Long>();
				int registerNum = 0;
				int num2= 0;
				int num3= 0;
				int num4= 0;
				int num5= 0;
				int num6= 0;
				int num7= 0;
				int num14= 0;
				int num30= 0;
				for (String gSite : gameSites) {
					registerList.clear();
					List<String> list = RedisUtil.getValueOfList("log@" + gSite + "@RegisterLogList@"+findDateStr, 0, -1);
					if (list != null) {
						for (String str : list) {
							RegisterLog registerLog = RedisUtil.deserializelist(str, new TypeToken<RegisterLog>() {});
							
							registerList.add(registerLog.getUserId());
						}
						
						if(registerList.size() > 0){
							registerNum += registerList.size();
							num2 += this.calRetain(gSite, findDate, 1, registerList);
							num3 += this.calRetain(gSite, findDate, 2, registerList);
							num4 += this.calRetain(gSite, findDate, 3, registerList);
							num5 += this.calRetain(gSite, findDate, 4, registerList);
							num6 += this.calRetain(gSite, findDate, 5, registerList);
							num7 += this.calRetain(gSite, findDate, 6, registerList);
							num14 += this.calRetain(gSite, findDate, 13, registerList);
							num30 += this.calRetain(gSite, findDate, 29, registerList);
						}
					}
				}
				
				
				if (registerNum > 0) {
					JSONObject json = new JSONObject();
					json.put("time", findDateStr);
					json.put("registerNum", registerNum);
					json.put("rate2", CommonUtil.formatDouble(num2 * 100f / registerNum, 2));
					json.put("rate3", CommonUtil.formatDouble(num3 * 100f / registerNum, 2));
					json.put("rate4", CommonUtil.formatDouble(num4 * 100f / registerNum, 2));
					json.put("rate5", CommonUtil.formatDouble(num5 * 100f / registerNum, 2));
					json.put("rate6", CommonUtil.formatDouble(num6 * 100f / registerNum, 2));
					json.put("rate7", CommonUtil.formatDouble(num7 * 100f / registerNum, 2));
					json.put("rate14", CommonUtil.formatDouble(num14 * 100f / registerNum, 2));
					json.put("rate30", CommonUtil.formatDouble(num30 * 100f / registerNum, 2));
					jsonList.add(json);
				}
				
			}

			resultMap.put("dataList", jsonList);
		} catch (Exception e) {
			LogUtil.error("异常:", e);
		}

		return resultMap;
	}
	
	/**
	 * 计算留存的人数
	 * delayDay留存天数
	 * registerList 注册的账号
	 */
	private int calRetain(String gSite, Date findDate,  int delayDay, Set<Long> registerList){
		int num = 0;
		List<String> list2 = RedisUtil.getValueOfList("log@" + gSite + "@LoginLogList@"+DateService.dateFormatYMD(DateService.addDateByType(findDate, Calendar.DAY_OF_MONTH, delayDay)), 0, -1);
		if(list2 != null){
			Set<Long> hasLoginIds = new HashSet<Long>();
			for (String str : list2) {
				LoginLog loginLog = RedisUtil.deserializelist(str, new TypeToken<LoginLog>() {});
				if(hasLoginIds.contains(loginLog.getUserId())){
					continue;
				} 
				
				if(registerList.contains(loginLog.getUserId())){
					num++;
					hasLoginIds.add(loginLog.getUserId());
				}
			}
		}
		return num;
	}

	public JSONObject fivePay(String startTime, String targetTime, String gameSite, String agent) {
		JSONObject resultMap = new JSONObject();

		List<JSONObject> jsonList = new ArrayList<JSONObject>();

		try {

			List<Map<String, Object>> resultList = payAnalysisDAO.fivePay(startTime, gameSite, agent);
			List<Map<String, Object>> endResultList = payAnalysisDAO.fivePay(targetTime, gameSite, agent);

			Calendar cal = checkCalendar(startTime);
			Calendar endCal = checkCalendar(targetTime);

			Map<Long, Integer> fiveMap = listToMap(resultList);
			Map<Long, Integer> endFiveMap = listToMap(endResultList);

			long maxTime = 0;
			long curTime;
			Iterator<Long> iterator = fiveMap.keySet().iterator();
			while (iterator.hasNext()) {
				curTime = iterator.next();
				if (curTime > maxTime) {
					maxTime = curTime;
				}
			}

			if (fiveMap.size() > 0) {
				for (int i = 0; i < 288; i++) {
					Date time = cal.getTime();
					curTime = cal.getTime().getTime() / 1000;
					Integer num = fiveMap.get(curTime);
					Integer num1 = endFiveMap.get(endCal.getTime().getTime() / 1000);
					if (num == null && curTime <= maxTime) {
						num = 0;
					}
					if (num1 == null)
						num1 = 0;
					
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("date", DateService.dateFormatHMS(time));

					if(num != null){
						jsonObject.put("num", num);
					}
					jsonObject.put("num1", num1);

					cal.add(Calendar.MINUTE, 5);
					endCal.add(Calendar.MINUTE, 5);

					jsonList.add(jsonObject);
				}
			}

			resultMap.put("dataList", jsonList);
		} catch (Exception e) {
			LogUtil.error("异常:", e);
		}

		return resultMap;
	}

	@SuppressWarnings("unchecked")
	public JSONObject gameStep(String gameSite, String agent) {
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		JSONObject jsonObject = new JSONObject();

		try {
			IBaseDataService baseDataService = GCCContext.getInstance().getServiceCollection().getBaseDataService();
			List<String> gameSites = baseDataService.getGameSites(gameSite, agent);
			
			int totalNum = 0;// 总创角数
			
			Map<Integer, Integer> taskMap = new HashMap<Integer, Integer>();
			for (String gSite : gameSites) {
				String num = RedisUtil.getValue("log@"+gSite+"@TotalPlayerNum");
				if(num != null && !num.equals("")){
					totalNum += Integer.valueOf(num);
				}
				
				Map<String, String> map = RedisUtil.getAllValueOfMap("log@"+gSite+"@GameStepMap");
				if(map != null){
					for(Map.Entry<String, String> entry : map.entrySet()){
						int taskId = Integer.valueOf(entry.getKey());
						int pnum = Integer.valueOf(entry.getValue());
						
						Integer pnums = taskMap.get(taskId);
						if(pnums == null){
							pnums = 0;
						}
						pnums += pnum;
						taskMap.put(taskId, pnums);
					}
				}
			}
			
			List<BaseGameStep> gameSteps = (List<BaseGameStep>)CacheService.getFromCache(CacheConstant.B_GAME_STEP);
			
			int lastNum = totalNum;
			for(BaseGameStep gameStep : gameSteps){
				
				Integer num = taskMap.get(gameStep.getId());
				if(num == null) num = 0;
				
				String stepName = gameStep.getTaskName();
				if (stepName != null) {
					int sindex = stepName.indexOf("[");
					if (sindex != -1) {
						stepName = stepName.substring(0, sindex);
					}

					JSONObject json = new JSONObject();
					json.put("gameStep", gameStep.getId());
					json.put("stepName", stepName);
					json.put("num", num);
					// 上一节点人数 - 节点人数 / 上一节点人数
					double rate1 = 0;
					if (lastNum > 0) {
						rate1 = CommonUtil.formatDouble((lastNum - num) * 100f / lastNum, 2);
					}
					json.put("rate1", rate1 + "%");
					double rate2 = totalNum > 0 ? CommonUtil.formatDouble(num * 100f / totalNum, 2) : 0;
					json.put("rate2", rate2 + "%");
					jsonList.add(json);

					lastNum = num;
				}
			}
			
			jsonObject.put("dataList", jsonList);
		} catch (Exception e) {
			LogUtil.error("异常:", e);
		}
		return jsonObject;
	}

	@Override
	public JSONObject diamondData(String dateTime, String endTime, String gameSite, String agent) {
		JSONObject jsonObject = new JSONObject();
		List<JSONObject> jsonObjectList = new ArrayList<JSONObject>();

		try {
			jsonObject.put("stockDiamondData", jsonObjectList);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// List<Map<String, Object>> diamindDataList =
		// payAnalysisDAO.diamondData(dateTime, endTime, gameSite, agent);
		// try {
		// if (diamindDataList != null) {
		// for (Map<String, Object> map : diamindDataList) {
		// JSONObject obj = new JSONObject();
		// obj.put("activePlayer", map.get("ACTIVE_PLAYER").toString());
		// obj.put("diamondStockTotalNum",
		// Long.parseLong(map.get("DIAMOND_STOCK_TOTAL_NUM").toString()));
		// obj.put("diamondStockBuyNum",
		// Long.parseLong(map.get("DIAMOND_STOCK_BUY_NUM").toString()));
		// obj.put("diamondStockUseNum",
		// Long.parseLong(map.get("DIAMOND_STOCK_USE_NUM").toString()));
		// obj.put("diamondStockCreateTime",
		// DateService.dateFormat_ymd((Date)
		// map.get("DIAMOND_STOCK_CREATE_TIME")));
		// jsonObjectList.add(obj);
		// }
		// }
		//
		// jsonObject.put("stockDiamondData", jsonObjectList);
		// } catch (Exception e) {
		// LogUtil.error("异常", e);
		// }

		return jsonObject;
	}

	public JSONObject showGameData(String agent, String gameSite) {
		JSONObject jsonObject = new JSONObject();
		List<JSONObject> jsonObjectList = new ArrayList<JSONObject>();
		
		try {
			IBaseDataService baseDataService = GCCContext.getInstance().getServiceCollection().getBaseDataService();
			List<ServerConf> serverConfs = baseDataService.getServerConfs(gameSite, agent);
			
			List<Map<String, Object>> maps = payAnalysisDAO.sumNum(agent, gameSite);
			
			for (ServerConf serverConf : serverConfs) {
				JSONObject json = new JSONObject();
				
				int regNum = 0; // 注册人数
				int pNum = 0; // 创角用户数
				int payNum = 0; // 付费人数
				int payMoney = 0; // 付费金额
				
				//游戏角色数
				int roleNum = 0;
				String roleNumStr = RedisUtil.getValue("log@"+serverConf.getGameSite()+"@TotalPlayerNum");
				if(roleNumStr != null && !roleNumStr.equals("")){
					roleNum = Integer.valueOf(roleNumStr);
				}
				
				Set<String> list = RedisUtil.getListValueOfS("log@" + serverConf.getGameSite() + "@RegisterNumSet");
				if(list != null){
					regNum = list.size();
				}
				Set<String> list2 = RedisUtil.getListValueOfS("log@" + serverConf.getGameSite() + "@CreatePlayerSet");
				if(list2 != null){
					pNum = list2.size();
				}
				
				for (Map<String, Object> map : maps) {
					String site = map.get("paySite").toString();
					if(site != null && site.equals(serverConf.getGameSite())){
						payNum = Integer.valueOf(map.get("payNum").toString());
						payMoney = Integer.valueOf(map.get("payMoney").toString());
						break;
					}
				}
				String playRate = regNum > 0 ? (double) (Math.round(pNum * 10000 / regNum) / 100.0) + "%" : 0 + "%"; // 创角率

				String payRate = regNum > 0 ? (double) (Math.round(payNum * 10000 / regNum) / 100.0) + "%" : 0 + "%"; // 付费率

				String pArpu = String.format("%.2f", payNum > 0 ? (double) payMoney / payNum : 0); // 人均付费ARPU

				String nArpu = String.format("%.2f", regNum > 0 ? (double) payMoney / regNum : 0); // 人均注册ARPU

				Date findDate = DateService.parseDate(serverConf.getOpenServerDate());
				String findDateStr = DateService.dateFormatYMD(findDate);
				Set<Long> registerList = new HashSet<Long>();
				int registerNum = 0;
				int num2= 0;
				int num7= 0;
				int num14= 0;
				int num30= 0;
				List<String> list3 = RedisUtil.getValueOfList("log@" + serverConf.getGameSite() + "@RegisterLogList@"+findDateStr, 0, -1);
				if (list3 != null) {
					for (String str : list3) {
						RegisterLog registerLog = RedisUtil.deserializelist(str, new TypeToken<RegisterLog>() {});
						
						registerList.add(registerLog.getUserId());
					}
					
					if(registerList.size() > 0){
						registerNum += registerList.size();
						num2 += this.calRetain(serverConf.getGameSite(), findDate, 1, registerList);
						num7 += this.calRetain(serverConf.getGameSite(), findDate, 6, registerList);
						num14 += this.calRetain(serverConf.getGameSite(), findDate, 13, registerList);
						num30 += this.calRetain(serverConf.getGameSite(), findDate, 29, registerList);
						registerList = null;
					}
				}
				
				String type1 = String.format("%.2f", 0.00) + "%"; // 次日留存
				String type7 = String.format("%.2f", 0.00) + "%"; // 7日留存
				String type14 = String.format("%.2f", 0.00) + "%";// 14日留存
				String type30 = String.format("%.2f", 0.00) + "%";// 月留存
				
				if (registerNum > 0) {
					type1 = String.valueOf(CommonUtil.formatDouble(num2 * 100f / registerNum, 2)); // 次日留存
					type7 = String.valueOf(CommonUtil.formatDouble(num7 * 100f / registerNum, 2)); // 7日留存
					type14 = String.valueOf(CommonUtil.formatDouble(num14 * 100f / registerNum, 2));// 14日留存
					type30 = String.valueOf(CommonUtil.formatDouble(num30 * 100f / registerNum, 2));// 月留存
				}
				
				json.put("gameSite", serverConf.getGameSite());
				json.put("serverName", serverConf.getServerName());
				json.put("openServerDate", serverConf.getOpenServerDate());
				json.put("regNum", regNum);
				json.put("createNum", pNum);
				json.put("playRate", playRate);
				json.put("roleNum", roleNum);
				json.put("payNum", payNum);
				json.put("payRate", payRate);
				json.put("payMoney", payMoney);
				json.put("pArpu", pArpu);
				json.put("nArpu", nArpu);
				json.put("type1", type1);
				json.put("type7", type7);
				json.put("type14", type14);
				json.put("type30", type30);

				jsonObjectList.add(json);
			}
			jsonObject.put("gameData", jsonObjectList);
		} catch (Exception e) {
			LogUtil.error("异常", e);
		}
		
		return jsonObject;
	}

	public JSONObject showIndexTrendPlate(Date startTime, Date endTime, String agent, String gameSite) {
		JSONObject json = new JSONObject();
		List<JSONObject> jsonObjectList = new ArrayList<JSONObject>();

		try {
			IBaseDataService baseDataService = GCCContext.getInstance().getServiceCollection().getBaseDataService();
			List<String> gameSites = baseDataService.getGameSites(gameSite, agent);
			
			List<Date> findDates = DateService.findDates(startTime, endTime);
			
			for(Date findDate : findDates){
				JSONObject jsonObject = new JSONObject();
				//日期
				String findDateStr = DateService.dateFormatYMD(findDate);
				//在线峰值
				int onlineNum = 0;
				
				Set<Long> registerList = new HashSet<Long>();
				Set<Long> loginList = new HashSet<Long>();
				for (String gSite : gameSites) {
					List<String> list = RedisUtil.getValueOfList("log@" + gSite + "@RegisterLogList@" + findDateStr, 0, -1);
					if (list != null) {
						for (String str : list) {
							RegisterLog registerLog = RedisUtil.deserializelist(str, new TypeToken<RegisterLog>() {});

							registerList.add(registerLog.getUserId());
						}
					}
					
					List<String> list2 = RedisUtil.getValueOfList("log@" + gSite + "@LoginLogList@" + findDateStr, 0, -1);
					if (list2 != null) {
						for (String str : list2) {
							LoginLog loginLog = RedisUtil.deserializelist(str, new TypeToken<LoginLog>() {});

							loginList.add(loginLog.getUserId());
						}
					}
					
					List<String> list3 = RedisUtil.getValueOfList("log@" + gSite + "@FiveOnlineLogList@"+findDateStr, 0, -1);
					if (list3 != null) {
						int maxLogin = 0;
						for (String str : list3) {
							FiveOnlineLog fiveOnlineLog = RedisUtil.deserializelist(str, new TypeToken<FiveOnlineLog>() {});
							maxLogin = Math.max(maxLogin, fiveOnlineLog.getNum());
						}
						onlineNum += maxLogin;
					}
				}	
				 //新注册用户数		
				int rNum = registerList.size();
				// 登陆用户数
				int loginNum = loginList.size();
				
				// 付费金额
				int mnum = 0;
				// 新注册付费金额
				int nMnum = 0;
				
				Set<Long> payList = new HashSet<Long>();
				Set<Long> nPayList = new HashSet<Long>();
				List<Map<String, Object>> payMaps = payAnalysisDAO.sumNum(findDateStr, agent, gameSite);
				for(Map<String, Object> map : payMaps){
					long userId = Long.valueOf(map.get("userId").toString());
					int money = Integer.valueOf(map.get("money").toString());
					
					mnum += money;
					payList.add(userId);
					
					if(registerList.contains(userId)){
						nMnum += money;
						nPayList.add(userId);
					}
				}
				// 付费用户数
				int pnum = payList.size();
				// 新注册付费用户数
				int nPnum = nPayList.size();
				
				// 当日付费率
				double payRate = 0;
				if(loginNum > 0){
					payRate = (double) (Math.round(pnum * 10000.0 / loginNum) / 100.0); 
				}
				// 人均付费ARPU
				double pArpu = pnum > 0 ? (double) (mnum * 1.0 / pnum) : 0; 
				
				// 新用户付费率
				double nPayRate = 0;
				if (rNum > 0) {
					nPayRate = (double) (Math.round(nPnum * 10000.0 / rNum) / 100.0);
				}
				// 新用户付费ARPU
				double nParpu = nPnum > 0 ? (double) (nMnum * 1.0 / nPnum) : 0; 
				
				jsonObject.put("date", findDateStr);
				jsonObject.put("onlinePeak",onlineNum);
				
				jsonObject.put("loginNum", loginNum);
				jsonObject.put("pnum", pnum);
				jsonObject.put("mnum", mnum);
				jsonObject.put("payRate", String.format("%.2f", payRate) + "%");
				jsonObject.put("pArpu", String.format("%.2f", pArpu));
				
				jsonObject.put("rnum", rNum);
				jsonObject.put("nPnum", nPnum);
				jsonObject.put("nMnum", nMnum);
				jsonObject.put("nPayRate", String.format("%.2f", nPayRate) + "%");
				jsonObject.put("nParpu", String.format("%.2f", nParpu));
				
				jsonObjectList.add(jsonObject);
			}
			json.put("indexTrendPlate", jsonObjectList);
		} catch (Exception e) {
			LogUtil.error("异常:", e);
		}
		return json;
	}

	@Override
	public JSONObject activeMonitor(String startTime, String endTime, String agent, String gameSite) {
		JSONObject jsonObject = new JSONObject();
		List<JSONObject> jsonObjectList = new ArrayList<JSONObject>();

		try {
			jsonObject.put("activeMonitor", jsonObjectList);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// List<Map<String, Object>> maps =
		// retainAnalysisDAO.monsterActive(startTime, endTime, agent, gameSite);
		// try {
		// if (maps != null) {
		//
		// for (Map<String, Object> map : maps) {
		// JSONObject obj = new JSONObject();
		//
		// //时间
		// obj.put("CREATE_TIME", DateService.dateFormat_ymd((Date)
		// map.get("CREATE_DATE")));
		// obj.put("LOGINNUM", ((Integer) map.get("LOGINNUM")).intValue());
		// double a1 = Double.parseDouble(map.get("A1").toString());
		// obj.put("A1", String.format("%.2f", a1));
		// double a2 = Double.parseDouble(map.get("A2").toString());
		// obj.put("A2", String.format("%.2f", a2));
		// double a3 = Double.parseDouble(map.get("A3").toString());
		// obj.put("A3", String.format("%.2f", a3));
		// double a4 = Double.parseDouble(map.get("A4").toString());
		// obj.put("A4", String.format("%.2f", a4));
		// double a5 = Double.parseDouble(map.get("A5").toString());
		// obj.put("A5", String.format("%.2f", a5));
		// double a6 = Double.parseDouble(map.get("A6").toString());
		// obj.put("A6", String.format("%.2f", a6));
		//
		// jsonObjectList.add(obj);
		//
		// }
		// jsonObject.put("activeMonitor", jsonObjectList);
		// }
		// } catch (Exception e) {
		// LogUtil.error("异常：", e);
		// return jsonObject;
		// }

		return jsonObject;
	}

	public JSONObject onlineTime(Date startTime, Date endTime, String agent, String gameSite) {
		JSONObject jsonObject = new JSONObject();
		List<JSONObject> jsonObjectList = new ArrayList<JSONObject>();

		try {
			IBaseDataService baseDataService = GCCContext.getInstance().getServiceCollection().getBaseDataService();
			List<String> gameSites = baseDataService.getGameSites(gameSite, agent);
			
			List<Date> findDates = DateService.findDates(startTime, endTime);
			
			for(Date findDate : findDates){
				JSONObject obj = new JSONObject();
				//日期
				String findDateStr = DateService.dateFormatYMD(findDate);
				/** 登录玩家数*/
				int loginNum = 0;
				int num1 = 0;
				int num5 = 0;
				int num10 = 0;
				int num20 = 0;
				int num30 = 0;
				int num40 = 0;
				int num50 = 0;
				int num60 = 0;
				int h5 = 0;
				int h10 = 0;
				int uph10 = 0;
				
				for (String gSite : gameSites) {
					List<String> list = RedisUtil.getValueOfList("log@" + gSite + "@OnlineTimeLogList@" + findDateStr, 0, -1);
					if (list != null) {
						for (String str : list) {
							OnlineTimeLog onlineTimeLog = RedisUtil.deserializelist(str, new TypeToken<OnlineTimeLog>() {});
							if(onlineTimeLog != null){
								loginNum += onlineTimeLog.getLoginNum();
								num1 += onlineTimeLog.getNum1();
								num5 += onlineTimeLog.getNum5();
								num10 += onlineTimeLog.getNum10();
								num20 += onlineTimeLog.getNum20();
								num30 += onlineTimeLog.getNum30();
								num40 += onlineTimeLog.getNum40();
								num50 += onlineTimeLog.getNum50();
								num60 += onlineTimeLog.getNum60();
								h5 += onlineTimeLog.getH5();
								h10 += onlineTimeLog.getH10();
								uph10 += onlineTimeLog.getUph10();
							}

						}
					}
				}
				
				if(loginNum == 0) continue;
				
				obj.put("date", findDateStr);
				obj.put("loginNum", loginNum);
				obj.put("num1", num1);
				obj.put("num5", num5);
				obj.put("num10", num10);
				obj.put("num20", num20);
				obj.put("num30", num30);
				obj.put("num40", num40);
				obj.put("num50", num50);
				obj.put("num60", num60);
				obj.put("h5", h5);
				obj.put("h10", h10);
				obj.put("uph10", uph10);

				jsonObjectList.add(obj);
			}
			jsonObject.put("onlineTimeList", jsonObjectList);
		} catch (JSONException e) {
			LogUtil.error("异常：", e);
		}

		return jsonObject;
	}
	
	public JSONObject loginUserContent(Date startTime, Date endTime, String agent, String gameSite) {
		JSONObject jsonObject = new JSONObject();
		List<JSONObject> jsonObjectList = new ArrayList<JSONObject>();

		try {
			IBaseDataService baseDataService = GCCContext.getInstance().getServiceCollection().getBaseDataService();
			List<String> gameSites = baseDataService.getGameSites(gameSite, agent);
			
			List<Date> findDates = DateService.findDates(startTime, endTime);
			
			for(Date findDate : findDates){
				JSONObject obj = new JSONObject();
				//日期
				String findDateStr = DateService.dateFormatYMD(findDate);

				Set<Long> registerList = new HashSet<Long>();
				Set<Long> loginList = new HashSet<Long>();
				for (String gSite : gameSites) {
					List<String> list = RedisUtil.getValueOfList("log@" + gSite + "@RegisterLogList@" + findDateStr, 0, -1);
					if (list != null) {
						for (String str : list) {
							RegisterLog registerLog = RedisUtil.deserializelist(str, new TypeToken<RegisterLog>() {});

							registerList.add(registerLog.getUserId());
						}
					}
					
					List<String> list2 = RedisUtil.getValueOfList("log@" + gSite + "@LoginLogList@" + findDateStr, 0, -1);
					if (list2 != null) {
						for (String str : list2) {
							LoginLog loginLog = RedisUtil.deserializelist(str, new TypeToken<LoginLog>() {});

							loginList.add(loginLog.getUserId());
						}
					}
					
				}	
				 //新注册用户数		
				int rNum = registerList.size();
				// 登陆用户数
				int loginNum = loginList.size();
				
				obj.put("CREATE_TIME", findDateStr);
				obj.put("loginNum", loginNum);
				obj.put("registerNum", rNum);
				obj.put("oldUser", loginNum - rNum);
				
				jsonObjectList.add(obj);
			}
			jsonObject.put("loginUserContent", jsonObjectList);
		} catch (JSONException e) {
			LogUtil.error("异常：", e);
		}

		return jsonObject;
	}
	
	public JSONObject payOrder(String startTime, String endTime, String gameSite, String agent) {
		JSONObject resultMap = new JSONObject();

		try {
			List<JSONObject> jsonList = new ArrayList<JSONObject>();
			List<Map<String, Object>> resultList = payAnalysisDAO.payOrder(startTime, endTime, gameSite, agent);

			if (resultList != null) {
				for (Map<String, Object> map : resultList) {
					JSONObject json = new JSONObject();
					json.put("logId", map.get("logId"));
					json.put("userId", map.get("userId"));
					json.put("playerId", map.get("playerId"));
					json.put("paySite", map.get("paySite"));
					json.put("outOrderNo", map.get("outOrderNo"));
					json.put("orderNo", map.get("orderNo"));
					json.put("money", map.get("money"));
					json.put("payType", map.get("payType"));
					json.put("payItemId", map.get("payItemId"));
					json.put("payUrl", map.get("payUrl"));
					json.put("state", map.get("state"));
					json.put("createTime", map.get("createTime"));

					jsonList.add(json);
				}
			}
			resultMap.put("dataList", jsonList);
		} catch (Exception e) {
			LogUtil.error("异常:", e);
		}

		return resultMap;
	}

	public JSONObject keyAccount(String gameSite, String agent) {
		JSONObject resultMap = new JSONObject();
		try {
			List<JSONObject> jsonList = new ArrayList<JSONObject>();
			List<Map<String, Object>> resultList = payAnalysisDAO.keyAccount(gameSite, agent);

			if (resultList != null) {
				for (Map<String, Object> map : resultList) {
					JSONObject json = new JSONObject();
					json.put("uid", map.get("uid"));
					json.put("paySites", map.get("paySites"));
					json.put("summoney", map.get("summoney"));
					json.put("sevenPay", map.get("sevenPay"));
					json.put("monthPay", map.get("monthPay"));
					json.put("lastTime", map.get("lastTime"));

					jsonList.add(json);
				}
			}
			resultMap.put("dataList", jsonList);
		} catch (Exception e) {
			LogUtil.error("异常:", e);
		}

		return resultMap;
	}

	@Override
	public JSONObject accountInfo(String gameSite, String playerId) {
		JSONObject resultMap = new JSONObject();

		try {
			List<JSONObject> jsonList = new ArrayList<JSONObject>();
			List<Map<String, Object>> resultList = payAnalysisDAO.accountInfo(gameSite, playerId);

			if (resultList != null) {
				for (Map<String, Object> map : resultList) {
					JSONObject json = new JSONObject();
					json.put("name", map.get("NAME"));
					json.put("career", map.get("CAREER"));
					json.put("level", map.get("LEVEL"));
					json.put("fightVal", map.get("BATTLE_VALUE"));
					json.put("vip", map.get("VIP"));
					json.put("rank", map.get("RANK"));
					json.put("position", map.get("POSITION"));
					json.put("diamond", map.get("DIAMOND"));
					json.put("bind_diamond", map.get("BIND_DIAMOND"));
					json.put("regdate", map.get("REGDATE"));
					json.put("lognum", map.get("LOGMUM"));
					json.put("logtime", map.get("LOGTIME"));
					json.put("tel", map.get("tel"));

					jsonList.add(json);
				}
			}
			resultMap.put("dataList", jsonList);
		} catch (Exception e) {
			LogUtil.error("异常:", e);
		}

		return resultMap;
	}

	public JSONObject serverPay(String startTime, String endTime, String gameSite, String agent) {
		JSONObject resultMap = new JSONObject();

		try {
			List<JSONObject> jsonList = new ArrayList<JSONObject>();
			List<Map<String, Object>> resultList = payAnalysisDAO.serverPay(startTime, endTime, gameSite, agent);

			if (resultList != null) {
				Map<String, JSONObject> results = new HashMap<String, JSONObject>();
				JSONObject json;

				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Calendar startDate = Calendar.getInstance();
				Calendar endDate = Calendar.getInstance();
				startDate.setTime(df.parse(startTime));
				endDate.setTime(df.parse(endTime));

				int intervalDay = (int) ((endDate.getTimeInMillis() - startDate.getTimeInMillis())
						/ (24 * 60 * 60 * 1000));

				for (Map<String, Object> map : resultList) {
					String siteName = map.get("site_name").toString();

					if (results.containsKey(siteName)) {
						json = results.get(siteName);
						json.put(map.get("date").toString(), map.get("summoney"));
					} else {
						json = new JSONObject();
						json.put("site_name", siteName);
						for (int i = 0; i <= intervalDay; i++) {
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(startDate.getTime());
							calendar.set(Calendar.DAY_OF_YEAR, (startDate.get(Calendar.DAY_OF_YEAR) + i));
							json.put(df.format(calendar.getTime()), 0);
						}
						json.put(map.get("date").toString(), map.get("summoney"));
						results.put(siteName, json);
						jsonList.add(json);
					}
				}
			}
			resultMap.put("dataList", jsonList);
		} catch (Exception e) {
			LogUtil.error("异常:", e);
		}

		return resultMap;
	}

	private Calendar checkCalendar(String searchTime) {
		Calendar cal = Calendar.getInstance();
		String[] arr = searchTime.split("-");
		cal.set(Calendar.YEAR, Integer.parseInt(arr[0]));
		cal.set(Calendar.MONTH, Integer.parseInt(arr[1]) - 1);
		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(arr[2]));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}

	private Map<Long, Integer> listToMap(List<Map<String, Object>> resultList) throws ParseException {
		Map<Long, Integer> fiveMap = new HashMap<Long, Integer>();

		if (resultList != null && !resultList.isEmpty()) {
			for (Map<String, Object> map : resultList) {
				Date createTime = DateService.parseDate(map.get("date").toString());
				Integer num = Integer.parseInt(map.get("num").toString());
				fiveMap.put(createTime.getTime() / 1000, num);
			}
		}

		return fiveMap;
	}

	public void cacheFiveMinData(JSONObject fiveOnlineData, JSONObject fiveRegisterData, JSONObject fivePayData,
			String agent, int gameID) {

		Map<Integer, Map<String, Object>> fiveDataMap = new ConcurrentHashMap<Integer, Map<String, Object>>();

		Map<String, Object> fiveData = new HashMap<String, Object>();
		fiveData.put("time", (int) (System.currentTimeMillis() / 1000));
		fiveData.put("agent", agent);
		fiveData.put("fiveOnlineData", fiveOnlineData);
		fiveData.put("fiveRegisterData", fiveRegisterData);
		fiveData.put("fivePayData", fivePayData);

		fiveDataMap.put(gameID, fiveData);

		CacheService.putToCache(CacheConstant.FIVE_MIN_DATA, fiveDataMap);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> checkFiveMinData(String agent, int gameID) {

		Map<Integer, Map<String, Object>> fiveDataMap = (Map<Integer, Map<String, Object>>) CacheService
				.getFromCache(CacheConstant.FIVE_MIN_DATA);
		if (fiveDataMap == null)
			return null;

		Map<String, Object> fiveData = fiveDataMap.get(gameID);
		if (fiveData == null)
			return null;

		int time = (int) fiveData.get("time");
		if (System.currentTimeMillis() / 1000 - time > 2 * 60)
			return null;

		String fiveAgent = (String) fiveData.get("agent");
		if (fiveAgent == null || agent == null || !fiveAgent.equals(agent))
			return null;

		return fiveData;
	}

}
