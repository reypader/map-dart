define(['angular', 'authentication/facebook/facebook.module'], function () {
  'use strict';

  var oauth = angular.module('oauthModule', ['facebookModule']);
  oauth.controller('OauthController', ['facebookService', '$window', function (facebookService, $window) {
    var _this = this;

    _this.fbLogin = function () {
      facebookService.fbLogin(function (stat) {
        if (stat) {
          $window.location.href = "https://" + $window.location.host;
        }
      });
    }

  }])
});