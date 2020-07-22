package com.hnjz.apps.base.org.model;

import com.hnjz.apps.base.dict.service.DictMan;
import com.hnjz.core.model.IOrg;
import com.hnjz.db.query.JoinList;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.HtmlConverter;
import com.hnjz.apps.base.post.model.SPost;
import com.hnjz.apps.base.user.model.SUser;
import net.sf.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * SOrg entity. @author MyEclipse Persistence Tools
 */

public class SOrg implements IOrg,java.io.Serializable {

	// Fields
	private String className = SOrg.class.getName();
	private String uuid;
	private String name;
	private String parentId;
	private Integer orderNum;
	private String delFlag;
	private String openFlag;
	private String remark;
	private Date issueDate;
	private String issueId;
	private String issueName;
	private Date editDate;
	private String code;
	private String unitCode;
	
	private String realOrgId;
	
	/**
	 * 定义关联岗位、用户列表
	 */
	private transient JoinList postList = null;
	private transient JoinList userList = null;
	
	/**
	 * 定义关联发文类型、发文字
	 */
	/*private transient List<String> fwDocTypeList = null;
	private transient JoinList fwWordList = null;*/
	

	// Constructors

	/** default constructor */
	public SOrg() {
	}

	public String getLogInfo() {
		StringBuffer sb = new StringBuffer();
		sb.append(HtmlConverter.appendHtmlNameField("主键", this.getUuid()));
		sb.append(HtmlConverter.appendHtmlNameField("名称", this.getName()));
		sb.append(HtmlConverter.appendHtmlNameField("父节点", this.getParentId()));
		sb.append(HtmlConverter.appendHtmlNameField("排序号", this.getOrderNum()));
		sb.append(HtmlConverter.appendHtmlNameField("删除标记", DictMan.getDictType("d_dealflag", this.getDelFlag()).getName()));
		sb.append(HtmlConverter.appendHtmlNameField("开启标记", DictMan.getDictType("d_openflag", this.getOpenFlag()).getName()));
		sb.append(HtmlConverter.appendHtmlNameField("备注",this.getRemark()));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sb.append(HtmlConverter.appendHtmlNameField("创建日期", sdf.format(this.getIssueDate())));
		sb.append(HtmlConverter.appendHtmlNameField("创建人id", this.getIssueId()));
		sb.append(HtmlConverter.appendHtmlNameField("创建人", this.getIssueName()));
		sb.append(HtmlConverter.appendHtmlNameField("修改日期", sdf.format(this.editDate)));
		sb.append(HtmlConverter.appendHtmlNameField("编码", this.getCode()));
		return sb.toString();
	}
	/** minimal constructor */
	public SOrg(String uuid, String name) {
		this.uuid = uuid;
		this.name = name;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}



	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getOpenFlag() {
		return openFlag;
	}

	public void setOpenFlag(String openFlag) {
		this.openFlag = openFlag;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public String getIssueId() {
		return issueId;
	}

	public void setIssueId(String issueId) {
		this.issueId = issueId;
	}

	public String getIssueName() {
		return issueName;
	}

	public void setIssueName(String issueName) {
		this.issueName = issueName;
	}

	public Date getEditDate() {
		return editDate;
	}

	public void setEditDate(Date editDate) {
		this.editDate = editDate;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}



	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public JoinList getPostList() {
		if(postList == null){
			QueryCache qc = new QueryCache("select a.postId from SOrgPost a where a.orgId=:orgId")
			.setParameter("orgId", uuid);
			postList = new JoinList(SPost.class, qc);
		}
		return postList;
	}

	public JoinList getUserList() {
		if(userList == null){
			QueryCache qc = new QueryCache("select a.uuid from SUser a where a.orgId=:orgId")
			.setParameter("orgId", uuid);
			userList = new JoinList(SUser.class, qc);
		}
		return userList;
	}

/*	public List<String> getFwDocTypeList() {
		if(fwDocTypeList == null){
			fwWordList = getFwWordList();
			if(fwWordList != null && fwWordList.getList().size()>0){
				fwDocTypeList = new ArrayList<String>();
				List<FwWord> fwWords = fwWordList.getList();
				for(FwWord fwWord : fwWords){
					FwDocType fwDocType = FwDocTypeMan.get(fwWord.getFwType());
					if(!fwDocTypeList.contains(fwDocType.getUuid())){
						fwDocTypeList.add(fwDocType.getUuid());
					}
				}
			}
		}
		return fwDocTypeList;
	}
	
	
	
	public JoinList getFwWordList() {
		if(fwWordList == null){
			StringBuffer sb = new StringBuffer("select a.fwWordId from FwWordOrg a  where a.orgId = :deptId");
			QueryCache qc = new QueryCache(sb.toString()).setParameter("deptId", uuid);
			fwWordList = new JoinList(FwWord.class, qc);
			List<FwWord> fwWords = fwWordList.getList();
			for(FwWord fwWord : fwWords){
				if("2".equals(fwWord.getOpenFlag())){
					fwWordList.remove(fwWord.getUuid());
				}
			}
		}
		return fwWordList;
	}
	
	
	public JoinList getFwWordList2() {
		if(fwWordList == null){
			StringBuffer sb = new StringBuffer(" select uuid from FwWord a where openFlag = '1' ");
			sb.append(" and exists(select 1 from FwWordOrg forg  where forg.fwWordId=a.uuid and forg.orgId = :deptId ) ");
			QueryCache qc = new QueryCache(sb.toString()).setParameter("deptId", uuid);
			fwWordList = new JoinList(FwWord.class, qc);
		}
		return fwWordList;
	}
*/
	/**
	 * 转换为单个json字符串
	 * @return
	 */
	public JSONObject toJson() {
		return new JSONObject().element("uuid", uuid).element("name", name)
				.element("parentId", parentId).element("orderNum", orderNum)
				.element("delFlag", delFlag).element("openFlag", openFlag)
				.element("remark", remark).element("issueDate", issueDate)
				.element("issueId", issueId).element("issueName", issueName)
				.element("editDate", editDate).element("code", code);
	}

 
	public String getOrgCode() {
//		return this.uuid.substring(0, 8);
		return code;
	}

	public String getOrgId() {
		return this.uuid;
	}

	public String getOrgName() {
		return this.name;
	}

	public String getRealOrgId() {
		return realOrgId;
	}

	public void setRealOrgId(String realOrgId) {
		this.realOrgId = realOrgId;
	}

	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	
}