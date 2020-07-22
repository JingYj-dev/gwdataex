package com.hnjz.apps.base.log.service;

import com.hnjz.apps.base.dict.model.SDict;
import com.hnjz.apps.base.dict.service.DictMan;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.StringHelper;
import org.apache.poi.hssf.usermodel.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LogFxExportService {

	public static InputStream exportExcel(Integer fxDate, String eventCodes,
			String fileName) throws Exception {
		List<Object[]> tjList = null;
		if (fxDate != null && StringHelper.isNotEmpty(eventCodes)) {
			String[] codes = eventCodes.split(",");
			tjList = new ArrayList<Object[]>();
			StringBuffer sb = new StringBuffer(" select a.event_type,");
			sb.append("sum(case when(a.op_time>='"+fxDate+"-01-01' and a.op_time<'"+fxDate+"-02-01') then 1 else 0 end) as 一月,");
			sb.append("sum(case when(a.op_time>='"+fxDate+"-02-01' and a.op_time<'"+fxDate+"-03-01') then 1 else 0 end) as 二月,");
			sb.append("sum(case when(a.op_time>='"+fxDate+"-03-01' and a.op_time<'"+fxDate+"-04-01') then 1 else 0 end) as 三月,");
			sb.append("sum(case when(a.op_time>='"+fxDate+"-04-01' and a.op_time<'"+fxDate+"-05-01') then 1 else 0 end) as 四月,");
			sb.append("sum(case when(a.op_time>='"+fxDate+"-05-01' and a.op_time<'"+fxDate+"-06-01') then 1 else 0 end) as 五月,");
			sb.append("sum(case when(a.op_time>='"+fxDate+"-06-01' and a.op_time<'"+fxDate+"-07-01') then 1 else 0 end) as 六月,");
			sb.append("sum(case when(a.op_time>='"+fxDate+"-07-01' and a.op_time<'"+fxDate+"-08-01') then 1 else 0 end) as 七月,");
			sb.append("sum(case when(a.op_time>='"+fxDate+"-08-01' and a.op_time<'"+fxDate+"-09-01') then 1 else 0 end) as 八月,");
			sb.append("sum(case when(a.op_time>='"+fxDate+"-09-01' and a.op_time<'"+fxDate+"-10-01') then 1 else 0 end) as 九月,");
			sb.append("sum(case when(a.op_time>='"+fxDate+"-10-01' and a.op_time<'"+fxDate+"-11-01') then 1 else 0 end) as 十月,");
			sb.append("sum(case when(a.op_time>='"+fxDate+"-11-01' and a.op_time<'"+fxDate+"-12-01') then 1 else 0 end) as 十一月,");
			sb.append("sum(case when(a.op_time>='"+fxDate+"-12-01' and a.op_time<'"+(fxDate+1)+"-01-01') then 1 else 0 end) as 十二月,");
			sb.append("count(a.event_type)/12.0,count(a.event_type)");
			sb.append(" from s_log a ");
			sb.append(" where a.event_type in :typeCode");
			sb.append(" group by a.event_type order by a.event_type ");
			QueryCache qc = new QueryCache(sb.toString(), true);
			qc.setParameter("typeCode", codes);
			List<?> list = qc.listCache();
			for(int i = 0; i < codes.length; i++){
				Object[] obj = {null,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
				obj[0] = codes[i];
				if(list != null && list.size() > 0){
					for(int j = 0; j < list.size(); j++){
						Object[] obj2 = (Object[]) list.get(j);
						if((String.valueOf(obj2[0])).equals(obj[0])){
							obj = obj2;
						}
					}
				}
				tjList.add(obj);
			}
			return createExcelFile(tjList, fileName);
		} else
			return null;
	}
	
	public static void setWhere(QueryCache qc, Integer year, Integer month) {
		String beginTime, endTime;
		if (month == 12) {
			beginTime = "" + year + "-12-01";
			endTime = "" + (year + 1) + "-01-01";
		} else {
			beginTime = "" + year + "-" + month + "-01";
			endTime = "" + year + "-" + (month + 1) + "-01";
		}
		qc.setParameter("beginTime", beginTime);
		qc.setParameter("endTime", endTime);
	}
	
	public static InputStream createExcelFile(List<Object[]> tjList, String fileName) throws Exception { 
		FileOutputStream fout = null;
		try {
			int size = tjList.size();
			HSSFWorkbook wb = new HSSFWorkbook();
			int sheets = 1;
			for (int j = 0; j < sheets; j ++) {
				HSSFSheet sheet = wb.createSheet("日志_" + j);
				HSSFRow row = sheet.createRow((int) 0);
				HSSFCellStyle style = wb.createCellStyle();
				style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				
				HSSFCell cell = row.createCell(0);
				cell.setCellValue("事件类型");
				cell.setCellStyle(style);
				
				cell = row.createCell(1);
				cell.setCellValue("一月");
				cell.setCellStyle(style);
				
				cell = row.createCell(2);
				cell.setCellValue("二月");
				cell.setCellStyle(style);
				
				cell = row.createCell(3);
				cell.setCellValue("三月");
				cell.setCellStyle(style);
				
				cell = row.createCell(4);
				cell.setCellValue("四月");
				cell.setCellStyle(style);
				
				cell = row.createCell(5);
				cell.setCellValue("五月");
				cell.setCellStyle(style);
				
				cell = row.createCell(6);
				cell.setCellValue("六月");
				cell.setCellStyle(style);
				
				cell = row.createCell(7);
				cell.setCellValue("七月");
				cell.setCellStyle(style);
				
				cell = row.createCell(8);
				cell.setCellValue("八月");
				cell.setCellStyle(style);
				
				cell = row.createCell(9);
				cell.setCellValue("九月");
				cell.setCellStyle(style);
				
				cell = row.createCell(10);
				cell.setCellValue("十月");
				cell.setCellStyle(style);
				
				cell = row.createCell(11);
				cell.setCellValue("十一月");
				cell.setCellStyle(style);
				
				cell = row.createCell(12);
				cell.setCellValue("十二月");
				cell.setCellStyle(style);

				cell = row.createCell(13);
				cell.setCellValue("平均");
				cell.setCellStyle(style);
				
				cell = row.createCell(14);
				cell.setCellValue("合计");
				cell.setCellStyle(style);
				
				List<SDict> eventList = DictMan.getDictType("d_eventtype");
				
				Object[] sum = {"合计",0,0,0,0,0,0,0,0,0,0,0,0,0.0,0};
				
				for (int i = 0; i < size; i ++) {
					row = sheet.createRow((int) i + 1);
					Object[] obj =  (Object[]) tjList.get(i);
					
					String dictName = "";
					for(SDict sDict : eventList){
						if(sDict.getCode().equals(String.valueOf(obj[0]))){
							dictName = sDict.getName();
							break;
						}
					}
					row.createCell(0).setCellValue(dictName);
					
					row.createCell(1).setCellValue(Integer.valueOf(obj[1].toString())); sum[1] = (Integer)sum[1] +  Integer.valueOf(obj[1].toString());
					row.createCell(2).setCellValue(Integer.valueOf(obj[2].toString())); sum[2] = (Integer)sum[2] +  Integer.valueOf(obj[2].toString());
					row.createCell(3).setCellValue(Integer.valueOf(obj[3].toString())); sum[3] = (Integer)sum[3] +  Integer.valueOf(obj[3].toString());
					row.createCell(4).setCellValue(Integer.valueOf(obj[4].toString())); sum[4] = (Integer)sum[4] +  Integer.valueOf(obj[4].toString());
					row.createCell(5).setCellValue(Integer.valueOf(obj[5].toString())); sum[5] = (Integer)sum[5] +  Integer.valueOf(obj[5].toString());
					row.createCell(6).setCellValue(Integer.valueOf(obj[6].toString())); sum[6] = (Integer)sum[6] +  Integer.valueOf(obj[6].toString());
					row.createCell(7).setCellValue(Integer.valueOf(obj[7].toString())); sum[7] = (Integer)sum[7] +  Integer.valueOf(obj[7].toString());
					row.createCell(8).setCellValue(Integer.valueOf(obj[8].toString())); sum[8] = (Integer)sum[8] +  Integer.valueOf(obj[8].toString());
					row.createCell(9).setCellValue(Integer.valueOf(obj[9].toString())); sum[9] = (Integer)sum[9] +  Integer.valueOf(obj[9].toString());
					row.createCell(10).setCellValue(Integer.valueOf(obj[10].toString())); sum[10] = (Integer)sum[10] +  Integer.valueOf(obj[10].toString());
					row.createCell(11).setCellValue(Integer.valueOf(obj[11].toString())); sum[11] = (Integer)sum[11] +  Integer.valueOf(obj[11].toString());
					row.createCell(12).setCellValue(Integer.valueOf(obj[12].toString())); sum[12] = (Integer)sum[12] +  Integer.valueOf(obj[12].toString());
					row.createCell(13).setCellValue(Double.valueOf(TjNumberFormat.format(Double.valueOf(obj[13].toString()))));
					row.createCell(14).setCellValue(Integer.valueOf(obj[14].toString())); sum[14] = (Integer)sum[14] +  Integer.valueOf(obj[14].toString());
				}
				
				sum[13] = (Integer)sum[14]*1.0/12;
				
				row = sheet.createRow(size + 1);
				row.createCell(0).setCellValue((String)sum[0]);
				row.createCell(1).setCellValue(Integer.valueOf(sum[1].toString()));
				row.createCell(2).setCellValue(Integer.valueOf(sum[2].toString()));
				row.createCell(3).setCellValue(Integer.valueOf(sum[3].toString()));
				row.createCell(4).setCellValue(Integer.valueOf(sum[4].toString()));
				row.createCell(5).setCellValue(Integer.valueOf(sum[5].toString()));
				row.createCell(6).setCellValue(Integer.valueOf(sum[6].toString()));
				row.createCell(7).setCellValue(Integer.valueOf(sum[7].toString()));
				row.createCell(8).setCellValue(Integer.valueOf(sum[8].toString()));
				row.createCell(9).setCellValue(Integer.valueOf(sum[9].toString()));
				row.createCell(10).setCellValue(Integer.valueOf(sum[10].toString()));
				row.createCell(11).setCellValue(Integer.valueOf(sum[11].toString()));
				row.createCell(12).setCellValue(Integer.valueOf(sum[12].toString()));
				row.createCell(13).setCellValue(Double.valueOf(TjNumberFormat.format((Double)sum[13])));
				row.createCell(14).setCellValue(Integer.valueOf(sum[14].toString()));
				
				//设置列宽
				sheet.setColumnWidth(0, 4500);
				
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
