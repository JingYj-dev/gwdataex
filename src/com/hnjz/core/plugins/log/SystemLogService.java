package com.hnjz.core.plugins.log;

import com.hnjz.core.model.ILog;
import com.hnjz.core.plugins.base.ILogProvider;
import com.hnjz.common.plugins.impl.AbstractPlugin;
import com.hnjz.util.Json;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 18:35
 */
public class SystemLogService extends AbstractPlugin implements ILogProvider {
    private static Log log = LogFactory.getLog(com.hnjz.core.plugins.log.SystemLogService.class);

    public SystemLogService() {
    }

    public void log(ILog slog) {
        if (slog != null) {
            log.info(Json.object2json(slog));
        }
    }
}
