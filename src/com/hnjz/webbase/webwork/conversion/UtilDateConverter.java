package com.hnjz.webbase.webwork.conversion;

import com.hnjz.util.DateTimeUtil;
import com.hnjz.util.StringHelper;
import com.opensymphony.webwork.util.WebWorkTypeConverter;

import java.util.Date;
import java.util.Map;

public class UtilDateConverter extends WebWorkTypeConverter {
    public UtilDateConverter() {
    }

    public Object convertFromString(Map context, String[] values, Class toClass) {
        return this.doConvertToParameter(values[0]);
    }

    public String convertToString(Map context, Object o) {
        String result = null;
        if (o instanceof Date) {
            Date param = (Date)o;
            result = DateTimeUtil.getDateTimeString(param);
            if (StringHelper.isEmpty(result)) {
                result = DateTimeUtil.getDateString(param);
            }
        }

        return result;
    }

    public Object doConvertToParameter(String res) {
        if (StringHelper.isEmpty(res)) {
            return null;
        } else {
            Date date = DateTimeUtil.stringToUtilDate(res);
            if (date == null) {
                date = DateTimeUtil.stringToUtilDate(res, "yyyy-MM-dd");
            }

            if (date == null) {
                date = DateTimeUtil.stringToUtilDate(res);
            }

            return date;
        }
    }
}
