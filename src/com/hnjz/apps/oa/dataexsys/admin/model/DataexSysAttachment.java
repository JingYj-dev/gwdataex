package com.hnjz.apps.oa.dataexsys.admin.model;



/**
 * DataexAttachment entity. @author MyEclipse Persistence Tools
 */
public class DataexSysAttachment  implements java.io.Serializable {


    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Fields    

     private String uuid;
     private String serverId;
     private String path;
     private String fileName;


    // Constructors

    /** default constructor */
    public DataexSysAttachment() {
    }

	/** minimal constructor */
    public DataexSysAttachment(String uuid) {
        this.uuid = uuid;
    }
    
    /** full constructor */
    public DataexSysAttachment(String uuid, String serverId, String path) {
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
