package cn.com.ths.iapppdfths;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager; 
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * com.kinggrid.iapppdf.demo.MainActivity
 * 
 * @author 涂博之<br/>
 *         create at 2016年12月22日 下午14:57:46
 */
public class MainActivity extends Activity implements ConstantValue {
	private static final String TAG = "MainActivity";

	private Context context;//上下文
	private String userName = "admin";//用户名
	
	//金格科技iApp试用许可A02    过期时间=2018-09-26
	private String copyRight = "SxD/phFsuhBWZSmMVtSjKZmm/c/3zSMrkV2Bbj5tznSkEVZmTwJv0wwMmH/+p6wLiUHbjadYueX9v51H9GgnjUhmNW1xPkB++KQqSv/VKLDsR8V6RvNmv0xyTLOrQoGzAT81iKFYb1SZ/Zera1cjGwQSq79AcI/N/6DgBIfpnlwiEiP2am/4w4+38lfUELaNFry8HbpbpTqV4sqXN1WpeJ7CHHwcDBnMVj8djMthFaapMFm/i6swvGEQ2JoygFU3W8onCO1AgMAD2SkxfJXM/mX1uF23u5oNhx5kpmkBkb3x6aD2yiupr6ji7hzsE6/Qng3l3AbK2vtwyJLdcl2md6r5JJO51PJS2vAlVxcmvGGVWEbHWAH22+t7LdPt+jENOIq5GN/n4KME0L/SFgUD1b8zb/8DFI+sDLA8bVOqHBiSgCNRP4FpYjl8hG/IVrYXOzDNrpoUGsPwMMlLKBA40uW8fXpxdRHfEuWC1PB9ruQ=";
	
	private String filePath = SDCARD_PATH + "/电信受理单.pdf";//文件本地路径
	private Button launch_btn;//启动按钮

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		context = this;
		launch_btn  = new Button(context);//创建启动按钮，整个界面就是一个按钮
		launch_btn.setText(R.string.launch_pdf);//设置按钮文本
		launch_btn.setTextSize(18);//设置按钮文本文字大小
		
		setContentView(launch_btn);//设置activity的内容

		copyFiles();//拷贝数据，正常使用中可忽略
		
		//设置按钮点击事件的监听器
		launch_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				doOpenFile(filePath);//打开PDF文档
			}
		});
	}
	
	/**
	 * 为了方便使用，拷贝一些文件到设备本地，在实际使用中可忽略
	 */
	private void copyFiles(){
		try {
			copyAssetsFileToSDCard("dx.pdf", filePath);
		} catch (IOException e) {
			Log.e(TAG, e.toString());
		}
	}
	
	/**
	 * 拷贝PDF文档到设备本地，方便演示功能
	 * @param fileName  文件名
	 * @param toFilePath 指定文件路径
	 * @throws IOException
	 */
	private void copyAssetsFileToSDCard(String fileName, String toFilePath)
			throws IOException {
		File file = new File(toFilePath);
		if (file.exists()) {
			return;
		}
		InputStream myInput;
		OutputStream myOutput = new FileOutputStream(toFilePath);
		myInput = getAssets().open(fileName);
		byte[] buffer = new byte[1024];
		int length = myInput.read(buffer);
		while (length > 0) {
			myOutput.write(buffer, 0, length);
			length = myInput.read(buffer);
		}
		myOutput.flush();
		myInput.close();
		myOutput.close();
	}
	
	/**
	 * 打开继承金格控件的Activity，并传递需要的参数
	 * @param filepath 要打开的文件路径
	 */
	private void doOpenFile(String filepath) {
		File file = new File(filepath);
		Uri uri = Uri.fromFile(file);//获取文件的uri
		
		//设置intent的action
		Intent intent = new Intent("android.intent.action.VIEW", uri);
		//设置跳转的activity
		intent.setClassName(this,
				"cn.com.ths.iapppdfths.BookShower");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//传递用户名，默认admin
		intent.putExtra(NAME, userName);
		//传递授权码(必传)
		intent.putExtra(LIC, copyRight);

		startActivity(intent);
	}
}
