package com.hnjz.core.model;

import com.hnjz.core.model.DefaultServiceResultImpl;
import com.hnjz.core.model.IServiceResult;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 18:48
 */
public class ModeFactory {
    private static com.hnjz.core.model.ModeFactory mf = null;

    private ModeFactory() {
    }

    public static com.hnjz.core.model.ModeFactory getModeFactory() {
        if (mf == null) {
            mf = new com.hnjz.core.model.ModeFactory();
        }

        return mf;
    }

    public static com.hnjz.core.model.ModeFactory createModeFactory(String facory) {
        try {
            Class<?> cla = Class.forName(facory);
            mf = (com.hnjz.core.model.ModeFactory)cla.newInstance();
        } catch (Exception var2) {
            throw new RuntimeException(var2);
        }

        return mf;
    }

    public <T> com.hnjz.core.model.IServiceResult<T> buildNewServiceResult() {
        return new DefaultServiceResultImpl();
    }

    public com.hnjz.core.model.IServiceResult buildNewServiceResult(int resCode, String resDesc) {
        DefaultServiceResultImpl res = new DefaultServiceResultImpl();
        res.setResultCode(resCode);
        res.setResultDesc(resDesc);
        return res;
    }

    public <T> IServiceResult<T> buildNewServiceResult(int resCode, String desc, T obj) {
        DefaultServiceResultImpl<T> res = new DefaultServiceResultImpl();
        res.setResultCode(resCode);
        res.setResultObject(obj);
        res.setResultDesc(desc);
        return res;
    }
}
