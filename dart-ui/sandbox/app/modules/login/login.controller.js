define([
  'progressbar',
  'cryptojs.sha256',
  'cryptojs.base64'
], function (progress) {
  'use strict';

  controller.$inject = [
    'facebookService',
    'googleService',
    'authenticationService',
    '$window',
    '$location'
  ];

  function controller(facebookService, googleService, authenticationService, $window, $location) {
    var _this = this;

    _this.loginFailed = false;

    _this.email = "";
    _this.password = "";

    _this.fbLogin = function () {
      progress.increment();
      facebookService.fbLogin(_this.redirectToHome, _this.authFail);
    }

    _this.gpLogin = function () {
      progress.increment();
      googleService.gpLogin(_this.redirectToHome, _this.authFail);
    }

    _this.basicLogin = function () {
      progress.increment();
      var hash = CryptoJS.SHA256(_this.password);
      var mode = {
        provider: "basic",
        token: hash.toString(CryptoJS.enc.Base64)
      }
      authenticationService.authenticate(_this.email, mode, _this.redirectToHome, _this.authFail);
    }

    _this.redirectToHome = function (stat) {
      progress.done();
      if (stat) {
        $window.location.href = $location.protocol() + "://" + location.host + "/#" + _this.getParameterByName('next');
      } else {
        _this.loginFailed = true;
      }
    };

    _this.authFail = function () {
      _this.loginFailed = true;
      progress.done();
    }

    _this.getParameterByName = function (name) {
      name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
      var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec($window.location.href);
      return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
    };
  }

  return controller;

});