/*
 * BookShower.java
 * classes : com.kinggrid.iapppdf.demo.BookShower
 * @author 涂博之
 * V 1.0.0
 * Create at 2014年5月20日 下午4:35:00
 */
package cn.com.ths.iapppdfths;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.kinggrid.iapppdf.ui.viewer.IAppPDFActivity;
import com.kinggrid.iapppdf.ui.viewer.PDFHandWriteView;

import org.json.JSONArray;
import org.json.JSONException;

import cn.com.ths.iapppdfths.dialogs.SignConfigDialog;
import cn.com.ths.iapppdfths.dialogs.SignDialog;


/**
 * com.kinggrid.iapppdf.demo.BookShower
 * 
 * @author 涂博之 <br/>
 *         create at 2016年12月22日 下午16:35:00
 */
@SuppressLint("NewApi")
public class BookShower extends IAppPDFActivity implements ConstantValue, OnClickListener{

	private static final String TAG = "BookShower";
	public static final String PDF_SAVE = "PDF_SAVE";
	public static final String PDF_CLOSE = "PDF_CLOSE";

	private FrameLayout frameLayout;
	private LinearLayout toolbar;//工具栏
	private Context context;
	
	private Button toolbar_btn_save,toolbar_btn_signature, toolbar_btn_full_signature;
	private PDFHandWriteView full_handWriteView;//手写视图
	/**
	 * 全文批注按钮
	 */
	public Button btnClose, btnClear, btnUndo, btnRedo, btnSave, btnPen, btnEarse;
	private View handwriteView_layout;
	private SignDialog signWindow;
	
	@Override
	protected void onCreateImpl(Bundle savedInstanceState) {
		super.onCreateImpl(savedInstanceState);
		context = this;
		setContentView(R.layout.kinggrid_bookshower_layout);
		frameLayout = (FrameLayout) findViewById(R.id.book_frame);
//        PermissionsUtil.TipInfo tip = new PermissionsUtil.TipInfo("注意:", "需要访问您的存储信息", "拒绝访问", "打开权限");
//        PermissionsUtil.requestPermission(this, new PermissionListener() {
//            @Override
//            public void permissionGranted(@NonNull String[] permissions) {
//
//            }
//
//            @Override
//            public void permissionDenied(@NonNull String[] permissions) {
//                Toast.makeText(BookShower.this, "用户拒绝了权限", Toast.LENGTH_LONG).show();
//            }
//        }, new String[]{Manifest.permission_group.STORAGE,Manifest.permission_group.PHONE}, true, tip);

		this.initPDFParams();
		super.initPDFView(frameLayout);
		this.initToolBar();
		super.setLoadingText(R.string.msg_loading_tip);
		
	}
	
	@Override
	protected void onPauseImpl(boolean finishing) {
		super.onPauseImpl(finishing);
		if(signWindow != null){
			signWindow.pause();
		}
		
	}
	
	@Override
	protected void onDestroyImpl(boolean finishing) {
		super.onDestroyImpl(finishing);
		System.exit(0);
	}
	
	/**
	 * 初始化金格控件的参数,根据各自需求重写
	 */
	private void initPDFParams(){
		Intent book_intent = getIntent();
		//用户名，默认admin
		if (book_intent.hasExtra(NAME)) {
			userName = book_intent.getStringExtra(NAME); //用户名，默认admin
		}
		
		//授权码(必传)
		if (book_intent.hasExtra(LIC)){
			copyRight = book_intent.getStringExtra(LIC); //授权码，必传，字符串格式
		}
		
	}

	/**
	 * 初始化工具栏
	 */
	private void initToolBar() {
		toolbar = (LinearLayout) findViewById(R.id.toolbar);
		
		toolbar_btn_save = (Button) findViewById(R.id.toolbar_btn_save);
		toolbar_btn_save.setOnClickListener(this);
		toolbar_btn_signature = (Button) findViewById(R.id.toolbar_btn_signature);
		toolbar_btn_signature.setOnClickListener(this);
		toolbar_btn_full_signature = (Button) findViewById(R.id.toolbar_btn_full_signature);
		toolbar_btn_full_signature.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		if (isLocked) {//当文档被锁定了，则点击按钮不响应
			return;
		}
		
		if(IAppPDFActivity.progressBarStatus == 1){//还在加载页面时，不响应按钮点击操作
			Toast.makeText(context, "正在加载页面，请稍后尝试！", Toast.LENGTH_SHORT).show();
			return;
		}
		int i = v.getId();
		if (i == R.id.toolbar_btn_save) {
			final Builder builder = new Builder(context);
			builder.setMessage(getString(R.string.dialog_message_save));
			builder.setTitle(getString(R.string.dialog_title));
			builder.setPositiveButton(getString(R.string.ok),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
											int which) {
							dialog.dismiss();
							saveAndExit();//保存文档并退出
						}
					});
			builder.setNegativeButton(getString(R.string.cancel),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
											int which) {
							dialog.dismiss();
						}
					});

			final Dialog dialog1 = builder.create();
			dialog1.setCancelable(false);
			dialog1.show();

		} else if (i == R.id.toolbar_btn_signature) {
			showSignWindow("Signature2");

		} else if (i == R.id.toolbar_btn_full_signature) {//当全文签批时，若页面还在滚动，则自动停止
			if (!getController().getView().isScrollFinished()) {
				getController().getView().forceFinishScroll();
			}
			handwriteView_layout = View.inflate(context, R.layout.kinggrid_signature_full, null);
			full_handWriteView = (PDFHandWriteView) handwriteView_layout.findViewById(R.id.v_canvas);
			initBtnView(handwriteView_layout);
			openHandwriteAnnotation(handwriteView_layout, full_handWriteView);

		}
	}
	
	
	/**
	 * 显示签名界面
	 * @param fieldName 签名域名称
	 */
	private void showSignWindow(final String fieldName){
		if(hasFieldInDocument(fieldName)){//判断文档是否存在该域
			lockScreen();//锁定文档
			signWindow = new SignDialog(BookShower.this,copyRight);//初始化签名框视图
			signWindow.show();//显示签名框
			//设置保存签名监听
			signWindow.setSignatureListener(new SignDialog.OnSignatureListener() {
				
				@Override
				public void onSignatureSave(final PDFHandWriteView handWriteView) {
					//域定位
					if(hasFieldInDocument(fieldName)){
						doSaveSignByFieldName(handWriteView, fieldName);
					} else {
						Toast.makeText(context, R.string.no_signature2,Toast.LENGTH_SHORT).show();
					}
					signWindow.dismiss();
					refreshDocument();
				}  
			});
			unLockScreen();//解锁文档
		} else {
			Toast.makeText(context, R.string.no_signature2,Toast.LENGTH_SHORT).show();
		}
	}
	
	Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_WHAT_DISMISSDIALOG://批注保存完毕之后，进度条与手写界面消失
				View rm_view = (View) msg.obj;
				hiddenViewFromPDF(rm_view);
				hiddenViewFromPDF(handwriteView_layout);
				break;
			case MSG_WHAT_REFRESHDOCUMENT://刷新文档
				refreshDocument();
				break;
			}
		}
	};
	
	/**
	 * 显示保存对话框
	 * @param context
	 */
	private void showSaveDialog(Context context){
		final Builder builder = new Builder(context);
		builder.setMessage(getString(R.string.dialog_save_info));
		builder.setTitle(getString(R.string.dialog_title));
		//保存按钮
		builder.setPositiveButton(R.string.btn_save, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				saveAndExit();
			}
		});
		//不保存按钮
		builder.setNeutralButton(R.string.btn_no_save, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				closeDocument();
				sendBroadcast(new Intent(PDF_CLOSE));
				finish();
				android.os.Process.killProcess(android.os.Process.myPid());
				
			}
		});
		//取消按钮
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
			}
		});
		
		
		final Dialog dialog1 = builder.create();
		dialog1.setCancelable(false);
		dialog1.show();
	}
	
	/**
	 * 保存并退出金格控件
	 */
	private void saveAndExit(){
		saveDocument();//保存文档
		closeDocument();//关闭文档
		sendBroadcast(new Intent(PDF_SAVE));
		finish();//结束当前activity
		android.os.Process.killProcess(android.os.Process.myPid());//必需要杀掉进程
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//按返回键
		if((keyCode == KeyEvent.KEYCODE_BACK)){
			if(!isDocumentModified()){
				final Builder builder = new Builder(context);
				builder.setTitle(getString(R.string.dialog_title));
				builder.setMessage(getString(R.string.close_doc_title));
				builder.setPositiveButton(getString(R.string.ok),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								closeDocument();//关闭文档
								sendBroadcast(new Intent(PDF_CLOSE));
								finish();
								android.os.Process.killProcess(android.os.Process.myPid());//杀死进程
							}
						});
				builder.setNegativeButton(getString(R.string.cancel),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});

				final Dialog dialog1 = builder.create();
				dialog1.setCancelable(false);
				dialog1.show();
			}else{
				showSaveDialog(context);
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private SignConfigDialog configDialog;
	
	//初始化手写界面按钮
	private void initBtnView(final View layout) {
		btnClose = (Button) layout.findViewById(R.id.btn_close);
		btnClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//判断手写界面当前状态，如处于擦除状态，则退出擦除状态；
				if(full_handWriteView.getEarseState()){
					full_handWriteView.cancelEarseHandwriteInfo();
					btnUndo.setVisibility(View.VISIBLE);
					btnRedo.setVisibility(View.VISIBLE);
					btnClear.setVisibility(View.VISIBLE);
					btnPen.setVisibility(View.VISIBLE);
					btnSave.setVisibility(View.VISIBLE);
					btnEarse.setVisibility(View.VISIBLE);
				}else{
					doCloseHandwriteInfo(layout,full_handWriteView);
				}
				
			}
		});
		
		btnSave = (Button) layout.findViewById(R.id.btn_save);
		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(full_handWriteView.canSave()){
					
					if(insertVertorFlag != 0){
						return;
					}
					
					//显示进度条
					final View dialog_view = getLayoutInflater().inflate(R.layout.insert_progress, null);
					showViewToPDF(dialog_view);
					
					Runnable runnable = new Runnable() {
						
						@Override
						public void run() {
							doSaveHandwriteInfo(true,false,full_handWriteView);
							
							//取消等待交互
							Message msg = new Message();
							msg.what = MSG_WHAT_DISMISSDIALOG;
							msg.obj = dialog_view;
							myHandler.sendMessage(msg);
							
						}
					};
					Thread thread = new Thread(runnable);
					thread.start();
				} else {
					Toast.makeText(context, "没有保存内容", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		btnEarse = (Button) layout.findViewById(R.id.btn_earse);
		btnEarse.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				full_handWriteView.doEarseHandwriteInfo();
				btnUndo.setVisibility(View.GONE);
				btnRedo.setVisibility(View.GONE);
				btnClear.setVisibility(View.GONE);
				btnPen.setVisibility(View.GONE);
				btnSave.setVisibility(View.GONE);
				btnEarse.setVisibility(View.GONE);
			}
			
		});
		
		btnUndo = (Button) layout.findViewById(R.id.btn_undo);
		btnUndo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				full_handWriteView.doUndoHandwriteInfo();
			}
		});
		
		btnRedo = (Button) layout.findViewById(R.id.btn_redo);
		btnRedo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				full_handWriteView.doRedoHandwriteInfo();
			}
		});
		
		btnClear = (Button) layout.findViewById(R.id.btn_clear);
		btnClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				full_handWriteView.doClearHandwriteInfo();
			}
		});
		
		btnPen = (Button) layout.findViewById(R.id.btn_pen);
		btnPen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*full_handWriteView.setPenSettingInfoName("demo_type", "demo_size", "demo_color");
				full_handWriteView.doSettingHandwriteInfo();*/
				configDialog = new SignConfigDialog(context, full_handWriteView,"sign_size","sign_color","sign_type",50);
				configDialog.showSettingWindow(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			}
		});
		
	}

	
	
}
