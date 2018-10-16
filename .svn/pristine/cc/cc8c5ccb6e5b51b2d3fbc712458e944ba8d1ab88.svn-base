安装插件方法：
cordova plugin add <local path> cn-com-ths-ssoprovider

移除插件方法：

cordova plugin rm cn-com-ths-ssoprovider


1.存储用户配置信息 

 SSOProvider.saveUserInfo(userInfo,successCallback);
 
   userInfo--用户JSON字符串
   
 使用示例：
  var userInfo= "{\"userID\":" +  $scope.appInfo.userid + ",\"password\":" +  $scope.appInfo.pw+"}";
      SSOProvider.saveUserInfo(userInfo,function (code) {
          alert(code);//code>=0 表示成功
      });  
      
      
2.//获取用户信息

SSOProvider.getUserInfo(successCallback);
使用示例：
SSOProvider.getUserInfo(function (msg) {
          alert(msg);
      });


3.获取应用包名

SSOProvider.getPackageName(callBack);
使用示例：
SSOProvider.getPackageName(function(packm){
    alert(packm);
});

4.//字符加密
SSOProvider.crypto(msg,callBack); 
使用示例：
SSOProvider.crypto(msg,function(cryptoMsg){
    alert(cryptoMsg);//加密后的字符串
});