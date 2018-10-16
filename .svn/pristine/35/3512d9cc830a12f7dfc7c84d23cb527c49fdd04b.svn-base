package cn.com.ths.appcollect;

import android.os.Build;

import org.json.JSONException;
import org.json.JSONObject;

public class AppCollectInfo {
    /**
     *  获取 硬件制造商
     * @return
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     *  获取 系统定制商
     * @return
     */
    public String getBrand() {
        return brand;
    }

    /**
     * 获取 主板信息
     * @return
     */
    public String getBoard() {
        return board;
    }

    /**
     * 获取 显示屏参数
     * @return
     */
    public String getDisplay() {
        return display;
    }

    /**
     * 获取 设备型号
     * @return
     */
    public String getDeviceMode() {
        return deviceMode;
    }

    /**
     * 获取 系统版本号
     * @return
     */
    public String getSysVersion() {
        return sysVersion;
    }

    /**
     * 获取 android 版本号
     * @return
     */
    public int getSdkVersion() {
        return sdkVersion;
    }

    /**
     * 获取 设备串号
     * @return
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * 获取 应用版本号
     * @return
     */
    public int getAppVer() {
        return appVer;
    }

    /**
     * 获取 应用包名
     * @return
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * 获取 网络类型
     * @return
     */
    public String getNetworkType() {
        return networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    /**
     * 获取 app 版本号名名称
     * @return
     */
    public String getAppVerName() {
        return appVerName;
    }

    /**
     * 获取 手机号码
     * @return
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * 获取 运营商
     * @return
     */
    public String getProvidersName() {
        return providersName;
    }

    /**
     * 获取 经度
     * @return
     */
    public double getLng() {
        return lng;
    }

    /**
     * 设置 经度
     * @param lng
     */
    public void setLng(double lng) {
        this.lng = lng;
    }

    /**
     * 获取 纬度
     * @return
     */
    public double getLat() {
        return lat;
    }

    /**
     * 设置 纬度
     * @param lat
     */
    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setProvidersName(String providersName) {
        this.providersName = providersName;
    }

    public void setAppVer(int appVer) {
        this.appVer = appVer;
    }

    public void setAppVerName(String appVerName) {
        this.appVerName = appVerName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    private String transNullValue(String s) {
        if (s == null || "null".equals(s)) {
            return "";
        }
        return s;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("硬件制造商：").append(transNullValue(this.manufacturer)).append("\r\n")
                .append("系统定制商：").append(transNullValue(this.brand)).append("\r\n")
                .append("主板型号：").append(transNullValue(this.board)).append("\r\n")
                .append("显示屏参数：").append(transNullValue(this.display)).append("\r\n")
                .append("手机型号：").append(transNullValue(this.deviceMode)).append("\r\n")
                .append("系统版本号：").append(transNullValue(this.sysVersion)).append("\r\n")
                .append("android sdk 版本号：").append(this.sdkVersion).append("\r\n")
                .append("IMEI：").append(transNullValue(this.deviceId)).append("\r\n")
                .append("手机号码：").append(transNullValue(this.phoneNumber)).append("\r\n")
                .append("运营商：").append(transNullValue(this.providersName)).append("\r\n")
                .append("应用版本号：").append(this.appVer).append("\r\n")
                .append("应用版本号名称：").append(transNullValue(this.appVerName)).append("\r\n")
                .append("应用包名：").append(transNullValue(this.packageName)).append("\r\n")
                .append("网络类型：").append(transNullValue(this.networkType)).append("\r\n")
                .append("经度：").append(this.lng).append("\r\n")
                .append("纬度：").append(this.lat).append("\r\n")
                .append("来自百度定位-经度：").append(this.lng_bd).append("\r\n")
                .append("来自百度定位-纬度：").append(this.lat_bd).append("\r\n")
        ;
        return sb.toString();
    }
    public JSONObject toJSONObject(){
      JSONObject jsonObject =new JSONObject();
      try {
        jsonObject.put("manufacturer",this.manufacturer);
        jsonObject.put("brand",this.brand);
        jsonObject.put("board",this.board);
        jsonObject.put("display",this.display);
        jsonObject.put("deviceMode",this.deviceMode);
        jsonObject.put("sysVersion",this.sysVersion);
        jsonObject.put("sdkVersion",this.sdkVersion);
        jsonObject.put("deviceId",this.deviceId);
        jsonObject.put("phoneNumber",this.phoneNumber);
        jsonObject.put("providersName",this.providersName);
        jsonObject.put("appVer",this.appVer);
        jsonObject.put("appVerName",this.appVerName);
        jsonObject.put("packageName",this.packageName);
        jsonObject.put("networkType",this.networkType);
        jsonObject.put("longitude",this.lng_bd);
        jsonObject.put("latitude",this.lat_bd);
      } catch (JSONException e) {
        e.printStackTrace();
      }
      return  jsonObject;
    }
    public AppCollectInfo() {}
// 设备信息
    /** 硬件制造商 */
    private String manufacturer = Build.MANUFACTURER;
    /** 系统定制商 */
    private String brand = Build.BRAND;
    /** 主板 */
    private String board=Build.BOARD;
    /** 显示屏参数*/
    private String display = Build.DISPLAY;
    /** 型号 */
    private String deviceMode = Build.MODEL;
    /** 系统版本号 */
    private String sysVersion = Build.VERSION.RELEASE;
    /** android sdk 版本号 */
    private int sdkVersion = Build.VERSION.SDK_INT;
    // 电话信息
    /** 串号 */
    private String deviceId;
    /** 手机号码 */
    private String phoneNumber;
    private String providersName;

    // 应用信息
    /** 应用版本号*/
    private int appVer;
    /** 应用版本号名称 */
    private String appVerName;
    /** 应用包名 */
    private String packageName;

    // 网络信息
    /** 网络类型 */
    private String networkType;

    // 位置信息
    /** 经度 */
    private double lng;
    /** 纬度 */
    private double lat;

    /** 经度 */
    public double lng_bd;
    /** 纬度 */
    public double lat_bd;
}
