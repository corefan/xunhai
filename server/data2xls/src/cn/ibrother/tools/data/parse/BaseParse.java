package cn.ibrother.tools.data.parse;

/**
 * 数据解析器基类
 * @author oyxz
 * @since 2011-10-13
 * @version 1.0
 */
import java.sql.SQLException;

import cn.ibrother.tools.data.framework.BaseDataProvider;

import com.kingsoft.commons.io.Logger;

public class BaseParse {
	protected static Logger log = new Logger(BaseParse.class);
	
	protected Integer count = 0;
	
	protected String[] filterArray = {};
	
	protected BaseDataProvider getDataProvider(String dbName, boolean bAutoCommit) throws SQLException {
		String dbName2 = "proxool."+dbName;
		
		BaseDataProvider dataProvider = new BaseDataProvider(dbName2);
		dataProvider.connect(bAutoCommit);
		
		return dataProvider;
	}
	
	public Integer getCount() {
		return count;
	}
}
