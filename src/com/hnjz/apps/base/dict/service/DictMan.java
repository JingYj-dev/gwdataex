/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file DictMan.java creation date: [Jan 3, 2014 2:31:03 PM] by liuzhb
 * http://www.css.com.cn
 */

package com.hnjz.apps.base.dict.service;

import com.hnjz.apps.base.dict.model.SDict;
import com.hnjz.base.memcached.MemCachedFactory;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Json;
import com.hnjz.util.Md5Util;
import com.hnjz.util.StringHelper;
import com.hnjz.apps.base.log.model.LogConstant;
import com.hnjz.apps.base.log.model.LogPart;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

public class DictMan {
	private static Log log = LogFactory.getLog(DictMan.class);

	/**
	 * 通过字典表名获取QueryCache对象
	 * @author liuzhb on Jan 6, 2014 9:57:02 AM
	 * @param table 
	 * @return
	 */
	public static QueryCache getDictListConf(String tableName) {
		String parentId = getUuid("d_root", tableName);
		QueryCache qc = new QueryDict(" from SDict a where a.parentId = :parentId order by code").setParameter("parentId", parentId);
		return qc;
	} 

	/**
	 * 初始化并返回字典列表，并将字典列表缓存至memcached中，
	 * @author liuzhb on Jan 6, 2014 9:58:34 AM
	 * @param table
	 * @return
	 */
	private static List initDictList(String table) {
		QueryCache qc = getDictListConf(table);
		boolean isNoCache = qc.isNoCache();
		List lstRes = qc.listCache(-1);
		if (isNoCache) {
			for (Object obj : lstRes) {
				SDict dt = (SDict) obj;
				addItemCache(dt);
			}
		}
		return lstRes;
	}

	/**
	 * 通过字典表名获取值为dictId的List对象，便于通过dictId查找SDict对象
	 * @author liuzhb on Jan 6, 2014 9:59:36 AM
	 * @param table
	 * @return
	 */
	public static List getDictTypeId(String table) {
		List<SDict> lst = DictMan.getDictType(table);
		List lstId = new ArrayList();
		for (SDict dt : lst)
			lstId.add(dt.getUuid());
		return lstId;
	}

	/**
	 * 通过字典表名获取值为SDict的List对象，
	 * @author liuzhb on Jan 6, 2014 10:01:14 AM
	 * @param table
	 * @return
	 */
	public static List<SDict> getDictType(String table) {
		if (StringHelper.isEmpty(table))
			return null;
		QueryCache qc = getDictListConf(table);
		boolean isNoCache = qc.isNoCache();
		List<SDict> lstRes = qc.listCache(-1);
		if (isNoCache) {
			for (Object obj : lstRes) {
				SDict sDict = (SDict) obj;
				addItemCache(sDict);
			}
		}
		return lstRes;
	}
	
	public static String getDictName(String table, String dictId) {
		if(StringHelper.isEmpty(table) || StringHelper.isEmpty(dictId))
			return "";
		String uuid = getUuid(table, dictId);
		SDict dict =  QueryDict.get(SDict.class, uuid);
		return dict==null ? "":dict.getName();
	}

	/**
	 * 通过字典表名清理memcached中的字典缓存
	 * @author liuzhb on Jan 6, 2014 10:02:23 AM
	 * @param table
	 */
	public static void clearTableCache(String table) {
		QueryCache qc = getDictListConf(table);
		qc.listCacheRemove();
	}

	/**
	 * 通过字典表名、dictId获取单个字典对象
	 * @author liuzhb on Jan 6, 2014 10:04:39 AM
	 * @param table
	 * @param dictId
	 * @return
	 */
	public static SDict getDictType(String table, String dictId) {
		if(StringHelper.isEmpty(table) || StringHelper.isEmpty(dictId))
			return null;
		String uuid = getUuid(table, dictId);
		return QueryDict.get(SDict.class, uuid);
	}

	/**
	 * 将字典添加到memcached缓存中
	 * @author liuzhb on Jan 6, 2014 10:06:40 AM
	 * @param table
	 * @param dt
	 */
	public static void addItemCache(SDict dt) {
		String key = QueryCache.getKey(QueryDict.MAPPING, dt.getClass().getName(), dt.getUuid());
		MemCachedFactory.set(key, dt);
	}

	/**
	 * 将字典从memcached缓存清除
	 * @author liuzhb on Jan 6, 2014 10:07:07 AM
	 * @param table
	 * @param dictId
	 */
	public static void delItemCache(SDict dt) {
		String key = QueryCache.getKey(QueryDict.MAPPING, dt.getClass().toString(), dt.getUuid());
		MemCachedFactory.delete(key);
	}
	
	public static List getDictList(String table,String firstVal) {
		List dict = getDictType(table);
		List lstRes = new ArrayList();
		if(StringHelper.isNotEmpty(firstVal)){
			SDict dictType = new SDict();
			dictType.setName(firstVal);
			lstRes.add(dictType);
		}
		lstRes.addAll(dict);
		return lstRes;
	}

	/**
	 * 通过表名获取字典列表
	 * @author liuzhb on Jan 6, 2014 10:07:28 AM
	 * @param table
	 * @return
	 */
	public static List getDictListQuery(String table) {
		List dict = getDictType(table);
		List lstRes = new ArrayList();
		SDict dictType = new SDict();
		dictType.setName("全部");
		lstRes.add(dictType);
		lstRes.addAll(dict);
		return lstRes;
	}

	/**
	 * 通过表名获取字典列表（该方法用于下拉菜单中，在下拉菜单的第一个选项总添加“请选择”）
	 * @author liuzhb on Jan 6, 2014 10:07:53 AM
	 * @param table
	 * @return
	 */
	public static List getDictListSel(String table) {
		List dict = getDictType(table);
		List lstRes = new ArrayList();
		SDict dictType = new SDict();
		dictType.setName("请选择");
		lstRes.add(dictType);
		lstRes.addAll(dict);
		return lstRes;
	}
	/**
	 * 添加字典项
	 * @author liuzhb on Jan 3, 2014 11:35:57 AM
	 * @param dict
	 * @return
	 */
	public static void addDict(SDict dict) {
		TransactionCache tx = null;
		try{
			tx = new QueryDict().getTransaction();
			tx.getSession().save(dict);
			tx.commit();
			LogPart lp = new LogPart();		
			lp.setOpObjType(SDict.class.getName());
			lp.setOpObjId(dict.getUuid());
			lp.setRelObjType(SDict.class.getName());
			lp.setRelObjId(dict.getUuid());
			lp.setOpType(LogConstant.LOG_TYPE_ADD);
			lp.setLogLevel(LogConstant.LOG_LEVEL_COMMON);
			lp.setLogData(Json.object2json(dict));
			lp.setResult(LogConstant.RESULT_SUCCESS);
			lp.save();
			clearTableCache(dict.getTableName());
			addItemCache(dict);
		}catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			log.error(ex.getMessage());
		} 
	}
	/**
	 * 更新字典项
	 * @author liuzhb on Jan 3, 2014 11:35:57 AM
	 * @param dict
	 * @return
	 */
	public static boolean updateDict(SDict dict) {
		TransactionCache tx = null;
		try {
			tx = new QueryDict().getTransaction();
			tx.getSession().update(dict);
			tx.commit();
			LogPart lp = new LogPart();		
			lp.setOpObjType(SDict.class.getName());
			lp.setOpObjId(dict.getUuid());
			lp.setRelObjType(SDict.class.getName());
			lp.setRelObjId(dict.getUuid());
			lp.setOpType(LogConstant.LOG_TYPE_MODIFY);
			lp.setLogLevel(LogConstant.LOG_LEVEL_COMMON);
			lp.setLogData(Json.object2json(dict));
			lp.setResult(LogConstant.RESULT_SUCCESS);
			lp.save();
			clearTableCache(dict.getTableName());
			addItemCache(dict);
			return true;
		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			log.error(ex.getMessage());
			return false;
		} 
	}

	/**
	 * 删除多个字典项
	 * @author liuzhb on Jan 3, 2014 1:05:29 PM
	 * @param table 
	 * @param dictIdList 
	 * @return
	 */

	public static void delDict(List<String> dictIdList) {
		if(dictIdList != null && dictIdList.size() > 0){
			Map<String, List<SDict>> dictMap = new HashMap<String, List<SDict>>();
			initDictMap(dictIdList, dictMap);
			if(dictMap != null && dictMap.size() > 0){
				Set<String> set = dictMap.keySet();
				Iterator<String> iter = set.iterator();
				while(iter.hasNext()){
					String parentId = iter.next();
					List<SDict> list = dictMap.get(parentId);
					if(list != null && list.size() > 0){
						QueryCache qc = new QueryDict();
						TransactionCache tc = qc.getTransaction();
						tc.delete(list);
						tc.commit();
						for(SDict dict : list){
							LogPart lp = new LogPart();		
							lp.setOpObjType(SDict.class.getName());
							lp.setOpObjId(dict.getUuid());
							lp.setRelObjType(SDict.class.getName());
							lp.setRelObjId(dict.getUuid());
							lp.setOpType(LogConstant.LOG_TYPE_DELETE);
							lp.setLogLevel(LogConstant.LOG_LEVEL_IMPORTANT);
							lp.setLogData("");
							lp.setResult(LogConstant.RESULT_SUCCESS);
							lp.save();
						}
					}
					clearTableCache(parentId);
				}
			}
		}
	}
	
	public static void initDictMap(List<String> dictIdList, Map<String, List<SDict>> dictMap){
		if(dictIdList != null && dictIdList.size() > 0){
			Iterator<String> iter = dictIdList.iterator();
			while(iter.hasNext()){
				String uuid = iter.next();
				SDict dict = QueryDict.get(SDict.class, uuid);
				String parentId = dict.getParentId();
				List dictList = dictMap.get(parentId);
				if(dictList == null || dictList.size() <= 0){
					dictList = new ArrayList<SDict>();
				}
				dictList.add(dict);
				dictMap.put(parentId, dictList);
				List children = DictMan.getDictType(uuid);
				if(children != null || children.size() > 0){
					List<String> childIdList = new ArrayList<String>();
					Iterator<SDict> dictIter = children.iterator();
					while(dictIter.hasNext()){
						SDict child = dictIter.next();
						childIdList.add(child.getUuid());
					}
					if(childIdList != null && childIdList.size() > 0)
						initDictMap(childIdList, dictMap);
				}
			}
		}
	}
	
	/**
	 * 根据表名、字典编码，通过md5加密生成字典表的uuid
	 * @author liuzhb on Jan 13, 2014 3:39:54 PM
	 * @param table
	 * @param dictId
	 * @return
	 */
	public static String getUuid(String table, String dictId){
		return Md5Util.MD5Encode(table + dictId);
	}
	public static void main(String[] args) {
		try {
			SDict dt = null;
			List l = new ArrayList();
			//添加字典表
		//	for(int i = 1; i < 3; i++){
		//				SDict dict = new SDict();
		//				dict.setParentId("0");
		//				dict.setCode(2 + "");
		//				dict.setTableName("d_dealFlag");
		//				dict.setUuid(Md5Util.MD5Encode(dict.getTableName() + dict.getCode()));
		//				dict.setName( "未删除");
		//				dict.setOrderNum(2);
		//				dict.setRemark("已删除");
		//				addDict(dict);
		//	}

//			dt = DictMan.getDictType("d_funcType", "1");
//			System.out.println(dt.getName());
//			dt = DictMan.getDictType("d_sex", "2");
//			System.out.println(dt.getName());
			//删除单个字典项
//			DictMan.delDict("d_sex", "3");
//			dt = DictMan.getDictType("d_sex", "2");
//			System.out.println(dt.getName());
			//删除多个字典项
//			l = new ArrayList<String>();
//			l.add("4");
//			l.add("5");
//			DictMan.delDict("d_sex", l);
			
//			
//			List<SDict> dictList = DictMan.getDictType("d_openFlag");
//			for(int i = 0; i < dictList.size(); i++){
//				dt = DictMan.getDictType("d_openFlag", dictList.get(i).getCode());
//				System.out.println(dt.getName());
//			}
//			dt = DictMan.getDictType("d_sex", "3");
//			System.out.println(dt.getName());
			
			//获取字典表uuid
			dt = new SDict();
			System.out.println(dt.getClass().getName());
			
//			System.out.println(Md5Util.MD5Encode("d_root" + "d_logtype"));
//			 
//			String uuid = DictMan.getDictType("0", "d_root").getUuid();
//			System.out.println(DictMan.getDictType("d_logtype").size());
//			System.out.println(uuid);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
