package com.hnjz.core.configuration;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 18:39
 */
public final class Environment {
    public static final int RESULT_CODE_SUCCESS = 0;
    public static final int RESULT_CODE_ERROR = 1;
    public static final String UN_DELETE_STATE = "2";
    public static final String DELETE_STATE = "1";
    public static final String CURRENT_SYSTEM_ID = "1001";
    public static final String System_SessionID = "SystemSessionID_Donne";
    public static final String Cookie_UserID = "cssbase_cookie";
    public static final String SESSION_LOGIN_KEY = "sUser";
    public static final String SYSTEM_NAME = "CSS_WORKREC";
    public static final String DEFAULT_PASSWORD = "888888";
    public static final Integer DEFAULT_FAILED_COUNT = 10;
    public static final String DEFAULT_SYS = "1001";
    public static final int SUPERADMIN = 1;
    public static final int NORMALUSER = 2;
    public static final String FORM_ENCRYPT_MARK = "form.encrypt";
    public static final String FORM_DES_CRYPTO_KEY = "form.crypto.des.key";
    public static final String FORM_DES_DEF_SECRET = "EDCioui1234";

    public Environment() {
    }
}
