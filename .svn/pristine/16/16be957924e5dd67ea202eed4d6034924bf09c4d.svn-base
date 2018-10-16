package cn.com.ths.ssoprovider;

import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * @author chixin 
 * @version v1.0
 *
 */
public class SPHelper {
	public static Context context;
	private static  String contentBaseUri;
//	private static  String contentBaseUri = ConstantUtils.CONTENT+ConstantUtils.AUTHORITY+ConstantUtils.SEPARATOR;
	private static String TYPE_GET_USER_CONFIG="get_user_config";//查寻类型

	public static void init(Application context) {
		SPHelper.context = context.getApplicationContext();
		contentBaseUri=ConstantUtils.getContUri(context);
	}
	/**
	 * 存储数据
	 * 
	 * @param key
	 * @param v
	 * @return
	 */
	public synchronized static int save(String key, Object v) {
		ContentResolver cr = context.getContentResolver();
		ContentValues cv = new ContentValues();
		if (v instanceof String) {
			cv.put(key, (String) v);
		} else if (v instanceof Boolean) {
			cv.put(key, (Boolean) v);
		} else if (v instanceof Long) {
			cv.put(key, (Long) v);
		} else if (v instanceof Double) {
			cv.put(key, (Double) v);
		} else if (v instanceof Float) {
			cv.put(key, (Float) v);
		} else if (v instanceof Integer) {
			cv.put(key, (Integer) v);
		}
		Uri uri = Uri.parse(contentBaseUri + TYPE_GET_USER_CONFIG);
		return cr.update(uri, cv, null, null);
	}
    /**
     * 查询sp值
     * @param key
     * @param defaultValue
     * @return
     */
	public static String getString(String key, String defaultValue) {
		ContentResolver cr = context.getContentResolver();
		Uri uri = Uri.parse(contentBaseUri + TYPE_GET_USER_CONFIG);
		Cursor cursor=cr.query(uri, null, null, null, null);
		if(cursor!=null&&cursor.moveToNext()){
			return cursor.getString(cursor.getColumnIndex(key));
		}
		return defaultValue;
		
	}
}
