cordova.define('cordova/plugin_list', function(require, exports, module) {
module.exports = [
    {
        "file": "plugins/cn-com-ths-baidulocation/www/baidulocation.js",
        "id": "cn-com-ths-baidulocation.baidulocation",
        "pluginId": "cn-com-ths-baidulocation",
        "clobbers": [
            "navigator.baidulocation"
        ]
    },
    {
        "file": "plugins/cordova-plugin-whitelist/whitelist.js",
        "id": "cordova-plugin-whitelist.whitelist",
        "pluginId": "cordova-plugin-whitelist",
        "runs": true
    }
];
module.exports.metadata = 
// TOP OF METADATA
{
    "cn-com-ths-baidulocation": "1.0.0",
    "cordova-plugin-whitelist": "1.0.0"
}
// BOTTOM OF METADATA
});