package com.hnjz.webbase.webwork.conversion;

import com.hnjz.util.DateTimeUtil;
import com.hnjz.util.StringHelper;
import com.opensymphony.webwork.util.WebWorkTypeConverter;

import java.sql.Date;
import java.util.Map;

public class SqlDateConverter extends WebWorkTypeConverter {
    public SqlDateConverter() {
    }

    public Object convertFromString(Map context, String[] values, Class toClass) {
        return this.doConvertToParameter(values[0]);
    }

    public String convertToString(Map context, Object o) {
        String result = null;
        if (o instanceof Date) {
            Date param = (Date)o;
            result = DateTimeUtil.getDateString(param);
        }

        return result;
    }

    public Object doConvertToParameter(String res) {
        if (StringHelper.isEmpty(res)) {
            return null;
        } else {
            java.util.Date date = DateTimeUtil.stringToUtilDate(res, "yyyy-MM-dd");
            if (date == null) {
                date = DateTimeUtil.stringToUtilDate(res);
            }

            return date != null ? new Date(date.getTime()) : null;
        }
    }
}
