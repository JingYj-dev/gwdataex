package com.hnjz.util;

import java.util.UUID;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 17:13
 */
public class UuidUtil {
    public UuidUtil() {
    }
    public static final String getUuid() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }

}
