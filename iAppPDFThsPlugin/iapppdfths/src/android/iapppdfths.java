package cn.com.ths.iapppdf;
import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;

import cn.com.ths.iapppdfths.BookShower;

import static cn.com.ths.iapppdfths.ConstantValue.LIC;
import static cn.com.ths.iapppdfths.ConstantValue.NAME;

/**
 * iappPDF插件，用于打开padf文件，并进行电子签章的操作
 */
public class iapppdfths extends CordovaPlugin {
  private static iapppdfths instance;
  private Activity activity;
  public iapppdfths() {
    instance = this;
  }
  @Override
  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);
    activity=cordova.getActivity();
    IntentFilter intentFilter=new IntentFilter();
    intentFilter.addAction(BookShower.PDF_SAVE);
    intentFilter.addAction(BookShower.PDF_CLOSE);
    cordova.getActivity().registerReceiver(new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        JSONObject jb=new JSONObject();
        try {
          jb.put("res",intent.getAction());
          //jb.put("res","SUCCESS");
        } catch (JSONException e) {
          e.printStackTrace();
        }
        String format = "cordova.plugins.iapppdfths.closePdfInAndroidCallback(%s);";
        final String js = String.format(format, jb.toString());
        activity.runOnUiThread(new Runnable() {
          @Override
          public void run() {
            instance.webView.loadUrl("javascript:" + js);
          }
        });
      }
    },intentFilter);

  }

    @Override
    public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        if (action.equals("openPdf")) {


          if (PermissionsUtil.hasPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)) {
              openPdf(args, callbackContext);
          } else {
            PermissionsUtil.requestPermission(activity, new PermissionListener() {
              @Override
              public void permissionGranted(@NonNull String[] permissions) {
                try {
                  openPdf(args, callbackContext);
                } catch (JSONException e) {
                  e.printStackTrace();
                }
              }

              @Override
              public void permissionDenied(@NonNull String[] permissions) {
                //用户拒绝了访问摄像头的申请
              }
            }, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE});
          }

            return true;
        }
        return false;
    }

  private void openPdf(JSONArray args, CallbackContext callbackContext) throws JSONException {
    String filepath = args.getString(0);
    String userName = args.getString(1);
    String copyRight = args.getString(2);
    this.openPdf(filepath,userName,copyRight, callbackContext);
  }

  private void openPdf(String filepath,String userName,String copyRight, CallbackContext callbackContext) {

      File file = new File(filepath);
      Uri uri = Uri.fromFile(file);//获取文件的uri
      //设置intent的action
      Intent intent = new Intent("android.intent.action.VIEW", uri);
      //设置跳转的activity
      intent.setClassName(cordova.getActivity(),
        "cn.com.ths.iapppdfths.BookShower");
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      //传递用户名，默认admin
      intent.putExtra(NAME, userName);
      //传递授权码(必传)
      intent.putExtra(LIC, copyRight);

      cordova.getActivity().startActivity(intent);
      callbackContext.success("success");
    }
}
