
var exec = require('cordova/exec');

var PLUGIN_NAME = 'NativeContactPicker';

var NativeContactPicker = {

  pickContact: function(cb) {
    exec(cb, null, PLUGIN_NAME, 'pickContact', []);
  }

};

module.exports = NativeContactPicker;
