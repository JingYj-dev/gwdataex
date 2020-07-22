package com.hnjz.apps.base.common.action;
import com.hnjz.core.model.IServiceResult;
import com.hnjz.util.Ajax;
import com.hnjz.webbase.webwork.action.UserAction;

@SuppressWarnings("serial")
public class GetCurrentDesKey extends UserAction {
	static String chars="01234567890abcdefghijklmnopqrstuvwxyzABCDEFJHIGKLMNOPQRSTUVWXYZ";
	@Override
	protected String userGo() {
		StringBuffer key=new StringBuffer();		 
		 int size = (int)Math.floor(Math.random()*10)+8;
		 for(int i=0 ; i<size ;i++){
			 int num=(int)Math.round(chars.length()*Math.random());
			 if(num >= chars.length())num=chars.length()-1;
			 key.append(chars.charAt(num));			 
		 }
		 super.set(com.hnjz.core.configuration.Environment.FORM_DES_CRYPTO_KEY, key.toString());
		 setResult(Ajax.JSONResult(IServiceResult.RESULT_OK, "ok", key.toString()));
		 return SUCCESS;
	}
}
