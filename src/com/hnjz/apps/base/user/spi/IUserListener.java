package com.hnjz.apps.base.user.spi;
import com.hnjz.apps.base.user.model.SUser;
import com.hnjz.common.exception.base.CssBaseException;

public interface IUserListener {
	/**
	 * 监听用户登录，身份认证通过后执行，用于加载用户信息
	 * @param user
	 * @throws CssBaseException
	 */
	void afterLogin(SUser user)throws CssBaseException;
	/**
	 * 监听用户注销，用于释放资源，在用户注销之前执行
	 * @param user
	 * @throws CssBaseException
	 */
	void beforeLogout(SUser user)throws CssBaseException;
}