package com.hnjz.apps.base.log.service;

import com.hnjz.apps.base.dict.model.SDict;
import com.hnjz.apps.base.dict.service.DictMan;
import com.hnjz.apps.base.log.model.SLogExt;
import com.hnjz.db.page.Page;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.DateUtil;
import com.hnjz.util.StringHelper;
import org.apache.poi.hssf.usermodel.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LogExportService extends LogService {
	@SuppressWarnings("unchecked")
	public static InputStream exportExcel(Page page, String funcId, String opName, String opType, 
						String logLevel, String logType, Date beginDate, Date endDate, String fileName) throws Exception {
		
		StringBuffer sb = new StringBuffer(" from SLogExt a "+ getWhere(funcId, opName, opType, 
				logLevel, logType, beginDate, endDate) + getOrder(page));
		QueryCache qc = new QueryLog(sb.toString());
		setWhere(qc, funcId, opName, opType, logLevel, logType, beginDate, endDate);
		List<SLogExt> listLogExts = qc.list();//直接从数据库里面取数据
		return createExcelFile(listLogExts, fileName);
	}

	public static InputStream createExcelFile(List<SLogExt> listLogExts, String fileName) throws Exception { 
		FileOutputStream fout = null;
		try {
			int size = listLogExts.size();
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
				cell.setCellValue("日志类型");
				cell.setCellStyle(style);
				
				cell = row.createCell(1);
				cell.setCellValue("日志级别");
				cell.setCellStyle(style);
				
				cell = row.createCell(2);
				cell.setCellValue("日志内容");
				cell.setCellStyle(style);
				
				cell = row.createCell(2);
				cell.setCellValue("操作人ID");
				cell.setCellStyle(style);
				
				cell = row.createCell(3);
				cell.setCellValue("操作人IP");
				cell.setCellStyle(style);
				
				cell = row.createCell(4);
				cell.setCellValue("操作人姓名");
				cell.setCellStyle(style);
				
				cell = row.createCell(5);
				cell.setCellValue("操作类型");
				cell.setCellStyle(style);
				
				cell = row.createCell(6);
				cell.setCellValue("操作时间");
				cell.setCellStyle(style);
				
				cell = row.createCell(7);
				cell.setCellValue("持续时间(毫秒)");
				cell.setCellStyle(style);
				
				cell = row.createCell(8);
				cell.setCellValue("服务器IP");
				cell.setCellStyle(style);
				
				cell = row.createCell(9);
				cell.setCellValue("服务器名字");
				cell.setCellStyle(style);
				
				cell = row.createCell(10);
				cell.setCellValue("操作结果");
				cell.setCellStyle(style);
				
				int k = rowsOfSheet;
				if (size % rowsOfSheet != 0 && j == (size / rowsOfSheet)) {
					k = size % rowsOfSheet;
				}
				for (int i = 0; i < k; i ++) {
					row = sheet.createRow((int) i + 1);
					SLogExt logExt = (SLogExt) listLogExts.get(rowsOfSheet * j + i);
					SDict sDict = null;
					sDict = DictMan.getDictType("d_logtype", logExt.getLogType());
					row.createCell(0).setCellValue(sDict == null ? "" : sDict.getName());
					
					sDict = DictMan.getDictType("d_loglevel", logExt.getLogLevel());
					row.createCell(1).setCellValue(sDict == null ? "" : sDict.getName());
					
					//row.createCell(2).setCellValue(logExt.getLogData());
					row.createCell(2).setCellValue(logExt.getOpId());
					row.createCell(3).setCellValue(logExt.getOpIp());
					row.createCell(4).setCellValue(logExt.getOpName());
					
					sDict = DictMan.getDictType("操作类型", logExt.getOpType());
					row.createCell(5).setCellValue(sDict == null ? "" : sDict.getName());
					row.createCell(6).setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(logExt.getOpTime()));
					row.createCell(7).setCellValue((long)logExt.getDurationTime());
					row.createCell(8).setCellValue(logExt.getServerIp());
					row.createCell(9).setCellValue(logExt.getServerName());
					row.createCell(10).setCellValue(logExt.getOpResult());
				}
				//设置列宽
				sheet.setColumnWidth(0, 2200);
				sheet.setColumnWidth(1, 2200);
				sheet.setColumnWidth(2, 3000);
				sheet.setColumnWidth(3, 4000);
				sheet.setColumnWidth(4, 3000);
				sheet.setColumnWidth(5, 2200);
				sheet.setColumnWidth(6, 4100);
				sheet.setColumnWidth(7, 3500);
				sheet.setColumnWidth(8, 4000);
				sheet.setColumnWidth(9, 3000);
				sheet.setColumnWidth(10, 3000);
				fout = new FileOutputStream(fileName); 
				wb.write(fout);
				fout.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fout != null) {
				fout.close();
			}
		}
		return new FileInputStream(fileName);
    }
	
	public static String getWhere(String funcId, String opName, String opType, 
			String logLevel, String logType, Date beginDate, Date endDate) {
		StringBuffer sb = new StringBuffer(" where 1=1 ");
		if (StringHelper.isNotEmpty(funcId)) {
			sb.append(" and a.funcId like :funcId ");
		}
		
		if(beginDate != null) {
			sb.append(" and opTime >=:beginDate");
		}
		
		if(endDate != null) {
			sb.append(" and opTime <:endDate");
		}
		
		if (StringHelper.isNotEmpty(opName)) { //操作人
			sb.append(" and a.opName like :opName ");
		}
		
		if(StringHelper.isNotEmpty(opType)) {
			sb.append(" and a.opType =:opType ");
		}
		
		if(StringHelper.isNotEmpty(logType)) {
			sb.append(" and a.logType =:logType ");
		}
		
		if(StringHelper.isNotEmpty(logLevel)) {
			sb.append(" and a.logLevel =:logLevel ");
		}
		
		return sb.toString();
	}
	public static String getOrder(Page page) {
		return StringHelper.isNotEmpty(page.getOrderByString()) ? page.getOrderByString() : "";
	}
	public static void setWhere(QueryCache qc, String funcId, String opName, String opType, 
			String logLevel, String logType, Date beginDate, Date endDate) throws ParseException {
		
		if(StringHelper.isNotEmpty(funcId)) {
			qc.setParameter("funcId", funcId);
		}
		
		if(beginDate != null){
			qc.setParameter("beginDate",beginDate);
		}
		
		if(endDate != null){
			qc.setParameter("endDate",DateUtil.addDate(endDate, 1));
		}
		
		if (StringHelper.isNotEmpty(opName)) {
			qc.setParameter("opName", "%" + opName.trim() + "%");
		}
		
		if(StringHelper.isNotEmpty(opType)) {
			qc.setParameter("opType", opType);
		}
		
		if(StringHelper.isNotEmpty(logType)) {
			qc.setParameter("logType", logType);
		}
		
		if(StringHelper.isNotEmpty(logLevel)) {
			qc.setParameter("logLevel", logLevel);
		}
	}
}