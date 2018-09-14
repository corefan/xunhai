package cn.ibrother.tools.data.parse;

/**
 * 数据库导出 excel
 * @author oyxz
 * @since 2011-10-13
 * @version 1.0
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cn.ibrother.tools.data.framework.BaseDataProvider;
import cn.ibrother.tools.data.init.CmdParameter;

public class Data2Excel extends BaseParse {
	public void run(CmdParameter cmdParameter) throws Exception {
		BaseDataProvider dataProvider = null;
		try {
			dataProvider = getDataProvider(cmdParameter.db, true);

			// 取出数据库中所有表名
			List<String> tableList = dataProvider.getTables();

			// 遍历所有表
			for (String tableName : tableList) {
				log.info("start export: ", tableName);
				// 遍历所有字段
				List<String> fileds = dataProvider.getFieldnames(tableName);

				// 取出所有记录
				List<Map<String, Object>> resultList = dataProvider
						.getListBySql(String.format("select * from `%s` ", tableName));
				
				// 生成excel文件
				File dir = new File(cmdParameter.path);
				if (!dir.exists()) {
					// 创建目录
					dir.mkdirs();	
				}
				
				File file = new File(cmdParameter.path + tableName + ".xlsx");
				
				try {
					file.createNewFile();

					OutputStream os = new FileOutputStream(file);

					int nCount = exportData(tableName, os, fileds, resultList);
					
					log.info(String.format("total export:%d", nCount));
				} catch (IOException e) {
					log.info("export fail.");
					throw new Exception(e);
				}
				log.info("end export.");
				++ count;
			}
		} catch (SQLException e) {
			throw new Exception(e);
		} finally {
			try {
				dataProvider.close();
			} catch (SQLException e) {
				throw new Exception(e);
			}
		}
	}

	private int exportData(String tableName, OutputStream os, List<String> fileds,
			List<Map<String, Object>> resultList) throws Exception {
		Integer nResult = 0;
		try {
			XSSFWorkbook wbook = new XSSFWorkbook();
			XSSFSheet  wsheet = wbook.createSheet(tableName);
			
			XSSFRow row1 = wsheet.createRow(0);// 创建一个行对象 
			// 设置Excel表头
			for (int i = 1; i <= fileds.size(); i++) {

				XSSFCell cell = row1.createCell(i);// 创建单元格
				cell.setCellValue(fileds.get(i-1));
			}

			if (resultList != null && resultList.size() > 0) {
				
				int row = 1; // 第0行用于显示字段名称
				for (Map<String, Object> rsMap : resultList) {
					XSSFRow fRow = wsheet.createRow(row);// 创建一个行对象 
					int col = 0;
					for (String fieldName : fileds) {
						String sValue = String.valueOf(rsMap.get(fieldName));

						// boolean
						if (rsMap.get(fieldName) instanceof Boolean) {
							// TINTYINT 类型会自动转成boolean型,回转成INTEGER
							if (sValue.equalsIgnoreCase("true")) {
								sValue = "1";
							} else {
								sValue = "0";
							}
						}
						// datetime
						// bit
						XSSFCell cell = fRow.createCell(col++);// 创建单元格 
						cell.setCellValue(sValue);

					}
					++row;
				}
				nResult = row - 1;
			}

			wbook.write(os);
			wbook.close();
			os.close();
		} catch (Exception e) {
			throw new Exception(e);
		}
		return nResult;
	}
}
