import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
declare let cordova: any;
@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {

  constructor(public navCtrl: NavController) {
    document.addEventListener("pdf.receiveMessage", (data)=>{
      console.log(data);
      
    }, false); //来了新消息调用的方法
  }
  //测试打开PDF方法
  openPdf(){
    console.log("1111");
    cordova.plugins.iapppdfths.openPdf("/storage/emulated/0/电信受理单.pdf","admin","SxD/phFsuhBWZSmMVtSjKZmm/c/3zSMrkV2Bbj5tznSkEVZmTwJv0wwMmH/+p6wLiUHbjadYueX9v51H9GgnjUhmNW1xPkB++KQqSv/VKLDsR8V6RvNmv0xyTLOrQoGzAT81iKFYb1SZ/Zera1cjGwQSq79AcI/N/6DgBIfpnlwiEiP2am/4w4+38lfUELaNFry8HbpbpTqV4sqXN1WpeJ7CHHwcDBnMVj8djMthFaapMFm/i6swvGEQ2JoygFU3W8onCO1AgMAD2SkxfJXM/mX1uF23u5oNhx5kpmkBkb3x6aD2yiupr6ji7hzsE6/Qng3l3AbK2vtwyJLdcl2md6r5JJO51PJS2vAlVxcmvGGVWEbHWAH22+t7LdPt+jENOIq5GN/n4KME0L/SFgUD1b8zb/8DFI+sDLA8bVOqHBiSgCNRP4FpYjl8hG/IVrYXOzDNrpoUGsPwMMlLKBA40uW8fXpxdRHfEuWC1PB9ruQ=");
  }
}
