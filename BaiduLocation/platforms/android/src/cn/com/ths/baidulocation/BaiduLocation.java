package cn.com.ths.baidulocation;

import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by zapzqc on 2015/9/2.
 */
public class BaiduLocation extends CordovaPlugin {

    private static final String ACTION_GET_LOCATION_EVENT = "get";
    private static final String ACTION_STOP_LOCATION_EVENT = "stop";

    private LocationClient mLocationClient = null;
    private BDLocationListener myListener = new MyLocationListener();
    private CallbackContext callbackContext;
    private JSONObject locationInfo;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        mLocationClient = new LocationClient(cordova.getActivity());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        initLocation();
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        this.callbackContext = callbackContext;
        if (ACTION_GET_LOCATION_EVENT.equals(action)) {
            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    mLocationClient.start();
                }
            });
            return true;
        } else if (ACTION_STOP_LOCATION_EVENT.equals(action)) {
            mLocationClient.stop();
            return true;
        }
        return false;
    }

    @Override
    public void onStop() {
        mLocationClient.stop();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mLocationClient.stop();
        super.onDestroy();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
//        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                return;
            }
            //Receive Location
            try {
                locationInfo = new JSONObject();
                locationInfo.put("time", location.getTime());
                locationInfo.put("locType", location.getLocType());
                locationInfo.put("latitude", location.getLatitude());
                locationInfo.put("longitude", location.getLongitude());
                locationInfo.put("radius", location.getRadius());
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    locationInfo.put("speed", location.getSpeed());// 单位：公里每小时
                    locationInfo.put("satellite", location.getSatelliteNumber());
                    locationInfo.put("height", location.getAltitude());// 单位：米
                    locationInfo.put("direction", location.getDirection());// 单位：度
                    locationInfo.put("addr", location.getAddrStr());
                    locationInfo.put("describe", "gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    locationInfo.put("addr", location.getAddrStr());
                    //运营商信息
                    locationInfo.put("operationers", location.getOperators());
                    locationInfo.put("describe", "网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    locationInfo.put("describe", "离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    locationInfo.put("describe", "服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    locationInfo.put("describe", "网络不通导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    locationInfo.put("describe", "无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
                locationInfo.put("locationdescribe", location.getLocationDescribe());// 位置语义化信息
                callbackContext.success(locationInfo);
            } catch (JSONException e) {
                e.printStackTrace();
                callbackContext.error(e.toString());
            }
        }
    }
}