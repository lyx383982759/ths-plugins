package cn.com.ths.sso;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * 
 * @author chixin
 * @version V1.0
 *
 */
public class THSSsoHandlerPlugin extends CordovaPlugin {
	THSSsoHandler mThsSsoHandler=null;
	private static final String ACTION_THS_SSO="useThssso";
	private static final String ACTION_DECRYPTO="action_decrypto";
	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.initialize(cordova, webView);
		mThsSsoHandler=new THSSsoHandler(cordova.getActivity());
	}
	@Override
	public boolean execute(String action, JSONArray args,final CallbackContext callbackContext) throws JSONException {
		if(ACTION_THS_SSO.equalsIgnoreCase(action)){
			if(mThsSsoHandler==null){
				mThsSsoHandler=new THSSsoHandler(this.cordova.getActivity());
			}
			mThsSsoHandler.anthorize(new THSAuthListener() {
				@Override
				public void onSuccess(UserInfo userInfo) {
					callbackContext.success(userInfo.toString());
				}
				
				@Override
				public void onFailure(int errCode, String errMsg) {
					callbackContext.error("{\"errCode\":"+errCode+",\"errMsg\":"+errMsg+"}");
				}
				
				@Override
				public void onCancel() {
					
				}
			});
			return true;
		}
		if(ACTION_DECRYPTO.equalsIgnoreCase(action)){
			String message=mThsSsoHandler.getExtraInfo();
			callbackContext.success(message);
			return true;
		}
		return false;
	}
}
