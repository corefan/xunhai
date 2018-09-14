package com.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.constant.SceneConstant;
import com.domain.map.BaseMap;


/**
 * 九宫格帮助类
 * @author ken
 * @date 2017-3-27
 */
public class NineGridUtil {

	/**
	 * 网络格转为区域格
	 */
	public static void initGrid(BaseMap model){
		Map<Integer, List<Integer>> gridMap = new HashMap<Integer, List<Integer>>();
		
		int smallGridNum = SceneConstant.GRID_CELL_SIZE;
		//副本或者野外pk地图不使用九宫格
		if(!model.isNineGrid()){
			smallGridNum = 1000000000;
		}
		int col = 0;
		if(model.getMapColumn() % smallGridNum == 0){
			col = model.getMapColumn() / smallGridNum;
		}else{
			col = model.getMapColumn() / smallGridNum + 1;
		}
		
		int row = 0;
		if(model.getMapRow() % smallGridNum == 0){
			row = model.getMapRow() / smallGridNum;
		}else{
			row = model.getMapRow() / smallGridNum + 1;
		}
		
		int totalNum = col * row;
		
		for(int i = 1; i <= totalNum; i++){
			gridMap.put(i, getAroundGrid(i, col, totalNum));
		}
		
		model.setGridMap(gridMap);
		model.setColNum(col);
	}
	
	/**
	 * 根据坐标算出在那个格子
	 * */
	public static int calInGrid(int posX, int posZ, int colNum) {
		
		if(colNum <= 1){
			return 1;
		}
		
		int row = (posZ - 1)/SceneConstant.GRID_SIZE + 1;
		int col = (posX - 1)/SceneConstant.GRID_SIZE + 1;
		
		return (row-1)*colNum + col;
	}
	
	/**
	 * 根据当前格子得到周围的8个格子
	 * */
	private static List<Integer> getAroundGrid(int gridValue, int colNum, int totalNum) {
		
		List<Integer> aroundGridList = new ArrayList<Integer>();
		if(gridValue < 1 || gridValue > totalNum){
			System.out.println("所属格子位置越界或者不存在：" + gridValue);
			return aroundGridList;
		}
		
		// 自己
		aroundGridList.add(gridValue);
		
		// 左上
		int leftOn = gridValue - colNum - 1;
		if (leftOn > 0 && leftOn%colNum != 0) {
			aroundGridList.add(leftOn);
		}
		// 上
		int on = gridValue - colNum;
		if (on > 0) {
			aroundGridList.add(on);
		}
		// 右上
		int rightOn = gridValue - colNum + 1;
		if (rightOn > 0 && gridValue%colNum != 0) {
			aroundGridList.add(rightOn);
		}
		// 左
		int left = gridValue - 1;
		if (left > 0 && left%colNum != 0) {
			aroundGridList.add(left);
		}
		// 右 
		int right = gridValue + 1;
		if (right <= totalNum && gridValue%colNum != 0) {
			aroundGridList.add(right);
		}
		// 左下
		int leftDown = gridValue + colNum - 1;
		if (leftDown <= totalNum && leftDown%colNum != 0) {
			aroundGridList.add(leftDown);
		}
		// 下
		int down = gridValue + colNum;
		if (down <= totalNum) {
			aroundGridList.add(down);
		}
		// 右下
		int rightDown = gridValue + colNum + 1;
		if (rightDown <= totalNum && gridValue%colNum != 0) {
			aroundGridList.add(rightDown);
		}
		
		return aroundGridList;
	}
	
	/**
	 * 获取格子列表
	 * @param intersectFlag true:交集  false:差集
	 */
	public static List<Integer> getGridList(List<Integer> list1, List<Integer> list2, boolean intersectFlag){
		
		List<Integer> result = new ArrayList<Integer>();
		if(intersectFlag){
			for(Integer v : list1){
				if(list2.contains(v)){
					result.add(v);
				}
			}
		}else{
			for(Integer v : list1){
				if(!list2.contains(v)){
					result.add(v);
				}
			}
		}

	    return result;
	}
	
	public static void main(String[] args) {
	System.out.println(calInGrid(0, 100, 0));
	}
}
