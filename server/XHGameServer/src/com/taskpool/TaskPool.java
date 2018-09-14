package com.taskpool;

import java.util.HashMap;
import java.util.Map;

import com.constant.TaskConstant;

/**
 * 任务池	
 * @author ken
 * @date 2017-2-21
 */
public class TaskPool {

	private static Map<Integer, Class<? extends AbstractTask>> taskMap = null;
	
	public static void initTaskPool() {
		
		taskMap = new HashMap<Integer, Class<? extends AbstractTask>>();
		
		taskMap.put(TaskConstant.TYPE_1, Type1.class);
		taskMap.put(TaskConstant.TYPE_2, Type2.class);
		taskMap.put(TaskConstant.TYPE_3, Type3.class);
		taskMap.put(TaskConstant.TYPE_4, Type4.class);
		taskMap.put(TaskConstant.TYPE_5, Type5.class);
		taskMap.put(TaskConstant.TYPE_6, Type6.class);
		taskMap.put(TaskConstant.TYPE_7, Type7.class);
		taskMap.put(TaskConstant.TYPE_8, Type8.class);		
		taskMap.put(TaskConstant.TYPE_9, Type9.class);
		taskMap.put(TaskConstant.TYPE_10, Type10.class);		
		taskMap.put(TaskConstant.TYPE_11, Type11.class);
		taskMap.put(TaskConstant.TYPE_12, Type12.class);
		taskMap.put(TaskConstant.TYPE_13, Type13.class);
		taskMap.put(TaskConstant.TYPE_14, Type14.class);
		taskMap.put(TaskConstant.TYPE_15, Type15.class);
		taskMap.put(TaskConstant.TYPE_16, Type16.class);
		taskMap.put(TaskConstant.TYPE_17, Type17.class);
		taskMap.put(TaskConstant.TYPE_18, Type18.class);		
		taskMap.put(TaskConstant.TYPE_19, Type19.class);
		taskMap.put(TaskConstant.TYPE_20, Type20.class);		
		taskMap.put(TaskConstant.TYPE_21, Type21.class);
		taskMap.put(TaskConstant.TYPE_22, Type22.class);
		taskMap.put(TaskConstant.TYPE_23, Type23.class);
		taskMap.put(TaskConstant.TYPE_24, Type24.class);
		taskMap.put(TaskConstant.TYPE_25, Type25.class);
		taskMap.put(TaskConstant.TYPE_26, Type26.class);
		taskMap.put(TaskConstant.TYPE_27, Type27.class);
		taskMap.put(TaskConstant.TYPE_29, Type29.class);
		taskMap.put(TaskConstant.TYPE_30, Type30.class);
		taskMap.put(TaskConstant.TYPE_33, Type33.class);
	}
	
	public static Class<? extends AbstractTask> getTask(int conditionType) {
		
		
		return taskMap.get(conditionType);
	}
	
}
