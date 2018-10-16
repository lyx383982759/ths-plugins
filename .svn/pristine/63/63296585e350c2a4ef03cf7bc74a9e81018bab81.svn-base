package cn.com.ths.ssoprovider;

import android.content.Context;

/**
 * @author chixin 2017-03-31
 * @version v1.0
 */
public class ConstantUtils {
//     public static final String AUTHORITY="cn.com.ths.ssoprovider";
     public static final String CONTENT="content://";
     public static final String SEPARATOR="/";
     
 	 public static final int MATCH_USERINFO_CODE = 100; //uri 匹配码
 	 public static final String TYPE_GET_USER_CONFIG="get_user_config";//查寻类型 path
     
     public static final String USER_INFO="user_info";//sp key
     
     public static String getAuthority(Context mContext){
    	 return mContext.getPackageName();
     }
     public static String getContUri(Context mContext){
    	 String pm=getAuthority(mContext);
    	 if(pm==null){
    		 return null;
    	 }
    	 return CONTENT+pm+SEPARATOR;
     }
}
