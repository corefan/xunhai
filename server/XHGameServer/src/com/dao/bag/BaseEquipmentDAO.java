package com.dao.bag;

import java.util.List;

import com.db.BaseSqlSessionTemplate;
import com.domain.bag.BaseEquipAddAttr;
import com.domain.bag.BaseEquipInherit;
import com.domain.bag.BaseEquipStrong;
import com.domain.bag.BaseEquipment;
import com.domain.battle.BasePkDrop;

/**
 * 装备基础DAO
 * @author ken
 * @date 2017-1-4
 */
public class BaseEquipmentDAO extends BaseSqlSessionTemplate {

	
	/**
	 * 取物品配置表
	 */
	public List<BaseEquipment> listBaseEquipments(){
		String equipmentSql = "select * from equipment";
		
		return this.selectList(equipmentSql, BaseEquipment.class);
	}
	
	/**
	 * 取pk掉落配置表
	 */
	public List<BasePkDrop> listBasePkDrops(){
		String pkdropSql = "select * from pkdrop";
		
		return this.selectList(pkdropSql, BasePkDrop.class);
	}
	
	/**
	 * 取装备附加属性
	 */
	public List<BaseEquipAddAttr> listBaseEquipAddAttr(){
		String addAttrSql = "select * from equipaddattr";
		return this.selectList(addAttrSql, BaseEquipAddAttr.class);
	}
	
	/**
	 * 强化配置
	 */
	public List<BaseEquipStrong> listBaseEquipStrongs(){
		String sql = "select * from equipstrong";
		return this.selectList(sql, BaseEquipStrong.class);
	}
	
	/**
	 * 传承配置
	 */
	public List<BaseEquipInherit> listBaseEquipInherits(){
		String sql = "select * from equipinherit";
		return this.selectList(sql, BaseEquipInherit.class);
	}
}
