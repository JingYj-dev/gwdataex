package com.hnjz.webbase;

import com.hnjz.core.model.DefaultActionLog;
import com.hnjz.core.model.DefaultDictItem;
import com.hnjz.core.model.DefaultLog;
import com.hnjz.core.model.IDictable;
import com.hnjz.util.StringHelper;
import com.opensymphony.xwork.config.Configuration;
import com.opensymphony.xwork.config.ConfigurationManager;
import com.opensymphony.xwork.config.entities.ActionConfig;
import com.opensymphony.xwork.config.entities.PackageConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

public class FunctionManager {
    private static com.hnjz.webbase.FunctionManager instance = null;
    private Map<String, DefaultActionLog> functions = new HashMap();
    private Map<String, DefaultActionLog> funcMap = new HashMap();
    private Map<String, Map<String, List<DefaultActionLog>>> sysFuncMap = new HashMap();
    private Map<String, String> systeMap = new HashMap();
    private static final Log _log = LogFactory.getLog(com.hnjz.webbase.FunctionManager.class);

    private FunctionManager() {
    }

    public static com.hnjz.webbase.FunctionManager getFunctionManager() {
        if (instance == null) {
            instance = new com.hnjz.webbase.FunctionManager();
            instance.init();
        }

        return instance;
    }

    private void init() {
        Configuration cm = ConfigurationManager.getConfiguration();
        this.systeMap.clear();

        try {
            Set<String> pkgNames = cm.getPackageConfigNames();
            Iterator var4 = pkgNames.iterator();

            while(var4.hasNext()) {
                String packName = (String)var4.next();
                PackageConfig pc = cm.getPackageConfig(packName);
                if (pc == null) {
                    return;
                }

                this.systeMap.put(packName, cm.getPackageConfig(packName).getCaption());
                String nms = pc.getNamespace();
                Map<String, ActionConfig> cfgs = pc.getAllActionConfigs();
                Iterator var9 = cfgs.entrySet().iterator();

                while(var9.hasNext()) {
                    Map.Entry<String, ActionConfig> entry = (Map.Entry)var9.next();
                    String name = (String)entry.getKey();
                    ActionConfig ac = (ActionConfig)entry.getValue();
                    DefaultActionLog ifunc = this.toFunc(packName, name, pc.getSystemId(), ac, nms);
                    if (ifunc != null) {
                        ifunc.setPackageName(packName);
                        this.registerFuncLog(ifunc);
                    }
                }
            }
        } catch (Exception var13) {
            var13.printStackTrace();
            _log.error("加载功能点配置[funcs.xml]失败", var13);
        }

    }

    private DefaultActionLog toFunc(String packName, String name, String systemId, ActionConfig ac, String nms) {
        DefaultActionLog config = new DefaultActionLog();
        Map<String, String> attrs = ac.getExtendParams();
        String eventType = (String)attrs.get("event-type");
        if (eventType == null) {
            eventType = "0";
        }

        config.setEventType(eventType);
        String desc = (String)attrs.get("desc");
        if (desc == null) {
            desc = ac.getCaption();
        }

        config.setActionDesc(desc);
        config.setSystemId(systemId);
        String opType = (String)attrs.get("op-type");
        if (StringHelper.isNotEmpty(opType)) {
            config.setType(opType);
        }

        config.setActionName(name);
        config.setPackageName(packName);
        String actionId = this.makeActionKey(packName, name);
        config.setActionId(actionId);
        if (StringHelper.isEmptyByTrim(nms)) {
            config.setUrl(name + ".action");
        } else {
            config.setUrl(nms + "/" + name + ".action");
        }

        config.setStatus("1");
        return config;
    }

    private void registerFuncLog(DefaultActionLog flog) {
        String key = this.makeActionKey(flog.getPackageName(), flog.getActionName());
        this.functions.put(key, flog);
        this.funcMap.put(flog.getActionId(), flog);
        String sysId = flog.getSystemId();
        if (sysId != null) {
            Map<String, List<DefaultActionLog>> funcLogs = (Map)this.sysFuncMap.get(sysId.toString());
            if (funcLogs == null) {
                funcLogs = new HashMap();
                this.sysFuncMap.put(sysId.toString(), funcLogs);
            }

            String pack = flog.getPackageName();
            List<DefaultActionLog> logs = (List)((Map)funcLogs).get(pack);
            if (logs == null) {
                logs = new LinkedList();
                ((Map)funcLogs).put(pack, logs);
            }

            ((List)logs).add(flog);
        }

    }

    public List<Map<String, String>> getSysIds() {
        List<Map<String, String>> packs = new ArrayList();
        Iterator var3 = this.sysFuncMap.keySet().iterator();

        while(var3.hasNext()) {
            String sysId = (String)var3.next();
            Map<String, String> map = new HashMap();
            map.put("id", sysId);
            packs.add(map);
        }

        return packs;
    }

    public List<String> getPackageNames() {
        List<String> packs = new LinkedList();
        Iterator var3 = this.sysFuncMap.values().iterator();

        while(var3.hasNext()) {
            Map<String, List<DefaultActionLog>> map = (Map)var3.next();
            if (map != null) {
                packs.addAll(map.keySet());
            }
        }

        return packs;
    }

    public List<String> getPackageNames(String sysId) {
        List<String> packs = new LinkedList();
        Map<String, List<DefaultActionLog>> logs = (Map)this.sysFuncMap.get(sysId);
        if (logs != null) {
            packs.addAll(logs.keySet());
        }

        return packs;
    }

    public List<IDictable> getPackages(String sysId) {
        List<IDictable> packs = new LinkedList();
        List<String> names = this.getPackageNames(sysId);
        Iterator var5 = names.iterator();

        while(var5.hasNext()) {
            String name = (String)var5.next();
            String packName = (String)this.systeMap.get(name);
            if (packName != null) {
                DefaultDictItem item = new DefaultDictItem();
                item.setCode("Packages");
                item.setItemCode(name);
                item.setItemName(packName);
                packs.add(item);
            }
        }

        return packs;
    }

    public static List<IDictable> getPackageListBySystemId(String sysId) {
        return getFunctionManager().getPackages(sysId);
    }

    public void cleanUp() {
        this.funcMap.clear();
        this.functions.clear();
        this.sysFuncMap.clear();
        instance = null;
        Configuration cm = ConfigurationManager.getConfiguration();
        cm.destroy();
    }

    public Map<String, List<DefaultActionLog>> getActionMap(String sysId) {
        return (Map)this.sysFuncMap.get(sysId);
    }

    public DefaultActionLog getFuncAction(String actionCode) {
        return (DefaultActionLog)this.funcMap.get(actionCode);
    }

    public boolean transform(String packageName, String actionName, DefaultLog log) {
        if (StringHelper.isEmpty(packageName)) {
            return false;
        } else if (StringHelper.isEmpty(actionName)) {
            return false;
        } else {
            DefaultActionLog func = (DefaultActionLog)this.functions.get(this.makeActionKey(packageName, actionName));
            if (func == null) {
                return false;
            } else {
                if (StringHelper.isNotEmpty(func.getSystemId())) {
                    log.setSysid(func.getSystemId());
                }

                if (func.getType() != null) {
                    log.setOpType(Integer.valueOf(func.getType()));
                }

                log.setFuncId(func.getActionId());
                log.setLogData(func.getActionDesc());

                try {
                    if (StringHelper.isNotEmptyByTrim(func.getEventType())) {
                        log.setEventType(Integer.parseInt(func.getEventType()));
                    }
                } catch (NumberFormatException var6) {
                    var6.printStackTrace();
                }

                log.setPackageName(func.getPackageName());
                return true;
            }
        }
    }

    public DefaultActionLog getActionLog(String actionId) {
        return (DefaultActionLog)this.functions.get(actionId);
    }

    private String makeActionKey(String pkg, String act) {
        return StringHelper.isEmpty(pkg) ? act : pkg + "/" + act;
    }

    public String getActionCode(String actionId) {
        if (actionId != null && !actionId.trim().equals("")) {
            DefaultActionLog func = (DefaultActionLog)this.functions.get(actionId);
            return func == null ? "" : func.getActionName();
        } else {
            return "";
        }
    }

    public static String getFuncActionDesc(String actionId) {
        DefaultActionLog log = getFunctionManager().getFuncAction(actionId);
        return log == null ? "" : log.getActionDesc();
    }

    public static void main(String[] args) {
        com.hnjz.webbase.FunctionManager fm = getFunctionManager();
        fm.getFuncAction("acl_dict");
        fm.getPackageNames();
        fm.getPackageNames("1001");
        fm.getPackages("1001");
        getPackageListBySystemId("1001");
        getFuncActionDesc("acl_dict");
        fm.getActionCode("");
        List list = (List)fm.getActionMap("1001").get("acl_dict");
        System.out.println(list.size());
    }
}
