define([], function () {
  'use strict';

  function controller(restClientService) {
    var _this = this;
    _this.newUser = {};
    _this.registrationDone = false;
    _this.registrationOpened = false;

    this.registerUser = function registerUser() {
      var hash = CryptoJS.SHA256(_this.newUser.password);
      _this.newUser.password = hash.toString(CryptoJS.enc.Base64);
      restClientService.registerUser(_this.newUser).then(function () {
        _this.registrationDone = true;
      });
    }
  }

  controller.$inject = ['restClientService'];

  return controller;
});