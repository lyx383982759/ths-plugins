var exec = require('cordova/exec');
module.exports = {
    /**
     *
     * exec方法一共5个参数:
     * 第一个:成功回调
     * 第二个:失败回调
     * 第三个:将要调用的对象名(在config.xml中配置,对应于feature的name属性，value就是本地实现的java类)
     * 第四个:调用的方法名(java类中通过action识别方法名)
     * 第五个:传递的参数（json格式）
     */
    startActivity: function(cls, userInfo) {
        cordova.exec(null, null, "ThsAarLauncherUtil", "startActivity", [cls, userInfo]);
    },
    /**
     * 初始化应急模块
     */
    //initAARMoudules: function(userId, serverHome,mqHome) {
    //    cordova.exec(null, null, "ThsAarLauncherUtil", "initAARMoudules", [userId, serverHome,mqHome]);
    //},
	initAARMoudules: function(userId) {
        cordova.exec(null, null, "ThsAarLauncherUtil", "initAARMoudules", [userId]);
    }

};