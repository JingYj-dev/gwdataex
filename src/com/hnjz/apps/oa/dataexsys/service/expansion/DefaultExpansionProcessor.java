package com.hnjz.apps.oa.dataexsys.service.expansion;

import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysTransContent;
import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysTransTask;
import com.hnjz.apps.oa.dataexsys.pkg.model.DefaultExpansion;
import com.hnjz.apps.oa.dataexsys.pkg.model.Expansion;
import com.hnjz.db.util.StringHelper;

import java.util.HashMap;
import java.util.Map;

public class DefaultExpansionProcessor implements IExpansionProcessor {

	public void unpkg(DataexSysTransContent transContent,
			DataexSysTransTask task, Expansion expansion, String[] args) {
		if(transContent==null || task==null || expansion==null){
			return;
		}
		DefaultExpansion exp = (DefaultExpansion)expansion;
		String outboxId = exp.getOutboxId();
		if(StringHelper.isNotEmpty(outboxId)){
			transContent.setDocId(outboxId);
		}
		Map<String, Map<String, String>> nums = exp.getNums();
		String receiverFlag = null;
		if(args!=null && args.length>0){
			receiverFlag = args[0];
		}
		if(StringHelper.isNotEmpty(receiverFlag) && nums!=null){
			Map<String, String> num = (Map<String, String>)nums.get(receiverFlag);
			if(num!=null){
				String startNum = num.get("startNum");
				if(StringHelper.isNotEmpty(startNum)){
					task.setStartNum(startNum);
				}
				String endNum = num.get("endNum");
				if(StringHelper.isNotEmpty(endNum)){
					task.setEndNum(endNum);
				}
			}
		}
	}

	public Expansion pkg(DataexSysTransContent transContent,
			DataexSysTransTask task, Expansion expansion) {
		DefaultExpansion exp = null;
		if(expansion!=null && (expansion instanceof DefaultExpansion) ){
			exp = (DefaultExpansion)expansion;
		}
		if(exp==null){
			exp = new DefaultExpansion();
		}
		if(transContent!=null){
			exp.setOutboxId(transContent.getDocId());
		}
		if(task!=null){
			Map<String,Map<String,String>> nums = new HashMap<String,Map<String,String>>();
			if(StringHelper.isNotEmpty(task.getStartNum(),task.getEndNum())){
				Map<String,String> num = new HashMap<String,String>();
				num.put("receiveFlag", task.getTargetOrg());
				num.put("receiverName", task.getTargetOrgName());
				num.put("startNum", task.getStartNum());
				num.put("endNum", task.getEndNum());
				nums.put(task.getTargetOrg(), num);
			}
			exp.setNums(nums);
		}
		return exp;
	}

	
}
