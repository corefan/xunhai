package com.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.common.DateService;
import com.dao.BehaviorAnalysisDAO;
import com.service.IBehaviorAnalysisService;
import com.util.DAOFactory;
import com.util.LogUtil;

public class BehaviorAnalysisService implements IBehaviorAnalysisService {

	private BehaviorAnalysisDAO behaviorAnalysisDAO = DAOFactory.getBehaviorAnalysisDAO();

	/**
	 * 精力使用情况
	 */
	@Override
	public JSONObject energyUse(String startTime, String endTime, String gameSite, String agent) {
		JSONObject resultMap = new JSONObject();

		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		try {
			if (startTime != null && startTime != "" && endTime != "" && endTime != null) {
				List<Map<String, Object>> resultList = behaviorAnalysisDAO.energyUse(startTime, endTime, gameSite,
						agent);

				if (resultList != null) {
					for (Map<String, Object> map : resultList) {
						JSONObject json = new JSONObject();
						json.put("date", DateService.dateFormatYMD((Date) map.get("CREATE_DATE")));
						json.put("num", Integer.parseInt(map.get("NUM").toString()));
						for (int i = 1; i < 7; i++) {
							json.put("A" + i, String.format("%.2f", Double.parseDouble(map.get("A" + i).toString())));
						}
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

	/**
	 * 每日副本情况
	 */
	@Override
	public JSONObject dailyInstance(String startTime, String endTime, String gameSite, String agent) {
		JSONObject resultMap = new JSONObject();

		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		try {
			if (startTime != null && startTime != "" && endTime != "" && endTime != null) {
				List<Map<String, Object>> resultList = behaviorAnalysisDAO.dailyInstance(startTime, endTime, gameSite,
						agent);

				if (resultList != null) {
					for (Map<String, Object> map : resultList) {
						JSONObject json = new JSONObject();
						json.put("date", DateService.dateFormatYMD((Date) map.get("CREATE_DATE")));
						json.put("num", Integer.parseInt(map.get("NUM").toString()));
						for (int i = 0; i < 21; i++) {
							json.put("A" + i,
									String.format("%.2f", Double.parseDouble(map.get("EXPNUM" + i).toString())));
							json.put("B" + i,
									String.format("%.2f", Double.parseDouble(map.get("MONNUM" + i).toString())));
							json.put("C" + i,
									String.format("%.2f", Double.parseDouble(map.get("MAGNUM" + i).toString())));
						}
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

	/**
	 * 多人副本情况
	 */
	@Override
	public JSONObject multiplayerInstance(String startTime, String endTime, String gameSite, String agent) {
		JSONObject resultMap = new JSONObject();

		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		try {
			if (startTime != null && startTime != "" && endTime != "" && endTime != null) {
				List<Map<String, Object>> resultList = behaviorAnalysisDAO.multiplayerInstance(startTime, endTime,
						gameSite, agent);

				if (resultList != null) {
					for (Map<String, Object> map : resultList) {
						JSONObject json = new JSONObject();
						json.put("date", DateService.dateFormatYMD((Date) map.get("CREATE_DATE")));
						json.put("num", Integer.parseInt(map.get("NUM").toString()));
						for (int i = 1; i < 8; i++) {
							json.put("A" + i, String.format("%.2f", Double.parseDouble(map.get("A" + i).toString())));
						}
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

	/**
	 * 恶魔城情况
	 */
	@Override
	public JSONObject castlevania(String startTime, String endTime, String gameSite, String agent) {
		JSONObject resultMap = new JSONObject();

		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		try {
			if (startTime != null && startTime != "" && endTime != "" && endTime != null) {
				List<Map<String, Object>> resultList = behaviorAnalysisDAO.castlevania(startTime, endTime, gameSite,
						agent);

				if (resultList != null) {
					for (Map<String, Object> map : resultList) {
						JSONObject json = new JSONObject();
						json.put("date", DateService.dateFormatYMD((Date) map.get("CREATE_DATE")));
						json.put("num", Integer.parseInt(map.get("NUM").toString()));
						for (int i = 0; i < 11; i++) {
							json.put("A" + i, String.format("%.2f", Double.parseDouble(map.get("A" + i).toString())));
						}
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

	/**
	 * 恶魔城Boss情况
	 */
	@Override
	public JSONObject castlevaniaBoss(String startTime, String endTime, String gameSite, String agent) {
		JSONObject resultMap = new JSONObject();

		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		try {
			if (startTime != null && startTime != "" && endTime != "" && endTime != null) {
				List<Map<String, Object>> resultList = behaviorAnalysisDAO.castlevaniaBoss(startTime, endTime,
						gameSite, agent);

				if (resultList != null) {
					for (Map<String, Object> map : resultList) {
						JSONObject json = new JSONObject();
						json.put("date", DateService.dateFormatYMD((Date) map.get("CREATE_DATE")));
						int sum = 0; // 击杀总次数
						int num = 0; 
						for (int i = 1; i < 31; i++) {
							num = Integer.parseInt(map.get("A" + i).toString());
							sum = sum + num;
							json.put("A" + i, num);
						}
						json.put("sum", sum);
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

	/**
	 * 王者争霸情况
	 */
	@Override
	public JSONObject kingCompetition(String startTime, String endTime, String gameSite, String agent) {
		JSONObject resultMap = new JSONObject();

		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		try {
			if (startTime != null && startTime != "" && endTime != "" && endTime != null) {
				List<Map<String, Object>> resultList = behaviorAnalysisDAO.kingCompetition(startTime, endTime,
						gameSite, agent);

				if (resultList != null) {
					for (Map<String, Object> map : resultList) {
						JSONObject json = new JSONObject();
						json.put("date", DateService.dateFormatYMD((Date) map.get("CREATE_DATE")));
						json.put("num", Integer.parseInt(map.get("NUM").toString()));
						for (int i = 1; i < 8; i++) {
							json.put("A" + i, String.format("%.2f", Double.parseDouble(map.get("A" + i).toString())));
						}
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

	/**
	 * 竞技场情况
	 */
	@Override
	public JSONObject arena(String startTime, String endTime, String gameSite, String agent) {
		JSONObject resultMap = new JSONObject();

		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		try {
			if (startTime != null && startTime != "" && endTime != "" && endTime != null) {
				List<Map<String, Object>> resultList = behaviorAnalysisDAO.arena(startTime, endTime, gameSite, agent);

				if (resultList != null) {
					for (Map<String, Object> map : resultList) {
						JSONObject json = new JSONObject();
						json.put("date", DateService.dateFormatYMD((Date) map.get("CREATE_DATE")));
						json.put("num", Integer.parseInt(map.get("NUM").toString()));
						for (int i = 1; i < 10; i++) {
							json.put("A" + i, String.format("%.2f", Double.parseDouble(map.get("A" + i).toString())));
						}
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

	/**
	 * 炼金情况
	 */
	@Override
	public JSONObject alchemy(String startTime, String endTime, String gameSite, String agent) {
		JSONObject resultMap = new JSONObject();

		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		try {
			if (startTime != null && startTime != "" && endTime != "" && endTime != null) {
				List<Map<String, Object>> resultList = behaviorAnalysisDAO.alchemy(startTime, endTime, gameSite, agent);

				if (resultList != null) {
					for (Map<String, Object> map : resultList) {
						JSONObject json = new JSONObject();
						json.put("date", DateService.dateFormatYMD((Date) map.get("CREATE_DATE")));
						json.put("num", Integer.parseInt(map.get("NUM").toString()));
						for (int i = 1; i < 9; i++) {
							json.put("A" + i, String.format("%.2f", Double.parseDouble(map.get("A" + i).toString())));
						}
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

	/**
	 * 每日任务环数
	 */
	@Override
	public JSONObject dailyTask(String startTime, String endTime, String gameSite, String agent) {
		JSONObject resultMap = new JSONObject();

		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		try {
			if (startTime != null && startTime != "" && endTime != "" && endTime != null) {
				List<Map<String, Object>> resultList = behaviorAnalysisDAO.dailyTask(startTime, endTime, gameSite,
						agent);

				if (resultList != null) {
					for (Map<String, Object> map : resultList) {
						JSONObject json = new JSONObject();
						json.put("date", DateService.dateFormatYMD((Date) map.get("CREATE_DATE")));
						json.put("num", Integer.parseInt(map.get("NUM").toString()));
						for (int i = 1; i < 6; i++) {
							json.put("A" + i, String.format("%.2f", Double.parseDouble(map.get("A" + i).toString())));
						}
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

	/**
	 * 公会任务环数
	 */
	@Override
	public JSONObject guildTask(String startTime, String endTime, String gameSite, String agent) {
		JSONObject resultMap = new JSONObject();

		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		try {
			if (startTime != null && startTime != "" && endTime != "" && endTime != null) {
				List<Map<String, Object>> resultList = behaviorAnalysisDAO.guildTask(startTime, endTime, gameSite,
						agent);

				if (resultList != null) {
					for (Map<String, Object> map : resultList) {
						JSONObject json = new JSONObject();
						json.put("date", DateService.dateFormatYMD((Date) map.get("CREATE_DATE")));
						json.put("num", Integer.parseInt(map.get("NUM").toString()));
						for (int i = 1; i < 6; i++) {
							json.put("A" + i, String.format("%.2f", Double.parseDouble(map.get("A" + i).toString())));
						}
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
	
	/**
	 * 诅咒殿堂时长分析
	 */
	@Override
	public JSONObject temple(String startTime, String endTime, String gameSite, String agent) {
		JSONObject resultMap = new JSONObject();

		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		try {
			if (startTime != null && startTime != "" && endTime != "" && endTime != null) {
				List<Map<String, Object>> resultList = behaviorAnalysisDAO.temple(startTime, endTime, gameSite,
						agent);

				if (resultList != null) {
					for (Map<String, Object> map : resultList) {
						JSONObject json = new JSONObject();
						json.put("CREATE_DATE", DateService.dateFormatYMD((Date) map.get("CREATE_DATE")));
						json.put("SUMNUM", map.get("SUMNUM"));
						json.put("SUMNUM1", Integer.parseInt(map.get("SUMNUM1").toString()));
						json.put("A10", map.get("A10"));
						json.put("A30", map.get("A30"));
						json.put("A60", map.get("A60"));
						json.put("A90", map.get("A90"));
						json.put("A120", map.get("A120"));
						json.put("A180", map.get("A180"));
						json.put("A240", map.get("A240"));
						json.put("A300", map.get("A300"));
						json.put("A380", map.get("A380"));
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
}
