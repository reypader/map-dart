define([
  'progressbar'
], function (progress) {
  'use strict';

  service.$inject = [
    'userRestClientService',
    '$cookies'
  ];

  function service(userRestClientService, $cookies) {
    var _this = this;

    _this.authenticate = function (email, mode, callback, failCallback) {
      progress.increment();
      userRestClientService.authenticate({
        email: email,
        provider: mode.provider,
        token: mode.token,
        data: mode.data
      }).then(function (response) {
        progress.done();
        if (response.token) {
          _this.setSession(response);
          if (callback) {
            callback(true);
          }
        } else {
          if (callback) {
            callback(false);
          }
        }
      }, failCallback);
    }

    _this.setSession = function (response) {
      console.log("store: " + response.identityProvider);
      $cookies.put('provider', response.identityProvider, {secure: false});
      console.log("store: " + response.user);
      $cookies.put('user', response.user, {secure: false});
      console.log("store: " + response.token);
      $cookies.put('sessionToken', response.token, {secure: false});
    }

    _this.clearSession = function () {
      $cookies.remove('provider', {secure: false});
      $cookies.remove('user', {secure: false});
      $cookies.remove('sessionToken', {secure: false});
    }
  }

  return service;
});