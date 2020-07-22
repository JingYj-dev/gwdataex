package com.hnjz.apps.base.user.model;



/**
 * 
 * 项目名称：gjshare    
 * 类名称：UserInfo    
 * 类描述：用户信息
 * 创建时间：2013-11-5 下午4:41:32       
 * @version   1.0
 * @author jiazhihui
 *
 */
public class UserInfo  implements java.io.Serializable {

	private static final long serialVersionUID = 1619356467080341218L;
	private Integer userId;//自增主键，与用户中心关联主键
    private String loginName;//登录名
    private String password;//密码
    private String realName;//真实姓名
    private Integer userLevel;//用户等级,1:超级管理员；2：资源库管理员；3：主讲教师；4：助教；
    private Integer status;//状态
    private String provinceId;//省份编号
    private Integer schoolId;//学校id
    private Integer sexId;//性别
    private String email;//邮箱
    private String mobile;//手机号
    private String phone;//电话
    private String remark;//教师介绍

    
    // Constructors

    /** default constructor */
    public UserInfo() {
    }

    
    /** full constructor */
    public UserInfo(String loginName, String password, String realName, Integer userLevel, Integer status, String provinceId, Integer schoolId, Integer sexId, String email, String mobile, String phone, String remark) {
        this.loginName = loginName;
        this.password = password;
        this.realName = realName;
        this.userLevel = userLevel;
        this.status = status;
        this.provinceId = provinceId;
        this.schoolId = schoolId;
        this.sexId = sexId;
        this.email = email;
        this.mobile = mobile;
        this.phone = phone;
        this.remark = remark;
    }

   
    // Property accessors

    public Integer getUserId() {
        return this.userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getLoginName() {
        return this.loginName;
    }
    
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return this.realName;
    }
    
    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getUserLevel() {
        return this.userLevel;
    }
    
    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    public Integer getStatus() {
        return this.status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getProvinceId() {
        return this.provinceId;
    }
    
    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public Integer getSchoolId() {
        return this.schoolId;
    }
    
    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public Integer getSexId() {
        return this.sexId;
    }
    
    public void setSexId(Integer sexId) {
        this.sexId = sexId;
    }

    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return this.mobile;
    }
    
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return this.phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRemark() {
        return this.remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
   








}