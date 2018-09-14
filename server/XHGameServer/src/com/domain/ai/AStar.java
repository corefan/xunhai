package com.domain.ai;

import java.util.ArrayList;
import java.util.List;

public class AStar {

	/** 行 */ 
	private int row;
	/** 列 */  
	private int column;
	
	public AStar(int row, int column){  
		this.row= row;  
		this.column= column;  
	}  
	
	//查找路线
	public List<Node> search(int y1,int x1,int y2,int x2){  
		
		List<Node> paths = new ArrayList<Node>();
		
		if(y1<0 || y1>row || y2<0 || y2>row || x1<0 || x1>column || x2<0 || x2>column){  
			return paths;  
		}  
		/*if(map[y1][x1] == NOT_MOVE || map[y2][x2] == NOT_MOVE){
			return paths;  
		}*/  
		Node sNode=new Node(x1,y1);  
		Node eNode=new Node(x2,y2);        
		
		List<Node> resultList=search(sNode, eNode);  
		
		return resultList;  
	}  
	
	//查找核心算法  
	private List<Node> search(Node sNode,Node eNode){ 
		
		List<Node> resultList=new ArrayList<Node>(); 
		
		boolean isFind=false;  
		Node node=null;
		
		while(!isFind) {
			
			if (resultList.size() > 100) {
				isFind = true;
				break;
			}
			
			if (resultList.isEmpty()) {
				node = sNode;
			} else {
				node=resultList.get(resultList.size() - 1);  
			}
			
			//判断是否找到目标点  
			if(node.getX() == eNode.getX() && node.getY() == eNode.getY()){  
				isFind=true;  
				break;  
			}  
			
			if (node.getX() == eNode.getX()) {
				if (node.getY() > eNode.getY()) {
					//上  
					if((node.getY()-1) >= 0){               
						resultList.add(new Node(node.getX(), node.getY()-1));
					} 
				} else {
					//下  
					if((node.getY()+1) <= row){  
						resultList.add(new Node(node.getX(),node.getY()+1));  
					} 
				}
				
			} else if (node.getX() > eNode.getX()) {
				if (node.getY() == eNode.getY()) {
					//左  
					if((node.getX()-1) >= 0){  
						resultList.add(new Node(node.getX()-1,node.getY()));  
					}
				} else if (node.getY() > eNode.getY()) {
					//左上  
					if((node.getX()-1) >= 0 && (node.getY()-1) >= 0){  
						resultList.add(new Node(node.getX()-1,node.getY()-1));  
					}
				} else {
					//左下  
					if((node.getX()-1) >= 0 && (node.getY()+1) <= row){  
						resultList.add(new Node(node.getX()-1,node.getY()+1));  
					} 
				}
			} else {
				if (node.getY() == eNode.getY()) {
					//右  
					if((node.getX()+1) <= column){  
						resultList.add(new Node(node.getX()+1,node.getY()));  
					}
				} else if (node.getY() > eNode.getY()) {
					//右上  
					if((node.getX()+1) <= column && (node.getY()-1) >= 0){               
						resultList.add(new Node(node.getX()+1,node.getY()-1));  
					}
				} else {
					//右下  
					if((node.getX()+1)<= column && (node.getY()+1) <= row){  
						resultList.add(new Node(node.getX()+1,node.getY()+1));  
					}   
				}
			}
		}  
		
		return resultList;   
	}  
}
