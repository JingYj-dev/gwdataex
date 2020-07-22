package com.hnjz.apps.base.resource;

import com.hnjz.apps.base.org.model.SOrg;
import com.hnjz.apps.base.org.service.OrganizationService;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.apps.base.user.model.SUser;
import com.hnjz.apps.base.user.service.UserService;
import com.hnjz.db.query.TransactionNoCache;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ResourceServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pushDataType = request.getParameter("dataType");
		String result = "failure";
		TransactionCache tx = new TransactionCache();
		try{
			if("removeUser".equals(pushDataType)){
				String userId = request.getParameter("userId");
				SUser user = QueryCache.get(SUser.class, userId);
				if(user != null){
					user.setDelFlag("1");
				}
				tx.update(user);
			}else if("saveOrUpdUser".equals(pushDataType)){
				String orgIds = request.getParameter("orgIds");
				BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
				String line = null;
				StringBuilder sb = new StringBuilder();
				while((line = br.readLine())!=null){
					sb.append(line);
				}
				JSONObject json = JSONObject.fromObject(sb.toString());
				SUser user = (SUser)JSONObject.toBean(json,SUser.class);
				if(user != null){
					UserService userService = new UserService();
					userService.saveOrUpdUser(user,orgIds,tx);
				}
			}else if("removeOrg".equals(pushDataType)){
				String orgIds = request.getParameter("orgIds");
				OrganizationService organizationService = new OrganizationService();
				organizationService.removeOrganization(orgIds,tx);
			}else if("saveOrUpdOrg".equals(pushDataType)){
				BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
				String line = null;
				StringBuilder sb = new StringBuilder();
				while((line = br.readLine())!=null){
					sb.append(line);
				}
				JSONObject json = JSONObject.fromObject(sb.toString());
				SOrg org = (SOrg)JSONObject.toBean(json,SOrg.class);
				if(org != null){
					OrganizationService organizationService = new OrganizationService();
					organizationService.saveOrganization(org, tx);
				}
			}else if("zzzwRemoveOrg".equals(pushDataType)){
				String orgIds = request.getParameter("orgIds");
				OrganizationService organizationService = new OrganizationService();
				organizationService.removeOrganization(orgIds,tx);
			}else if("zzzwSaveOrUpdOrg".equals(pushDataType)){
				BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
				String line = null;
				final StringBuilder sb = new StringBuilder();
				while((line = br.readLine())!=null){
					sb.append(line);
				}
				new Thread(new Runnable(){
					public void run(){
						JSONArray array = JSONArray.fromObject(sb.toString());
						long time1 = System.currentTimeMillis();
						//重写事务提交类，原有事务类再save同时会写入缓存，如果数据量过大会导致OOM
						TransactionNoCache orgTx = new TransactionNoCache();
						try{
							if(array.size()>0){
								OrganizationService organizationService = new OrganizationService();
								organizationService.saveZzzwOrganizationNew(array, orgTx);
								orgTx.commit();
							}
							long time2 = System.currentTimeMillis();
							System.out.println("同步机构耗时================"+(time2-time1)+"ms");
						}catch(Exception e){
							if(orgTx != null){
								orgTx.rollback();
							}
							e.printStackTrace();
						}
					}
				}).start();
				
				/*List<Map<String,String>> list = array;
				
				//查询t_org表中同步前所有的部门和机构
				QueryCache qc = new QueryCache("select a.uuid,a.code from SOrg a");
				List<Object[]> oldOrg = qc.list();
				List<Map<String,String>> oldOrgList = new ArrayList<Map<String,String>>();
				for (Object[] obj : oldOrg) {
					Map<String,String> map = new HashMap<String,String>();
					map.put("uuid", obj[0].toString());
					map.put("code", obj[1].toString());
					oldOrgList.add(map);
				}
				
				//查询sys_dir表中同步前所有的部门和机构
				QueryCache qc1 = new QueryCache("select a.uuid,a.dirOrg from DataexSysDir a");
				List<Object[]> oldSys = qc1.list();
				List<Map<String,String>> oldDirList = new ArrayList<Map<String,String>>();
				for (Object[] obj : oldSys) {
					Map<String,String> map = new HashMap<String,String>();
					map.put("uuid", obj[0].toString());
					map.put("code", obj[1].toString());
					oldDirList.add(map);
				}
				
				int a = 1;
				System.out.println(list.size());
				for(Map<String,String> map : list){
					a = a+1;
					System.out.println(a);
					String orgId = map.get("id");
					String code = map.get("code");
					boolean flag = false;
					boolean flag1 = false;
					for(Map<String,String> oldMap : oldOrgList){
						if(oldMap.get("uuid").equals(orgId)&&oldMap.get("code").equals(code)){
							flag = true;
						}
					}
					for(Map<String,String> oldMap : oldDirList){
						if(oldMap.get("uuid").equals(orgId)&&oldMap.get("code").equals(code)){
							flag1 = true;
						}
					}
					if(!(flag&flag1)){
						OrganizationService organizationService = new OrganizationService();
						organizationService.saveZzzwOrganization(map, tx);
					}
				}*/
			}
			result = "success";
			if("zzzwRemoveOrg".equals(pushDataType)){
				tx.commit();
			}
		}catch(Exception e){
			if(tx != null){
				tx.rollback();
			}
			e.printStackTrace();
			result = e.getMessage();
		}finally{
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write(result);
		}
		
	}
}
