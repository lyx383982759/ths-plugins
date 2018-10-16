package cn.com.ths.libaar;

import android.content.Intent;
import com.thyc.ModuleHelper;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Administrator on 2017/2/15.
 */

public class ThsAarLauncherUtil extends CordovaPlugin {

  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

    if("startActivity".equals(action)){
      String clsString=args.optString(0);
      Intent intent=new Intent();
      String userJson= args.optString(1);
      if(userJson!=null)
      intent.putExtra("UserInfo", userJson);
      intent.setClassName(cordova.getActivity(),clsString);
      cordova.getActivity().startActivity(intent);
      return true;
    }
    if("initAARMoudules".equalsIgnoreCase(action)){
      ModuleHelper moduleHelper=new ModuleHelper(this.cordova.getActivity());
      String userId=args.optString(0);
      //String homeServer=args.optString(1);
	  //String mqBrokerHome=args.optString(2);
      moduleHelper.setUserId(userId);
     // moduleHelper.setMqBrokerHome(mqBrokerHome);
     // moduleHelper.setServerHome(homeServer);
      return true;
    }
    return false;
  }
}
