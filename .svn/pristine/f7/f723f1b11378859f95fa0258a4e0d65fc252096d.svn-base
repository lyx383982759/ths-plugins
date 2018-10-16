var exec = require('cordova/exec');

exports.openPdf = function (arg0,arg1,arg2, success, error) {
    exec(success, error, 'iapppdfths', 'openPdf', [arg0,arg1,arg2]);
};
exports.closePdfInAndroidCallback = function (data) {
    cordova.fireDocumentEvent('pdf.receiveMessage', data);
};

