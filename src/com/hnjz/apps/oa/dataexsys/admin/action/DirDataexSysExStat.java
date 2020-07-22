package com.hnjz.apps.oa.dataexsys.admin.action;

import com.hnjz.apps.base.dict.model.SDict;
import com.hnjz.apps.base.dict.service.DictMan;
import com.hnjz.apps.oa.dataexsys.admin.service.DataexSysDirItem;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Css Team
 * All rights reserved.
 * <p>
 * This file DirDataexInbox.java creation date: [Jun 30, 2014 10:00:27 PM] by mazhh
 * http://www.css.com.cn
 */

public class DirDataexSysExStat extends AdminAction {

    private static final long serialVersionUID = 1L;

    private static Log log = LogFactory.getLog(DirDataexSysExStat.class);

    private String dirName; //��������
    private List<Object[]> objList;

    public DirDataexSysExStat() {
    }

    @SuppressWarnings("unchecked")
    @Override
    protected String adminGo() {
        trimStr();//���˿ո�
        String searchDirs = "";
        try {
            List<SDict> dicts = DictMan.getDictType("fw_d_security_level");
            int len = dicts.size();
            StringBuffer fwSql = new StringBuffer("   select t.send_org sendOrg, ");
            fwSql.append("          sum(case when doc_security='��' or doc_security='' or doc_security is null then 1 else 0 end) as securityF0, ");
            for (int i = 0; i < len; i++) {
                fwSql.append("      sum(case when doc_security='" + dicts.get(i).getName() + "' then 1 else 0 end) as securityF" + (i + 1));
                if (i + 1 < len) {
                    fwSql.append(", ");
                }
            }
            fwSql.append("     from dataex_sys_trans_content t ");


            StringBuilder swSql = new StringBuilder("   select b.target_org targetOrg, ");
            swSql.append("          sum(case when doc_security='��' or a.doc_security='' or doc_security is null then 1 else 0 end) as securityS0, ");
            for (int i = 0; i < len; i++) {
                swSql.append("      sum(case when doc_security='" + dicts.get(i).getName() + "' then 1 else 0 end) as securityS" + (i + 1));
                if (i + 1 < len) {
                    swSql.append(", ");
                }
            }
            swSql.append("     from dataex_sys_trans_content a, dataex_sys_transtask b ");
            swSql.append("    where a.uuid = b.content_id "); //"and b.send_status='1'" �˴���ȷ�����ͳɹ�״ֵ̬����ʱ��д

            if (StringHelper.isNotEmpty(dirName)) {
                searchDirs = DataexSysDirItem.getDirOrgsByName(dirName);
                fwSql.append(" where t.send_org in (" + searchDirs + ") ");
                swSql.append(" and b.target_org in (" + searchDirs + ") ");
            }

            fwSql.append(" group by t.send_org ");
            swSql.append(" group by b.target_org ");

            QueryCache fwQc = new QueryCache(fwSql.toString(), true);
            List<Object> fwList = fwQc.list();

            QueryCache swQc = new QueryCache(swSql.toString(), true);
            List<Object> swList = swQc.list();

            List<String> orgList = null;
            List<String> searchDirList = null;
            if (StringHelper.isNotEmpty(searchDirs)) {
                searchDirList = StringHelper.strToList(searchDirs.replaceAll("'", ""));
                orgList = searchDirList;
            } else {
                orgList = DataexSysDirItem.getDirOrgs();
            }
            if (orgList == null) {
                orgList = new ArrayList<String>();
            }
            objList = new ArrayList<Object[]>();
            Object[] arr = null;
            Object[] objArr = null;
            for (String org : orgList) {
                arr = new Object[]{"", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                if (StringHelper.isEmpty(org)) {
                    continue;
                }
                arr[0] = org;
                for (int i = 0; i < fwList.size(); i++) {
                    objArr = (Object[]) fwList.get(i);
                    if (objArr != null && org.equals((String) objArr[0])) {
                        arr[1] = objArr[1];
                        arr[2] = objArr[2];
                        arr[3] = objArr[3];
                        arr[4] = objArr[4];
                        arr[5] = ((BigInteger) objArr[1]).add((BigInteger) objArr[2]).add((BigInteger) objArr[3]).add((BigInteger) objArr[4]);
                    }
                }
                for (int i = 0; i < swList.size(); i++) {
                    objArr = (Object[]) swList.get(i);
                    if (objArr != null && org.equals((String) objArr[0])) {
                        arr[6] = objArr[1];
                        arr[7] = objArr[2];
                        arr[8] = objArr[3];
                        arr[9] = objArr[4];
                        arr[10] = ((BigInteger) objArr[1]).add((BigInteger) objArr[2]).add((BigInteger) objArr[3]).add((BigInteger) objArr[4]);
                    }
                }
                objList.add(arr);
            }
            return Action.SUCCESS;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            setMessage(Messages.getString("systemMsg.exception"));
            return Action.ERROR;
        }
    }

    private void trimStr() {
        dirName = StringHelper.isNotEmpty(dirName) ? dirName : "";
    }

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public List<Object[]> getObjList() {
        return objList;
    }

    public void setObjList(List<Object[]> objList) {
        this.objList = objList;
    }
}
