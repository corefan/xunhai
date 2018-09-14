package com.util;

import java.util.Comparator;

import com.domain.bag.PlayerBag;
import com.domain.bag.PlayerEquipment;
import com.domain.enemy.PlayerEnemy;
import com.domain.family.PlayerFamily;
import com.domain.mail.MailInbox;
import com.domain.puppet.EnemyModel;
import com.domain.team.Team;



/**
 * 比较帮助类
 * @author ken.li
 *
 */
public class ComparatorUtil implements Comparator<Object>{

	/** 升序(默认) */
	public static final int UP = 1;
	/** 降序 */
	public static final int DOWN = -1;
	
	/** 排序方式 */
	private int type = 1;
	
	public ComparatorUtil(){}
	
	public ComparatorUtil(int type) {
		this.type = type;
	}
	
	/**
	 * 入口
	 * 注:默认升序
	 * */ 
	@Override
	public int compare(Object obj1, Object obj2) {
		if (obj1 instanceof String) {
			return compare((String) obj1, (String) obj2);
		} else if (obj1 instanceof Integer) {
			return compare(Long.parseLong(obj1.toString()), Long.parseLong(obj2.toString()));
		} else if (obj1 instanceof Long) {
			return compare(((Long) obj1).longValue(), ((Long) obj2).longValue());
		} else if (obj1 instanceof PlayerBag) {
			return compare((PlayerBag) obj1, (PlayerBag) obj2);
		} else if (obj1 instanceof EnemyModel) {
			return compare((EnemyModel) obj1, (EnemyModel) obj2);
		} else if (obj1 instanceof MailInbox) {
			return compare((MailInbox)obj1, (MailInbox)obj2);
		} else if (obj1 instanceof PlayerEquipment) {
			return compare((PlayerEquipment)obj1, (PlayerEquipment)obj2);
		} else if (obj1 instanceof PlayerFamily) {
			return compare((PlayerFamily)obj1, (PlayerFamily)obj2);
		} else if (obj1 instanceof PlayerEnemy) {
			return compare((PlayerEnemy)obj1, (PlayerEnemy)obj2);
		} else if (obj1 instanceof Team) {
			return compare((Team)obj1, (Team)obj2);
		} else{
			return 0;
		}
	}

	/**
	 * 整数
	 */
	private int compare(long avg1, long avg2) {
		int result = avg1 > avg2 ? 1:(avg1 < avg2 ? -1 : 0);
		return result*type;
	}

	/**
	 * 字符串
	 * */
	private int compare(String avg1, String avg2) {

		int len1 = avg1.length();
		int len2 = avg2.length();
		
		int len = Math.min(len1, len2);
		
		char[] char1 = avg1.toCharArray();
		char[] char2 = avg2.toCharArray();
		
		int pos = 0;
		while (len-- != 0) {
			char c1 = char1[pos];
			char c2 = char2[pos];
			if (c1 != c2) {
				return (c1-c2)*type;
			}
			pos++;
		}

		return (len1-len2)*type;
	}
	
	/**
	 * 敌人伤害列表
	 */
	private int compare(EnemyModel e1, EnemyModel e2){
		return compare(e1.getTotalDmg(), e2.getTotalDmg());
	}
	
	/**
	 * 收件
	 */
	private int compare(MailInbox mi1, MailInbox mi2) {
		return DOWN*compare(mi1.getMailInboxID(), mi2.getMailInboxID());
	}
	
	
	/**
	 * 家族成员等级
	 */
	private int compare(PlayerFamily p1, PlayerFamily p2) {
		return DOWN*compare(p1.getPosition(), p2.getPosition());
	}
	
	
	/**
	 * 仇敌添加时间(降序)
	 */
	private int compare(PlayerEnemy p1, PlayerEnemy p2) {
		return DOWN*compare(p1.getAddTime(), p2.getAddTime());
	}
	
	
	/**
	 * 队伍需求玩家等级(降序)
	 */
	private int compare(Team p1, Team p2) {
		int v_id = compare(p1.getMinLevel(), p2.getMinLevel());
		if (v_id != 0) {
			return v_id;
		}else{
			return compare((long)p1.getAutoMatchTime(), (long)p2.getAutoMatchTime());
		}		
	}

	/**
	 * 背包物品排序
	 * */ 
	private int compare(PlayerBag pb1, PlayerBag pb2) {
		
		int v_id = compare(pb2.getSortItemId(),pb1.getSortItemId());
		if (v_id != 0) {
			return v_id;
		}else{
			int v_bind = compare((long)pb1.getIsBinding(), (long)pb2.getIsBinding());
			if (v_bind != 0) {
				return v_bind;
			} else {
				return compare((long)pb1.getPlayerBagId(), (long)pb2.getPlayerBagId());
			}
		}
		
	}
	
}
