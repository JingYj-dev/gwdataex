package com.hnjz.apps.base.log.service;

import com.hnjz.apps.base.dict.model.SDict;
import com.hnjz.apps.base.dict.service.DictMan;
import com.hnjz.apps.base.log.model.SLog;
import com.hnjz.db.page.Page;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.DateUtil;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.FunctionManager;
import org.apache.poi.hssf.usermodel.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SecLogExportService {
	@SuppressWarnings("unchecked")
	public static InputStream exportExcel(Page page, String funcId, String opName, Integer opType, 
						String opObjType,Integer eventType, Date beginDate, Date endDate, String fileName) throws Exception {
		if(endDate != null){
			endDate = DateUtil.addDate(endDate, 1);
		}
		StringBuffer sb = new StringBuffer(" from SLog a "+ getWhere(funcId, opName, opType, 
				opObjType,eventType, beginDate, endDate) + getOrder(page));
		QueryCache qc = new QueryLog(sb.toString());
		setWhere(qc, funcId, opName, opType, opObjType,eventType, beginDate, endDate);
		List<SLog> listLogs = qc.list();//直接从数据库里面取数据
		return createExcelFile(listLogs, fileName);
	}
	
	public static String getWhere(String funcId, String opName, Integer opType, 
			String opObjType,Integer eventType, Date beginDate, Date endDate) {
		StringBuffer sb = new StringBuffer(" where a.operatorType in (1,4) ");
		if (StringHelper.isNotEmpty(funcId)) {
			sb.append(" and a.funcId like :funcId ");
		}
		
		if(StringHelper.isNotEmpty(opName))
			sb.append(" and a.opName like :operName ");
		
		if(beginDate != null)
			sb.append(" and a.opTime >=:beginDate");
		
		if(endDate != null)
			sb.append(" and a.opTime <:endDate");
		
		if(opType != null)
			sb.append(" and a.opType=:operType ");
		
		if(StringHelper.isNotEmpty(opObjType))
			sb.append(" and a.opObjType=:opObjType ");
		
		if(eventType != null)
			sb.append(" and a.eventType=:eventType ");
		
		return sb.toString();
	}
	public static String getOrder(Page page) {
		return StringHelper.isNotEmpty(page.getOrderByString()) ? page.getOrderByString() : " order by opTime desc ";
	}
	public static void setWhere(QueryCache qc, String funcId, String opName, Integer opType, 
			String opObjType,Integer eventType, Date beginDate, Date endDate) throws ParseException {
		
		if(StringHelper.isNotEmpty(funcId)) {
			qc.setParameter("funcId", funcId);
		}
		
		if(StringHelper.isNotEmpty(opName))
			qc.setParameter("operName", "%" + opName.trim() + "%");
		
		if(beginDate != null)
			qc.setParameter("beginDate",beginDate);
		
		if(endDate!= null)
			qc.setParameter("endDate",endDate);
		
		if(opType != null)
			qc.setParameter("operType", opType);
		
		if(StringHelper.isNotEmpty(opObjType))
			qc.setParameter("opObjType", opObjType);
		
		if(eventType != null)
			qc.setParameter("eventType", eventType);
	}
	
	public static InputStream createExcelFile(List<SLog> listLogs, String fileName) throws Exception { 
		FileOutputStream fout = null;
		try {
			int size = listLogs.size();
			HSSFWorkbook wb = new HSSFWorkbook();
			int rowsOfSheet = 3000; //每个sheet的行数
			int sheets = 1;
			if (size > rowsOfSheet) {
				sheets = size % rowsOfSheet != 0 ? size / rowsOfSheet + 1 : size / rowsOfSheet;
			}
			for (int j = 0; j < sheets; j ++) {
				HSSFSheet sheet = wb.createSheet("日志_" + j);
				HSSFRow row = sheet.createRow((int) 0);
				HSSFCellStyle style = wb.createCellStyle();
				style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				
				HSSFCell cell = row.createCell(0);
				cell.setCellValue("功能名称");
				cell.setCellStyle(style);
				
				cell = row.createCell(1);
				cell.setCellValue("事件类型");
				cell.setCellStyle(style);
				
				cell = row.createCell(2);
				cell.setCellValue("操作人");
				cell.setCellStyle(style);
				
				cell = row.createCell(3);
				cell.setCellValue("操作人IP");
				cell.setCellStyle(style);
				
				cell = row.createCell(4);
				cell.setCellValue("操作时间");
				cell.setCellStyle(style);
				
				cell = row.createCell(5);
				cell.setCellValue("操作类型");
				cell.setCellStyle(style);
				
				cell = row.createCell(6);
				cell.setCellValue("操作对象类型");
				cell.setCellStyle(style);
				
				cell = row.createCell(7);
				cell.setCellValue("操作对象ID");
				cell.setCellStyle(style);
				
				cell = row.createCell(8);
				cell.setCellValue("关联对象类型");
				cell.setCellStyle(style);
				
				cell = row.createCell(9);
				cell.setCellValue("关联对象ID");
				cell.setCellStyle(style);
				
				cell = row.createCell(10);
				cell.setCellValue("操作结果");
				cell.setCellStyle(style);
				
				cell = row.createCell(11);
				cell.setCellValue("重要程度");
				cell.setCellStyle(style);
				
				cell = row.createCell(12);
				cell.setCellValue("内容");
				cell.setCellStyle(style);
				
				int k = rowsOfSheet;
				if (size % rowsOfSheet != 0 && j == (size / rowsOfSheet)) {
					k = size % rowsOfSheet;
				}
				
				List<SDict> eventList = DictMan.getDictType("d_eventtype");
				List<SDict> operList = DictMan.getDictType("d_opertype");
				List<SDict> objList = DictMan.getDictType("d_objtype");
				List<SDict> levelList = DictMan.getDictType("d_loglevel");
				
				for (int i = 0; i < k; i ++) {
					row = sheet.createRow((int) i + 1);
					SLog log = (SLog) listLogs.get(rowsOfSheet * j + i);
					row.createCell(0).setCellValue(FunctionManager.getFuncActionDesc(log.getFuncId()));
					
					String dictName = "";
					for(SDict sDict : eventList){
						if(sDict.getCode().equals(String.valueOf(log.getEventType()))){
							dictName = sDict.getName();
							break;
						}
					}
					row.createCell(1).setCellValue(dictName);
					
					row.createCell(2).setCellValue(log.getOpName());
					row.createCell(3).setCellValue(log.getOpIp());
					row.createCell(4).setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(log.getOpTime()));
					
					dictName = "";
					for(SDict sDict : operList){
						if(sDict.getCode().equals(String.valueOf(log.getOpType()))){
							dictName = sDict.getName();
							break;
						}
					}
					row.createCell(5).setCellValue(dictName);
					
					dictName = "";
					for(SDict sDict : objList){
						if(sDict.getCode().equals(log.getOpObjType())){
							dictName = sDict.getName();
							break;
						}
					}
					row.createCell(6).setCellValue(dictName);
					
					row.createCell(7).setCellValue(log.getOpObjId());

					dictName = "";
					for(SDict sDict : objList){
						if(sDict.getCode().equals(log.getRelObjType())){
							dictName = sDict.getName();
							break;
						}
					}
					row.createCell(8).setCellValue(dictName);
					
					row.createCell(9).setCellValue(log.getRelObjId());
					row.createCell(10).setCellValue(log.getResult());
					
					dictName = "";
					for(SDict sDict : levelList){
						if(sDict.getCode().equals(String.valueOf(log.getLogLevel()))){
							dictName = sDict.getName();
							break;
						}
					}
					row.createCell(11).setCellValue(dictName);
					
					if(log.getLogData()!=null && log.getLogData().length()>1100){
						row.createCell(12).setCellValue(log.getLogData().substring(0, 1100));
					}else{
						row.createCell(12).setCellValue(log.getLogData());
					}
				}
				//设置列宽
				sheet.setColumnWidth(0, 4000);
				sheet.setColumnWidth(1, 4000);
				sheet.setColumnWidth(2, 2500);
				sheet.setColumnWidth(3, 3800);
				sheet.setColumnWidth(4, 5000);
				sheet.setColumnWidth(5, 2500);
				sheet.setColumnWidth(6, 4000);
				sheet.setColumnWidth(7, 10000);
				sheet.setColumnWidth(8, 3500);
				sheet.setColumnWidth(9, 10000);
				sheet.setColumnWidth(10, 2500);
				sheet.setColumnWidth(11, 2500);
				sheet.setColumnWidth(12, 5000);
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
