package cn.ibrother.tools.data.init;

import java.util.ArrayList;
import java.util.List;

import cn.ibrother.tools.data.filter.IDataFilter;
import cn.ibrother.tools.data.filter.SymbolDataFilter;

/**
 * 全局对象类
 * 
 * @author oyxz
 * @since 2011-10-13
 * @version 1.0
 */
public class Global {
	private static volatile List<IDataFilter> filterList = null;

	public static List<IDataFilter> getFilterList() {
		// 添加数据过滤器类
		if (filterList == null) {
			filterList = new ArrayList<IDataFilter>();
			IDataFilter dataFilter = new SymbolDataFilter();
			filterList.add(dataFilter);
		}
		return filterList;
	}
}
