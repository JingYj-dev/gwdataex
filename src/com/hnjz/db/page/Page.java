package com.hnjz.db.page;

import com.hnjz.db.config.ConfigurationManager;
import org.hibernate.Query;

import java.io.Serializable;
import java.util.List;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 16:35
 */
public class Page implements Serializable {
    public static final int OEDER_DESC = 0;
    public static final int OEDER_ASC = 1;
    public static final String MAPPING_FILE = "/hibernate.cfg.xml";
    private int pageSize = ConfigurationManager.getPageSize();
    private int currentPage = 1;
    private int totalRows;
    private int totalPages;
    private List results;
    private int orderFlag;
    private String countField;
    private String orderString;
    private static int PAGESIZE = 10;

    public Page() {
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.pageSize);
        sb.append(",");
        sb.append(this.currentPage);
        sb.append(",");
        sb.append(this.orderFlag);
        sb.append(",");
        sb.append(this.orderString);
        sb.append(",");
        return sb.toString();
    }

    public void finalize() throws Throwable {
        this.gc();
        super.finalize();
    }

    public void gc() {
        if (this.results != null) {
            this.results.clear();
            this.results = null;
        }

        this.orderString = null;
    }

    public void initPage(int totalRows, int pageSize) {
        this.totalRows = totalRows;
        this.pageSize = pageSize;
        this.initPageInfo();
    }

    public void initPage(Query query, Query queryRows) {
        if (this.pageSize == 0) {
            this.pageSize = PAGESIZE;
        }

        this.getQueryRows(queryRows);
        this.results = this.getQueryResult(query);
    }

    private List getQueryResult(Query query) {
        List listResult;
        if (this.pageSize == -1) {
            listResult = query.list();
            this.totalRows = listResult.size();
            this.totalPages = 1;
            this.currentPage = 1;
        } else {
            if (this.currentPage < 1) {
                this.currentPage = 1;
            }

            listResult = query.setFirstResult((this.currentPage - 1) * this.pageSize).setMaxResults(this.pageSize).list();
        }

        return listResult;
    }

    private void getQueryRows(Query query) {
        try {
            this.totalRows = Integer.parseInt(query.list().get(0).toString());
        } catch (Exception var3) {
            var3.printStackTrace();
            this.totalRows = 0;
        }

        this.initPageInfo();
    }

    public void initPageInfo() {
        this.totalPages = (this.totalRows + this.pageSize - 1) / this.pageSize;
        this.totalPages = this.totalPages < 1 ? 1 : this.totalPages;
        this.currentPage = this.currentPage > this.totalPages ? this.totalPages : this.currentPage;
    }

    public boolean isNextPage() {
        return this.currentPage < this.totalPages;
    }

    public boolean isPreviousPage() {
        return this.currentPage > 1;
    }

    public String getCountField() {
        return this.countField;
    }

    public void setCountField(String countField) {
        this.countField = countField;
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List getResults() {
        return this.results;
    }

    public void setResults(List results) {
        this.results = results;
    }

    public int getTotalRows() {
        return this.totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getTotalPages() {
        return this.totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getOrderFlag() {
        return this.orderFlag;
    }

    public void setOrderFlag(int orderFlag) {
        this.orderFlag = orderFlag;
    }

    public String getOrderString() {
        return this.orderString;
    }

    public void setOrderString(String orderString) {
        this.orderString = orderString;
    }

    public String getOrderByString() {
        String sRet = "";
        if (this.orderString != null && !this.orderString.equals("")) {
            sRet = " order by " + this.orderString + (this.orderFlag == 1 ? " " : " desc ");
        }

        return sRet;
    }

    public String getOrderByString2() {
        String sRet = "";
        if (this.orderString != null && !this.orderString.equals("")) {
            sRet = ", " + this.orderString + (this.orderFlag == 1 ? " " : " desc ");
        }

        return sRet;
    }

    public String getPageSplit() {
        StringBuffer sb = new StringBuffer();
        if (this.pageSize != -1 && this.totalPages != 1) {
            sb.append("<li><a href=\"javascript:;\" class=\"page-first\">首页</a></li>　");
            if (this.isPreviousPage()) {
                sb.append("<li><a href=\"javascript:;\" class=\"page-prev\">上一页</a></li>　");
            }

            if (this.isNextPage()) {
                sb.append("<li><a href=\"javascript:;\" class=\"page-next\">下一页</a></li>　");
            }

            sb.append("<li><a href=\"javascript:;\" class=\"page-last\">尾页</a></li>　");
            sb.append("<li>第<input class=\"page-jump\" size=\"3\" maxlength=\"6\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\"");
            sb.append(" onbeforepaste=\"clipboardData.setData('text',clipboardData.getData('text').replace(/[^\\d]/g,''))\"");
            sb.append(" value=\"" + this.currentPage + "\"/>");
            sb.append("页 <a href=\"javascript:;\" class=\"page-go\">Go</a> 共" + this.totalPages + "页 每页");
            sb.append("<input class=\"page-num\" size=\"3\" maxlength=\"6\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\"");
            sb.append(" onbeforepaste=\"clipboardData.setData('text',clipboardData.getData('text').replace(/[^\\d]/g,''))\"");
            sb.append(" name=\"page.pageSize\" id=\"page.pageSize\" value=\"" + this.pageSize + "\"/>");
            sb.append("条 记录总数：<b>" + this.totalRows + "</b>条</li>");
        } else {
            sb.append("<li>共1页 记录总数： <b>" + this.totalRows + " </b>条</li>");
        }

        return "<ul>" + sb.toString() + "</ul>";
    }

    public static boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }
}
