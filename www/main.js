
var exec = require('cordova/exec');

var PLUGIN_NAME = 'NativeContactPicker';

var NativeContactPicker = {

  parseLastNineDigits: function(number, cb) {
    exec(cb, null, PLUGIN_NAME, 'parseLastNineDigits', []);
  }

};

module.exports = NativeContactPicker;
