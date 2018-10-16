1.安装
  cordova plugin add <loc-path>cn-com-ths-infocollect --variable BD_AK=zfIDzFSHYkT2cIpqxMuppAEEb44zfS8e
  
  cordova plugin add cordova-plugin-app-version
  cordova plugin add cordova-plugin-geolocation

2.使用获取应用信息

   ThsAppCollectInfo.getAppInfo(function (message) {
       console.log(message)
   });

