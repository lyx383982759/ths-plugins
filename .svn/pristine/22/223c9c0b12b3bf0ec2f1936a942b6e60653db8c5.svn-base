package cn.com.ths.ssoprovider;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 
 * @author chixin   2017-03-31
 * @version v1.0
 *
 */
public class SPHelperImpl {
	private final String PRE_NAME = "THSSSO_PROVIDER";
	private SharedPreferences sp = null;
	SimpleCrypto mSimpleCrypto = null;

	public SPHelperImpl(Context mContext) {
		sp = mContext.getSharedPreferences(PRE_NAME, Context.MODE_PRIVATE);
		mSimpleCrypto = new SimpleCrypto();
	}
	/**
	 * 加密数据
	 * @param t   
	 * @return
	 */
	private <T> String encrypto(T t){
		return mSimpleCrypto.encode(String.valueOf(t));
	}
	/**
	 * 解密字符串
	 * @param txt   加密字符串
	 * @return
	 */
	private String decrypto(String txt){
		return mSimpleCrypto.decode(txt);
	}
	/**
	 * 加密保存数据
	 * @param key    原始键值
	 * @param src    原始数据
	 */
	synchronized void saveEncryptValue(String key, Object src) {
		if (sp == null)return;
		SharedPreferences.Editor editor = sp.edit();
		key=encrypto(key);
		String value=encrypto(src);
		editor.putString(key, value);
		editor.commit();
	}
	/**
	 * 获取解密字符串
	 * @param src  原始键值
	 * @return     解密后的数据
	 */
	public String getDecryptValue(String src) {
		String key=encrypto(src);
		if (sp == null) return null;
		String value=sp.getString(key, null);
		return value==null?"":decrypto(value);
	}
}
