package com.hnjz.core.model;

import java.io.Serializable;
import java.util.Date;

public interface ILog extends Serializable {
    String getLogId();

    String getSysid();

    String getFuncId();

    Integer getLogLevel();

    Integer getEventType();

    String getLogData();

    void setLogData(String var1);

    String getOpId();

    String getOperatorType();

    Integer getOpType();

    String getOpName();

    String getOpIp();

    Date getOpTime();

    long getDurationTime();

    String getResult();

    String getServerIp();

    String getServerName();
}
