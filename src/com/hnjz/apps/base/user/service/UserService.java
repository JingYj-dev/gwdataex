package com.hnjz.apps.base.user.service;

import com.hnjz.apps.base.org.model.SUserOrg;
import com.hnjz.apps.base.user.model.SUser;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.db.util.StringHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserService {
	
	public static SUser getUser(String userId) {
		if (userId == null)
			return null;
		return QueryCache.get(SUser.class, userId);
	}

	@SuppressWarnings("unchecked")
	public void saveOrUpdUser(SUser suser, String orgIds, TransactionCache tx) throws Exception {
		String userId = suser.getUuid();
		SUser user = getUser(userId);
		String[] orgIdArr = StringHelper.strToArr(orgIds);
		if(user == null && StringHelper.isNotEmpty(userId)){
			user = suser;
			tx.save(user);
		}else{
			user.setRealName(suser.getRealName());
			user.setOrgId(suser.getOrgId());
			user.setSex(suser.getSex());
			user.setOpenFlag(suser.getOpenFlag());
			user.setDelFlag(suser.getDelFlag());
			user.setUserType(suser.getUserType());
			user.setOrderNum(suser.getOrderNum());
			user.setPhone(suser.getPhone());
			user.setMobile(suser.getMobile());
			user.setEmail(suser.getEmail());
			user.setEditDate(new Date());
			List<String> lst = new QueryCache("select a.uuid from SUserOrg a where a.userId =:userId ").setParameter("userId", userId).list();
			List<SUserOrg> orglist = QueryCache.idToObj(SUserOrg.class, lst);
			if(orglist != null && orglist.size() > 0){
				tx.delete(orglist);
			}
			tx.update(user);
		}
		if(orgIdArr.length > 0){
			List<SUserOrg> orgs = new ArrayList<SUserOrg>();
			for(int i=0;i<orgIdArr.length;i++){
				SUserOrg org = new SUserOrg();
				org.setUserId(userId);
				org.setOrgId(orgIdArr[i]);
				orgs.add(org);
			}
			tx.save(orgs);
		}
	}
	
	public static SUser getUserByLoginName(String loginName){
		String sql = "from SUser where loginName=:loginName";
		QueryCache cache = new QueryCache(sql);
		cache.setParameter("loginName", loginName);
		return (SUser)cache.uniqueResultCache();
	}
}
