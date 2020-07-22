package com.hnjz.base.web.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：JingYj
 * @date ：2020/7/2
 * @version: V1.0.0
 */
public class UserDetailsServiceImpl {

    /**日志*/
    private static final Log log = LogFactory.getLog(UserDetailsServiceImpl.class);
/*

    private Dao dao;

    */
/**
     * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
     *//*

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        String hsql = "from TSysUser where isActive = 'Y' and username = ?";
        List<TSysUser> re = dao.find(hsql, name);
        if (re.isEmpty()) {
            throw new UsernameNotFoundException("用户： " + name + "不存在");
        }
        //用户具有的角色
        TSysUser user = re.get(0);
        hsql = "select r.id from TSysUserRole ur,TSysRole r where  ur.role.id = r.id and ur.user.id = ? ";
        List<String> roles = this.dao.find(hsql, user.getId());
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (String ele : roles) {
            authorities.add(new SimpleGrantedAuthority(ele));
            //user.setRole(ele);
            if (log.isDebugEnabled()) {
                log.debug("用户具有的角色："+ele);
            }

            //System.out.println(ele);
        }
        user.setAuthorities(authorities);
        return user;
    }

    public void setDao(Dao dao) {
        this.dao = dao;
    }
*/

}
