package com.hnjz.apps.oa.dataexsys.admin.model;



/**
 * DataexAttachment entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("all")
public class DataexAttachment  implements java.io.Serializable {


    // Fields    

     private String uuid;
     private String serverId;
     private String path;
     private String fileName;


    // Constructors

    /** default constructor */
    public DataexAttachment() {
    }

	/** minimal constructor */
    public DataexAttachment(String uuid) {
        this.uuid = uuid;
    }
    
    /** full constructor */
    public DataexAttachment(String uuid, String serverId, String path) {
        this.uuid = uuid;
        this.serverId = serverId;
        this.path = path;
    }

   
    // Property accessors

    public String getUuid() {
        return this.uuid;
    }
    
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getServerId() {
        return this.serverId;
    }
    
    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getPath() {
        return this.path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
   








}
