define(['angular', 'authentication/facebook/facebook.module'], function () {
  'use strict';

  var oauth = angular.module('oauthModule', ['facebookModule']);
  oauth.controller('LoginController', ['facebookService', '$window', '$location', function (facebookService, $window, $location) {
    var _this = this;

    _this.fbLogin = function () {
      facebookService.fbLogin(_this.redirectToHome);
    }

    _this.redirectToHome = function (stat) {
      if (stat) {
        $window.location.href = $location.protocol + "://" + $location.host;
      }
    };
  }])
});