package cn.com.ths.ssoprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

/**
 * @author chixin
 * @version v1.0
 *
 */
public class SPContentProvider extends ContentProvider{
    SPHelperImpl spHandle;
    
	private static UriMatcher uriMatcher;
//	static {
//		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
//		/**
//		 * uriMatcher.addURI(authority, path, code); 其中
//		 * authority：主机名(用于唯一标示一个ContentProvider,这个需要和清单文件中的authorities属性相同)
//		 * path:路径路径(可以用来表示我们要操作的数据，路径的构建应根据业务而定)
//		 * code:返回值(用于匹配uri的时候，作为匹配成功的返回值)
//		 */
//		uriMatcher.addURI(ConstantUtils.AUTHORITY, ConstantUtils.TYPE_GET_USER_CONFIG, ConstantUtils.MATCH_USERINFO_CODE);// 匹配记录集合
//	}
    @Override
    public boolean onCreate() {
    	if(spHandle==null)
    	spHandle=new SPHelperImpl(getContext());
    	uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		/**
		 * uriMatcher.addURI(authority, path, code); 其中
		 * authority：主机名(用于唯一标示一个ContentProvider,这个需要和清单文件中的authorities属性相同)
		 * path:路径路径(可以用来表示我们要操作的数据，路径的构建应根据业务而定)
		 * code:返回值(用于匹配uri的时候，作为匹配成功的返回值)
		 */
		uriMatcher.addURI(ConstantUtils.getAuthority(getContext()), ConstantUtils.TYPE_GET_USER_CONFIG, ConstantUtils.MATCH_USERINFO_CODE);// 匹配记录集合
        return true;
    }
    @Override
    public String getType(Uri uri) {
		return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int code=uriMatcher.match(uri);
        switch (code) {
		case ConstantUtils.MATCH_USERINFO_CODE:
			  String value=spHandle.getDecryptValue(ConstantUtils.USER_INFO);
	           MatrixCursor cursor = new MatrixCursor(new String[]{ConstantUtils.USER_INFO});
	           cursor.addRow(new Object[]{value});
	           return cursor;
        }
        return null;
    }
    @Override
    public Uri insert(Uri uri, ContentValues values) {
    	int code=uriMatcher.match(uri);
    	switch (code) {
   		case ConstantUtils.MATCH_USERINFO_CODE:
   			Object obj=values.get(ConstantUtils.USER_INFO);
   			spHandle.saveEncryptValue(ConstantUtils.USER_INFO, obj);
    	}
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
       insert(uri,values);
       return 0;
    }
}
