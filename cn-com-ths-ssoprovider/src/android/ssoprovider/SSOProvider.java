package cn.com.ths.ssoprovider;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * @author chixin
 * @version v1.0
 *
 */
public class SSOProvider extends CordovaPlugin {
	private static final String ACTION_SAVE="save";
	private static final String ACTION_GET="get";
	private static final String ACTION_GET_PM="app_get_pm";
	private static final String ACTION_CRYPTO="action_crypto";
	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.initialize(cordova, webView);
		SPHelper.init(this.cordova.getActivity().getApplication());
	}
     @Override
    public boolean execute(String action, JSONArray args,
    		CallbackContext callbackContext) throws JSONException {
    	if(ACTION_SAVE.equalsIgnoreCase(action)){
    	    setUserConfig(args,callbackContext);	
		    return true;		   
    	}
		if(ACTION_GET.equalsIgnoreCase(action)){
    		getUserConfigJson(args,callbackContext);
			return true;
    	}
		if(ACTION_GET_PM.equalsIgnoreCase(action)){
			String pm=this.cordova.getActivity().getPackageName();
			String enPm=new SimpleCrypto().encode(pm);
			callbackContext.success(enPm);
			return true;
		}
		if(ACTION_CRYPTO.equalsIgnoreCase(action)){
			String json=args.optString(0);
			String enJson=new SimpleCrypto().encode(json);
			callbackContext.success(enJson);
			return true;
		}
    	return false; 
    }
     /**
      * 获取sp数据
      * @param args
      * @param callbackContext
      */
     private void getUserConfigJson(JSONArray args,CallbackContext callbackContext){
    	 String jsonStr=SPHelper.getString(ConstantUtils.USER_INFO,null);
    	 callbackContext.success(jsonStr);
    	 
     }
     /**
      * 存储sp数据
      * @param args
      * @param callbackContext
      */
     private void setUserConfig(JSONArray args,CallbackContext callbackContext){
    	 String value=args.optString(0);
    	 int code=SPHelper.save(ConstantUtils.USER_INFO, value);
    	 callbackContext.success(code);
    	 
     }
  
}
