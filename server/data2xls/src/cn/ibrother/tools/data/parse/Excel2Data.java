package cn.ibrother.tools.data.parse;

import java.io.BufferedInputStream;

/**
 * excel导入数据库(mysql) 
 * @author oyxz
 * @since 2011-10-14
 * @version 1.0
 */

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cn.ibrother.tools.data.framework.BaseDataProvider;
import cn.ibrother.tools.data.framework.EmptyObject;
import cn.ibrother.tools.data.init.CmdParameter;

public class Excel2Data extends BaseParse {
	private static final int MAX_COUNT = 1000;

	public void run(CmdParameter cmdParameter) throws Exception {
		// 从指定目录读取出所有的excel文件
		File[] files = null;

		File file = new File(cmdParameter.path);
		if (file.isDirectory()) {
			if (cmdParameter.file != null && cmdParameter.file.length() > 0) {
				filterArray = cmdParameter.file.split(" ");
			}
			files = file.listFiles(new ExcelFilter(filterArray));
		}

		if (files == null) {
			log.info("not found file.");
			return;
		}

		BaseDataProvider dataProvider = null;
		try {
			dataProvider = getDataProvider(cmdParameter.db, false);

			List<Map<String, Object>> sqlLists = new ArrayList<Map<String, Object>>();

			//数据库所有表
			List<String> tableList = dataProvider.getTables();
			
			// 遍历excel文件
			for (int i = 0; i < files.length; i++) {
				if (!files[i].isFile()) {
					continue;
				}
				
				String sTable = files[i].getName().substring(0,
						files[i].getName().length() - 5).toLowerCase();
				
				if(!tableList.contains(sTable)){
					continue;
				}
				
				sqlLists.clear();
				log.info("-----------start import: ", sTable);
			//导入所有excel字段
				// 读取单个excel内文件的内容,当前只读取第一个sheet的值,当数据量大时,需要遍历读取所有sheet内的值
				//int nCount = importData(files[i], sTable, sqlLists);
				
			//导入数据库拥有字段
				List<String> fieldNames = dataProvider.getFieldnames(sTable);
				int nCount = importData2(files[i], sTable, sqlLists,fieldNames);

				log.info(String.format("-------------total import:%d", nCount));

				// 清空表数据
				dataProvider.deleteByTable(sTable);

				if (nCount > MAX_COUNT) {
					// 1000条执行一次
					int fromIndex = 0;
					int toIndex = 0;
					while (fromIndex < nCount) {
						// 提交数据
						toIndex = Math.min(fromIndex + MAX_COUNT, nCount);

						// System.out.println(fromIndex + "," + toIndex);
						List<Map<String, Object>> lists = sqlLists.subList(
								fromIndex, toIndex);
						// System.out.println("start:" +
						// lists.get(0).get("id"));
						// System.out.println("end:"
						// + lists.get(lists.size() - 1).get("id"));

						// System.out.println("value:"
						// + lists.get(0).get("attackAddedRate"));

						dataProvider.executeInsertBatch(lists);

						dataProvider.commit();

						fromIndex += MAX_COUNT;
					}
				} else {
					dataProvider.executeInsertBatch(sqlLists);
				}

				// 10个表提交一次
				if ((i + 1) % 10 == 0) {
					dataProvider.commit();
				}

				log.info("-----------end import.");

				++count;
			}
			dataProvider.commit();
		} catch (SQLException e) {
			try {
				dataProvider.rollback();
			} catch (SQLException e1) {
				throw new Exception(e);
			}
			log.info("import fail.");
			throw new Exception(e);
		} finally {
			try {
				dataProvider.close();
			} catch (SQLException e) {
				throw new Exception(e);
			}
		}
	}

	/**
	 * 只导入数据库拥有字段的数据
	 * @param file
	 * @param sTable
	 * @param sqlLists
	 * @return
	 * @throws Exception
	 */
	private int importData2(File file, String sTable, List<Map<String, Object>> sqlLists, List<String> fieldNames) throws Exception {
		Integer nCount = 0;
		try {
			InputStream input = new FileInputStream(file);
			XSSFWorkbook book = new XSSFWorkbook(new BufferedInputStream(input));

			// 获得第一个工作表对象
			XSSFSheet sheet = book.getSheetAt(0);

			// 取第一行数据,即字段列表
			Map<Integer, String> fieldMap = new HashMap<Integer, String>();
			XSSFRow row1 = sheet.getRow(1);
			
		    for (int j = row1.getFirstCellNum(); j < 50; j++) {  
		        // 通过 row.getCell(j).toString() 获取单元格内容，  
		    	XSSFCell cell = row1.getCell(j);
				if (cell == null){
					continue;
				}
				
				String fiedName = cell.toString().trim();
				if(fiedName.equals("?")){
					continue;
				}
				if(fieldNames.contains(fiedName)){
					fieldMap.put(j, fiedName);
				}
		    } 
		    

			// 循环读值
			for (int row = 3; row < sheet.getPhysicalNumberOfRows(); row++) {
				// getRows会读出带格式,但实际上没有数据的空白行来,判断处理一下即可
				Map<String, Object> map = new HashMap<String, Object>();

				map.put("__TableName", sTable);

				XSSFRow valueRow = sheet.getRow(row);
				if(valueRow == null || valueRow.getCell(0) == null){
					break;
				}
				try {
					for(Map.Entry<Integer, String> entry : fieldMap.entrySet()){
						XSSFCell cell = valueRow.getCell(entry.getKey());
						if(cell == null){
							map.put(entry.getValue(), new EmptyObject());
							continue;
						}
						String value = cell.toString();
						
						if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
							value = String.valueOf(cell.getNumericCellValue());
						}
						
						value = value.trim();
						
						if (value.equalsIgnoreCase("null") || value.equals("")) {
							map.put(entry.getValue(), new EmptyObject());

						} else {
							map.put(entry.getValue(), value);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				nCount++;
				sqlLists.add(map);
			}
			try {
				input.close();
				book.close();
			} catch (Exception e) {
			}
		} catch (IOException e) {
			throw new Exception(e);
		}
		return nCount;
	}
	
	class ExcelFilter implements FileFilter {
		String[] filters = {};

		public ExcelFilter(String[] filterArray) {
			filters = filterArray;
		}

		@Override
		public boolean accept(java.io.File pathname) {
			String filename = pathname.getName().toLowerCase();
			if (filename.contains(".xlsx")) {
				if (filterArray.length > 0) {
					boolean bFind = false;
					// 支持指定excel的处理
					for (int i = 0; i < filterArray.length; i++) {
						if (filename.equalsIgnoreCase(filterArray[i])) {
							bFind = true;
							break;
						}
					}
					return bFind;

				} else {

					return true;
				}
			} else {
				return false;
			}
		}
	}
}