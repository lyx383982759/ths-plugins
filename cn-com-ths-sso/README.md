1.安装插件

  cordova plugin add <local path> cn-com-ths-sso
  
  
2.移除插件 

 cordova plugin rm cn-com-ths-sso
 
 

3.获取用户登陆信息

THSSsoHandler.anthorize(succcallback,errorCallback);
  使用示例：
  THSSsoHandler.anthorize(function(succ){
    alert(succ);
},function(error){
	alert(error);
});


4. 获取额外的信息
 THSSsoHandler.getExtraInfo(succcallback);
 使用示例：
  THSSsoHandler.getExtraInfo(function(extraInfo){
  	alert(extraInfo);
  });
  
 