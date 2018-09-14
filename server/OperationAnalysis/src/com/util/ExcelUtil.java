package com.util;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ExcelUtil {
	
	public static HSSFWorkbook buildDiamondStockExcel(JSONObject jsonObject) throws JSONException {
		HSSFWorkbook excel = new HSSFWorkbook();
		
		Sheet sheet = excel.createSheet("Sheet1");
		sheet.setColumnWidth(0, 3000);
		sheet.setColumnWidth(1, 3500);
		sheet.setColumnWidth(2, 3500);
		sheet.setColumnWidth(3, 3500);
		sheet.setColumnWidth(4, 3500);
		
		Row row = sheet.createRow((short) 0);
		row.setHeight((short) 450);
		HSSFCellStyle style_1 = createStyle(excel, true, false, HSSFCellStyle.ALIGN_CENTER);
		createCell(excel, row, 0, style_1).setCellValue("日期");
		createCell(excel, row, 1, style_1).setCellValue("库存");
		createCell(excel, row, 2, style_1).setCellValue("进");
		createCell(excel, row, 3, style_1).setCellValue("销");
		createCell(excel, row, 4, style_1).setCellValue("差额");

		JSONArray diamondData = jsonObject.getJSONArray("stockDiamondData");
		HSSFCellStyle style_2 = createStyle(excel, false, false, HSSFCellStyle.ALIGN_RIGHT);
		HSSFCellStyle style_3 = createStyle(excel, false, false, HSSFCellStyle.ALIGN_CENTER);
		for (int i = 0; i < diamondData.length(); i++) {
			Row newRow = sheet.createRow((short) (i + 1));
			newRow.setHeight((short) 450);
			JSONObject obj = diamondData.getJSONObject(i);
			String diamondStockBuyNum = obj.getString("diamondStockBuyNum");
			String diamondStockUseNum = obj.getString("diamondStockUseNum");
			int diamondBalance = Integer.parseInt(diamondStockBuyNum) - Integer.parseInt(diamondStockUseNum);
			createCell(excel, newRow, 0, style_3).setCellValue(obj.getString("diamondStockCreateTime"));
			createCell(excel, newRow, 1, style_2).setCellValue(obj.getString("diamondStockTotalNum"));
			createCell(excel, newRow, 2, style_2).setCellValue(diamondStockBuyNum);
			createCell(excel, newRow, 3, style_2).setCellValue(diamondStockUseNum);
			createCell(excel, newRow, 4, style_2).setCellValue(diamondBalance);
		}
		return excel;
	}
	
	public static HSSFWorkbook payOrderExcel(JSONObject jsonObject) throws JSONException {
		HSSFWorkbook excel = new HSSFWorkbook();
		
		Sheet sheet = excel.createSheet("Sheet1");
		sheet.setColumnWidth(0, 3000);
		sheet.setColumnWidth(1, 3000);
		sheet.setColumnWidth(2, 3000);
		sheet.setColumnWidth(3, 3000);
		sheet.setColumnWidth(4, 3500);
		sheet.setColumnWidth(5, 3500);
		sheet.setColumnWidth(6, 3000);
		sheet.setColumnWidth(7, 3000);
		sheet.setColumnWidth(8, 3000);
		sheet.setColumnWidth(9, 6000);
		sheet.setColumnWidth(10, 3000);
		sheet.setColumnWidth(11, 6000);
		
		Row row = sheet.createRow((short) 0);
		row.setHeight((short) 450);
		HSSFCellStyle style_1 = createStyle(excel, true, false, HSSFCellStyle.ALIGN_CENTER);
		createCell(excel, row, 0, style_1).setCellValue("充值记录ID");
		createCell(excel, row, 1, style_1).setCellValue("用户编号");
		createCell(excel, row, 2, style_1).setCellValue("玩家编号");
		createCell(excel, row, 3, style_1).setCellValue("支付区服");
		createCell(excel, row, 4, style_1).setCellValue("商户订单号");
		createCell(excel, row, 5, style_1).setCellValue("平台订单号");
		createCell(excel, row, 6, style_1).setCellValue("金额");
		createCell(excel, row, 7, style_1).setCellValue("支付类型");
		createCell(excel, row, 8, style_1).setCellValue("商品编号");
		createCell(excel, row, 9, style_1).setCellValue("链接参数");
		createCell(excel, row, 10, style_1).setCellValue("订单状态");
		createCell(excel, row, 11, style_1).setCellValue("支付时间");
		JSONArray data = jsonObject.getJSONArray("dataList");
		HSSFCellStyle style_2 = createStyle(excel, false, false, HSSFCellStyle.ALIGN_RIGHT);
		HSSFCellStyle style_3 = createStyle(excel, false, false, HSSFCellStyle.ALIGN_CENTER);
		for (int i = 0; i < data.length(); i++) {
			Row newRow = sheet.createRow((short) (i + 1));
			newRow.setHeight((short) 450);
			JSONObject obj = data.getJSONObject(i);
			createCell(excel, newRow, 0, style_3).setCellValue(obj.getString("logId"));
			createCell(excel, newRow, 1, style_2).setCellValue(obj.getString("userId"));
			createCell(excel, newRow, 2, style_2).setCellValue(obj.getString("playerId"));
			createCell(excel, newRow, 3, style_2).setCellValue(obj.getString("paySite"));
			createCell(excel, newRow, 4, style_2).setCellValue(obj.getString("outOrderNo"));
			createCell(excel, newRow, 5, style_2).setCellValue(obj.getString("orderNo"));
			createCell(excel, newRow, 6, style_3).setCellValue(obj.getString("money"));
			createCell(excel, newRow, 7, style_2).setCellValue(obj.getString("payType"));
			createCell(excel, newRow, 8, style_2).setCellValue(obj.getString("payItemId"));
			createCell(excel, newRow, 9, style_2).setCellValue(obj.getString("payUrl"));
			createCell(excel, newRow, 10, style_2).setCellValue(obj.getString("state"));
			createCell(excel, newRow, 11, style_2).setCellValue(obj.getString("createTime"));
			
		}
		return excel;
	}
	
	public static HSSFCellStyle createStyle(HSSFWorkbook excel, boolean isBold, boolean haveBorder, short align) {
		HSSFFont font = excel.createFont();
		font.setFontName("宋体");
		if (isBold)
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		HSSFCellStyle style = excel.createCellStyle();
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setFont(font);
		style.setAlignment(align);
		
		if (haveBorder) {
			style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		}
		
		return style;
	}
	
	public static Cell createCell(HSSFWorkbook excel, Row row, int index, HSSFCellStyle style) {
		Cell cell = row.createCell(index);
		cell.setCellStyle(style);
		return cell;
	}
}
