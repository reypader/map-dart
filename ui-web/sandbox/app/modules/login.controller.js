define(['angular', 'cryptojs', 'authentication/facebook/facebook.module', 'authentication/authentication.module'], function () {
  'use strict';

  var oauth = angular.module('loginModule', ['facebookModule', 'authenticationModule']);

  oauth.controller('LoginController', ['facebookService', 'authenticationService', '$window', '$location', function (facebookService, authenticationService, $window, $location) {
    var _this = this;

    _this.loginFailed = false;

    _this.email = "";
    _this.password = "";

    _this.fbLogin = function () {
      facebookService.fbLogin(_this.redirectToHome);
    }

    _this.basicLogin = function () {
      var hash = CryptoJS.SHA256(_this.password);
      var mode = {
        provider: "self",
        token: hash.toString(CryptoJS.enc.Base64)
      }
      authenticationService.authenticate(_this.email, mode, _this.redirectToHome);
    }

    _this.redirectToHome = function (stat) {
      if (stat) {
        $window.location.href = $location.protocol() + "://" + location.host;
      } else {
        _this.loginFailed = true;
      }
    };
  }
  ])
})
;