package com.hnjz.platform.sys.login;

import com.hnjz.apps.base.log.model.LogConstant;
import com.hnjz.core.configuration.Environment;
import com.hnjz.util.Ajax;
import com.hnjz.util.Messages;
import com.hnjz.util.StringHelper;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/7/3
 */
@Controller("/gwjh")
public class LoginController {

    private static final Log log = LogFactory.getLog(LoginController.class);
    @Autowired
    private LoginManager loginManager;

    /*,produces = "application/json"*/
    @RequestMapping(value = "/login.json", method = RequestMethod.GET)
    public String login(HttpServletRequest request, HttpServletResponse response, @RequestBody List<Map<String, String>> paramList) {
        Map<String, String> paramMap = paramList.get(0);
        String userName = paramMap.get("userName");
        String pwd = paramMap.get("pwd");
        if (StringHelper.isEmpty(userName) || StringHelper.isEmpty(pwd)) {
            String message="用户名或密码不能为空";
            setMessage(Messages.getString("systemMsg.fieldEmpty"));
            result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));
            logpart(LogConstant.LOG_LEVEL_IMPORTANT, LogConstant.RESULT_ERROR, Messages.getString("systemMsg.fieldEmpty"), getLoginData(), "", "");
            return Action.ERROR;
        }


        return "";
    }

}
