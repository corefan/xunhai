package com.domain.ai;

import java.io.Serializable;

/**
 * 节点
 * @author ken
 * @date 2016-12-20
 */
public class Node implements Serializable {

	private static final long serialVersionUID = 7723816142632322915L;
	
	// X坐标
	private int x;  
	// z坐标 
	private int y; 

	public Node(int x,int y) {       
		this.x=x;  
		this.y=y;  
	}  
	
	public int getX() {  
		return x;  
	}  
	public void setX(int x) {  
		this.x = x;  
	}  
	public int getY() {  
		return y;  
	}  
	public void setY(int y) {  
		this.y = y;  
	}  
}
