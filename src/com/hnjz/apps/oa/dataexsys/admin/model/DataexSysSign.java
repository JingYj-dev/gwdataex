package com.hnjz.apps.oa.dataexsys.admin.model;

import java.util.Date;


/**
 * DataexAttachment entity. @author MyEclipse Persistence Tools
 */

public class DataexSysSign  implements java.io.Serializable {


    // Fields    

     /**
	 * 
	 */
	private static final long serialVersionUID = 7013565139935636225L;
	private String uuid;
     private String identityId;
     private String nodeIp;
     private String randomId;
     private Integer timeOut;
     private Date signTime;
     private String token;
     private Date lastDataexTime;

    // Constructors

    /** default constructor */
    public DataexSysSign() {
    }

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getIdentityId() {
		return identityId;
	}

	public void setIdentityId(String identityId) {
		this.identityId = identityId;
	}

	public String getNodeIp() {
		return nodeIp;
	}

	public void setNodeIp(String nodeIp) {
		this.nodeIp = nodeIp;
	}

	public String getRandomId() {
		return randomId;
	}

	public void setRandomId(String randomId) {
		this.randomId = randomId;
	}

	public Integer getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(Integer timeOut) {
		this.timeOut = timeOut;
	}

	public Date getSignTime() {
		return signTime;
	}

	public void setSignTime(Date signTime) {
		this.signTime = signTime;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getLastDataexTime() {
		return lastDataexTime;
	}

	public void setLastDataexTime(Date lastDataexTime) {
		this.lastDataexTime = lastDataexTime;
	}

	

}
