Cordova Native Contact Picker for Android
======

This plugin puropse is to use native android contact book to pick phone numbers from your contact book.

To install use:
```
cordova plugin add native-contact-picker
```
How to use plugin:
```
window.NativeContactPicker.pickContact(function(e) {
  console.log("parseLastNineDigits: ", e);
});
```
