define([], function () {
  'use strict';

  function controller(userRestClientService, vcRecaptchaService) {
    var _this = this;
    _this.newUser = {};
    _this.registrationDone = false;
    _this.registrationOpened = false;

    _this.setResponse = function (response) {
      _this.response = response;
    };
    _this.setWidgetId = function (widgetId) {
      _this.widgetId = widgetId;
    };
    _this.cbExpiration = function () {
      _this.response = null;
    };

    this.registerUser = function registerUser() {
      userRestClientService.validateRecaptcha(_this.response).then(function (response) {
        if (response.userIsHuman) {
          var hash = CryptoJS.SHA256(_this.npassword);
          _this.newUser.password = hash.toString(CryptoJS.enc.Base64);
          userRestClientService.registerUser(_this.newUser).then(function () {
            _this.registrationDone = true;
          });
        } else {
          vcRecaptchaService.reload(_this.widgetId);
        }
      });
    }
  }

  controller.$inject = ['userRestClientService', 'vcRecaptchaService'];

  return controller;
});