package com.hnjz.apps.oa.dataexsys.service.expansion;

import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysTransContent;
import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysTransTask;
import com.hnjz.apps.oa.dataexsys.pkg.model.Expansion;
import com.hnjz.apps.oa.dataexsys.pkg.model.OtherExpansion;
import com.hnjz.db.util.StringHelper;

import java.util.Map;


public class OtherExpansionProcessor implements IExpansionProcessor {
	
	
	public void unpkg(DataexSysTransContent transContent,
			DataexSysTransTask task, Expansion expansion, String[] args) {
		if(transContent==null || task==null || expansion==null){
			return;
		}
		OtherExpansion exp = (OtherExpansion)expansion;
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
		String other = exp.getOther();
		if(StringHelper.isNotEmpty(other)){
			transContent.setExpansion(other);
		}
	}

	@Override
	public Expansion pkg(DataexSysTransContent transContent,
			DataexSysTransTask task, Expansion expansion) {
		OtherExpansion exp = null;
		if(expansion!=null && (expansion instanceof OtherExpansion) ){
			exp = (OtherExpansion)expansion;
		}
		if(exp==null){
			exp = new OtherExpansion();
		}
		exp = (OtherExpansion) new DefaultExpansionProcessor().pkg(transContent, task, exp);
		if(transContent!=null){
			exp.setOther(transContent.getExpansion());
		}
		return exp;
	}
	
}
