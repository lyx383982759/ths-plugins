# 安装方式：
 cordova plugin add  `<local-path>` cn-com-ths-libaar

# 移除插件

 cordova plugin rm  cn-com-ths-libaar
  
 ## Android Quirks   
 
 方法
-------

- cordova.plugins.ThsAarLauncherUtil.startActivity
- cordova.plugins.ThsAarLauncherUtil.initAARMoudules


cordova.plugins.ThsAarLauncherUtil.startActivity(cls,userJson);
=================
启动指定的activity

    参数说明：
    cls       目标activity名称（包名+类名）
    userJson  通过intent传值（json）

使用示例：

    cordova.plugins.ThsAarLauncherUtil.startActivity('com.demo.test.library_arr.LibMainActivity','{"userId":"admin"}');


cordova.plugins.ThsAarLauncherUtil.initAARMoudules(userId,homeServer);
================
初始化应急模块

    参数说明：
    userId        用户Id
    homeServer    服务地址 (ip:端口/项目名)

使用示例：

    cordova.plugins.ThsAarLauncherUtil.initAARMoudules('admin','222.128.15.41:9527/eims_haidian');