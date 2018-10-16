package cn.com.ths.appcollect;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ThsInfoCollectPlugin extends CordovaPlugin {

  private AppCollectInfo appCollectInfo;
  public static final int REQUEST_PHONE_PERMISSION_CODE = 1000;
  public static final int REQUEST_LOCATION_PERMISSION_CODE = 1001;
  public static final int REQUEST_BD_LOCATION_PERMISSION_CODE = 1002;
  CallbackContext callbackContext;
  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    this.callbackContext=callbackContext;
    if("getInfo".equals(action)){
       initAppCollect();
       return true;
    }
    return false;
  }

  private void initAppCollect(){
    initCommData();
    initRequirePermissionData();
    getAppCollectInfoCallBack();
  }
  private void getAppCollectInfoCallBack(){
    PluginResult pluginResult =new PluginResult(PluginResult.Status.OK,appCollectInfo.toJSONObject());
    pluginResult.setKeepCallback(true);
    this.callbackContext.sendPluginResult(pluginResult);
  }
  private void initRequirePermissionData(){
    Context mContext =cordova.getActivity();
    if(checkAndReqPermission(new String[]{Manifest.permission.READ_PHONE_STATE},REQUEST_PHONE_PERMISSION_CODE)){
      getTelInfo(mContext,appCollectInfo);
    }else{
      return;
    }
    if(checkAndReqPermission(new String[]{
      Manifest.permission.ACCESS_FINE_LOCATION,
      Manifest.permission.ACCESS_COARSE_LOCATION,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.READ_EXTERNAL_STORAGE,
      Manifest.permission.READ_PHONE_STATE
    },REQUEST_BD_LOCATION_PERMISSION_CODE)){
      getLocationByBaidu(mContext);
    }else {
      return;
    }
  }
  private void initCommData(){
    appCollectInfo = new AppCollectInfo();
    Context mContext =cordova.getActivity();
    initAppInfo(mContext,appCollectInfo);
    // 注册广播监听网络变化
    listeningNetWork(mContext);
  }

  @Override
  public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) throws JSONException {
    super.onRequestPermissionResult(requestCode, permissions, grantResults);
    List<String> deniedList = new ArrayList<String>();
    for (int i = 0; i < grantResults.length; i++) {
      if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
        deniedList.add(permissions[i]);
      }
    }
    if(deniedList.size()>0){
      String[] perArr=new String[deniedList.size()];
      deniedList.toArray(perArr);
      cordova.requestPermissions(this,requestCode,perArr);
    }else{
      initRequirePermissionData();
      getAppCollectInfoCallBack();
    }
  }

  private BroadcastReceiver receiver=new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      if (appCollectInfo == null) {
        appCollectInfo = new AppCollectInfo();
      }
      getNetWorkInfo(context,appCollectInfo);
      getAppCollectInfoCallBack();
    }
  };
  /**
   * 注册网络广播
   * @param mContext
   */
  public void listeningNetWork(Context mContext) {
    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
    mContext.registerReceiver(receiver, intentFilter);
  }

  /**
   *  解除网络广播监听
   * @param mContext
   */
  public void removeListeningNetWork(Context mContext) {
    mContext.unregisterReceiver(receiver);
  }
  /**
   * 权限检查并申请
   * @param permissionArr
   * @param reqCode
   * @return
   */
  private boolean checkAndReqPermission(String[] permissionArr,int reqCode) {
    ArrayList<String> deniedPermissionList = new ArrayList<String>();
    int size=0;
    if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
      Context mContext = cordova.getActivity();
      for (String permission : permissionArr) {
        if (ContextCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {
          // 未授权 需要申请的权限
          deniedPermissionList.add(permission);
        }
      }
      size=deniedPermissionList.size();
      if(size>0){
        String[] perArr=new String[size];
        deniedPermissionList.toArray(perArr);
        cordova.requestPermissions(this,reqCode,perArr);
      }

    }
    return size==0;
  }
  /**
   * 权限检查
   * @param permissionArr
   * @return
   */
  private boolean checkPermission(String[] permissionArr) {
    ArrayList<String> deniedPermissionList = new ArrayList<String>();
    if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
      Context mContext = cordova.getActivity();
      for (String permission : permissionArr) {
        if (ContextCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {
          // 未授权 需要申请的权限
          deniedPermissionList.add(permission);
        }
      }
    }
    return deniedPermissionList.size()==0;
  }

  /**
   * 获取 电话信息
   * @param mContext
   * @param appCollectInfo
   */
  @SuppressLint("MissingPermission")
  private void getTelInfo(Context mContext, AppCollectInfo appCollectInfo) {
    //Manifest.permission.READ_PHONE_STATE
    TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
    String imsi = telephonyManager.getSubscriberId();
    String providersName = null;
    if (imsi != null) {
      if (imsi.startsWith("46000") || imsi.startsWith("46002") || imsi.startsWith("46007")) {
        providersName = "中国移动";
      } else if (imsi.startsWith("46001") || imsi.startsWith("46006")) {
        providersName = "中国联通";
      } else if (imsi.startsWith("46003")) {
        providersName = "中国电信";
      }
    }
    appCollectInfo.setDeviceId(telephonyManager.getDeviceId());
    appCollectInfo.setPhoneNumber(telephonyManager.getLine1Number());
    appCollectInfo.setProvidersName(providersName);
  }


  /**
   * 获取app信息
   * @param mContext
   */
  private void initAppInfo(Context mContext, AppCollectInfo appCollectInfo) {
    String packageName = mContext.getPackageName();
    appCollectInfo.setPackageName(packageName);
    PackageManager pm = mContext.getPackageManager();
    try {
      PackageInfo pi = pm.getPackageInfo(packageName, 0);
      appCollectInfo.setAppVer(pi.versionCode);
      appCollectInfo.setAppVerName(pi.versionName);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
  }

  /**
   *  获取网络信息
   * @param mContext
   */
  public void getNetWorkInfo(Context mContext, AppCollectInfo appCollectInfo) {
    String strNetworkType = "";
    NetworkInfo networkInfo = ((ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
    if (networkInfo != null && networkInfo.isConnected()) {
      String subTypeName = networkInfo.getSubtypeName();

      if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
        strNetworkType = "WIFI";
      } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
        String _strSubTypeName = networkInfo.getSubtypeName();
        // TD-SCDMA   networkType is 17
        int networkType = networkInfo.getSubtype();
        switch (networkType) {
          case TelephonyManager.NETWORK_TYPE_GPRS:
          case TelephonyManager.NETWORK_TYPE_EDGE:
          case TelephonyManager.NETWORK_TYPE_CDMA:
          case TelephonyManager.NETWORK_TYPE_1xRTT:
          case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
            strNetworkType = "2G";
            break;
          case TelephonyManager.NETWORK_TYPE_UMTS:
          case TelephonyManager.NETWORK_TYPE_EVDO_0:
          case TelephonyManager.NETWORK_TYPE_EVDO_A:
          case TelephonyManager.NETWORK_TYPE_HSDPA:
          case TelephonyManager.NETWORK_TYPE_HSUPA:
          case TelephonyManager.NETWORK_TYPE_HSPA:
          case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
          case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
          case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
            strNetworkType = "3G";
            break;
          case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
            strNetworkType = "4G";
            break;
          default:
            // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
            if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
              strNetworkType = "3G";
            } else {
              strNetworkType = _strSubTypeName;
            }
            break;
        }

      }
    }
    appCollectInfo.setNetworkType(strNetworkType);
  }
  // 通过百度 获取位置信息
  private BDLocationService myBDLocation;
  private void getLoactionInfo(BDLocation location, AppCollectInfo appCollectInfo) {
    if(location!=null){
      appCollectInfo.lat_bd=location.getLatitude();
      appCollectInfo.lng_bd=location.getLongitude();
      getAppCollectInfoCallBack();

    }
  }
  private BDAbstractLocationListener bdAbstractLocationListener = new BDAbstractLocationListener() {
    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
      getLoactionInfo(bdLocation,appCollectInfo);
    }

    @Override
    public void onLocDiagnosticMessage(int i, int i1, String s) {
      super.onLocDiagnosticMessage(i, i1, s);
      Log.i("chix-->>",i+" "+i1 + " "+s);

    }
  };
  private void getLocationByBaidu(Context mContext){
    myBDLocation= new BDLocationService(mContext.getApplicationContext());
    myBDLocation.registerListener(bdAbstractLocationListener);
    myBDLocation.setLocationOption(myBDLocation.getDefaultLocationClientOption());
    myBDLocation.start();
  }

  @Override
  public void onStop() {
    super.onStop();
    removeListeningNetWork(cordova.getActivity());
    myBDLocation.stop();
    myBDLocation.unregisterListener(bdAbstractLocationListener);
  }
}
