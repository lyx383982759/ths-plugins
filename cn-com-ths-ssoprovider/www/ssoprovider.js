var exec = require('cordova/exec');
module.exports = {
	/**
	 * 
	 * exec方法一共5个参数: 第一个 :成功回调 第二个 :失败回调 第三个
	 * :将要调用的对象名(在config.xml中配置,对应于feature的name属性，value就是本地实现的java类) 第四个
	 * :调用的方法名(java类中通过action识别方法名) 第五个 :传递的参数（json格式）
	 */
	// 通过调用cordova.exec（）方法链接本地的java实现。
	saveUserInfo:function(userInfo,successCallback) {
		cordova.exec(successCallback, null, "SSOProvider", "save", [userInfo]);
	},
	getUserInfo:function(successCallback) {
		cordova.exec(successCallback, null, "SSOProvider", "get", []);
	},
	getPackageName:function(successCallback){
		cordova.exec(successCallback, null, "SSOProvider", "app_get_pm", []);
	},
	crypto:function(msg,successCallback){
	    cordova.exec(successCallback, null, "SSOProvider", "action_crypto", [msg]);
	}
	
};
