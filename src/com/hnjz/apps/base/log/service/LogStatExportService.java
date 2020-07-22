package com.hnjz.apps.base.log.service;

import com.hnjz.apps.base.dict.model.SDict;
import com.hnjz.apps.base.dict.service.DictMan;
import com.hnjz.db.query.QueryCache;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.Region;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LogStatExportService {

	@SuppressWarnings("unchecked")
	public static InputStream exportExcel(String beginDate, String endDate,
			String fileName) throws Exception {

		if (beginDate != null && endDate != null) {
			String[] s = beginDate.split("-");
			Integer year1 = Integer.valueOf(s[0]);
			Integer month1 = Integer.valueOf(s[1]);
			s = endDate.split("-");
			Integer year2 = Integer.valueOf(s[0]);
			Integer month2 = Integer.valueOf(s[1]);
			List list = new ArrayList();

			for (int i = year1; i <= year2; i++) {
				if(i < year2){
					int j = 0;
					j = i == year1?month1:1;
					for(; j <= 12; j++){
						doSearch(list,i,j);
					}
				}else if (i == year2){
					int j = 0;
					j = i == year1?month1:1;
					for(; j <= month2; j++){
						doSearch(list,i,j);
					}
				}
			}
			return createExcelFile(list, fileName);

		} else
			return null;
	}
	
	
	public static void doSearch(List<Object[]> list,Integer i,Integer j){
		StringBuffer sb = new StringBuffer(
				" select a.event_type,sum(case when(a.log_level='1') then 1 else 0 end) as 一般,"
						+ " sum(case when(a.log_level='2') then 1 else 0 end) as 重要,"
						+ " sum(case when(a.log_level='3') then 1 else 0 end) as 严重  "
						+ " from s_log a " + getWhere(i, j));
		QueryCache qc = new QueryCache(sb.toString(), true);
		List<?> list1 = qc.listCache();
		if (list1 != null && list1.size() > 0) {
			Object[] obj3 = new Object[2];
			obj3[0] = "" + i + "-" + (j > 9 ? j : "0" + j);
			obj3[1] = list1;
			list.add(obj3);
		}
	}
	
	public static String getWhere(Integer i, Integer j) {
		StringBuffer sb = new StringBuffer(" where 1=1 ");
			sb.append(" and a.op_time >= '" + i+"-"+j+"-01'");
			
			if(j != 12){
				sb.append(" and a.op_time < '" + i+"-"+(j+1)+"-01'");
			}else {
				sb.append(" and a.op_time < '" + (i+1)+"-01-01'");
			}

			sb.append(" group by a.event_type order by a.event_type");

		return sb.toString();
	}
	
	@SuppressWarnings("deprecation")
	public static InputStream createExcelFile(List<Object[]> list3, String fileName) throws Exception { 
		FileOutputStream fout = null;
		try {
			int size = list3.size();
			HSSFWorkbook wb = new HSSFWorkbook();
			int sheets = 1;
			for (int j = 0; j < sheets; j ++) {
				HSSFSheet sheet = wb.createSheet("日志_" + j);
				HSSFRow row = sheet.createRow((int) 0);
				HSSFCellStyle style = wb.createCellStyle();
				style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				
				HSSFCell cell = row.createCell(0);
				cell.setCellValue("月份");
				cell.setCellStyle(style);
				sheet.addMergedRegion(new Region(0, (short)0, 1, (short)0));
				
				cell = row.createCell(1);
				cell.setCellValue("事件类型");
				cell.setCellStyle(style);
				sheet.addMergedRegion(new Region(0, (short)1, 1, (short)1));
				
				cell = row.createCell(2);
				cell.setCellValue("数量分布");
				cell.setCellStyle(style);
				sheet.addMergedRegion(new Region(0, (short)2, 0, (short)4));
				
				cell = row.createCell(5);
				cell.setCellValue("合计");
				cell.setCellStyle(style);
				sheet.addMergedRegion(new Region(0, (short)5, 1, (short)5));
				
				row = sheet.createRow(1);
				cell = row.createCell(2);
				cell.setCellValue("一般");
				cell.setCellStyle(style);
				
				cell = row.createCell(3);
				cell.setCellValue("重要");
				cell.setCellStyle(style);
				
				cell = row.createCell(4);
				cell.setCellValue("严重");
				cell.setCellStyle(style);
				
				List<SDict> eventList = DictMan.getDictType("d_eventtype");
				
				int num = 2;
				for (int i = 0; i < size; i ++) {
					row = sheet.createRow(num);
					Object[] obj = (Object[]) list3.get(i);
					cell = row.createCell(0);
					cell.setCellValue((String)obj[0]);
					cell.setCellStyle(style);
					
					sheet.addMergedRegion(new Region(num, (short)0, num+((List<?>)obj[1]).size(), (short)0));
					Integer s1 = 0;
					Integer s2 = 0;
					Integer s3 = 0;
					
					for(int k=0; k<((List<?>)obj[1]).size();k++){
						if(k != 0)
							row = sheet.createRow(num);
						num++;
						Object[] obj1 = (Object[]) ((List<?>)obj[1]).get(k);
						
						String dictName = "";
						for(SDict sDict : eventList){
							if(sDict.getCode().equals(String.valueOf(obj1[0]))){
								dictName = sDict.getName();
								break;
							}
						}
						row.createCell(1).setCellValue(dictName);
						
						row.createCell(2).setCellValue(Integer.valueOf(obj1[1].toString()));
						s1 += Integer.valueOf(obj1[1].toString());
						row.createCell(3).setCellValue(Integer.valueOf(obj1[2].toString()));
						s2 += Integer.valueOf(obj1[2].toString());
						row.createCell(4).setCellValue(Integer.valueOf(obj1[3].toString()));
						s3 += Integer.valueOf(obj1[3].toString());
						row.createCell(5).setCellValue(Integer.valueOf(obj1[1].toString())+Integer.valueOf(obj1[2].toString())+Integer.valueOf(obj1[3].toString()));
					}
					row = sheet.createRow(num);
					num++;
					row.createCell(1).setCellValue("合计");
					row.createCell(2).setCellValue(s1);
					row.createCell(3).setCellValue(s2);
					row.createCell(4).setCellValue(s3);
					row.createCell(5).setCellValue(s1+s2+s3);
				}
				//设置列宽
				sheet.setColumnWidth(0, 4000);
				sheet.setColumnWidth(1, 4000);
				sheet.setColumnWidth(2, 4000);
				sheet.setColumnWidth(3, 4000);
				sheet.setColumnWidth(4, 4000);
				sheet.setColumnWidth(5, 4000);
				fout = new FileOutputStream(fileName); 
				wb.write(fout);
				fout.flush();
			}
		}  finally {
			if (fout != null) {
				fout.close();
			}
		}
		return new FileInputStream(fileName);
    }

}
