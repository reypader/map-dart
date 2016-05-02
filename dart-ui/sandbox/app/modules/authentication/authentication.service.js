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
      $cookies.put('provider', response.identityProvider, {secure: true});
      $cookies.put('user', response.user, {secure: true});
      $cookies.put('sessionToken', response.token, {secure: true});
      $cookies.put('displayName', response.displayName, {secure: true});
    }

    _this.clearSession = function () {
      $cookies.remove('provider', {secure: true});
      $cookies.remove('user', {secure: true});
      $cookies.remove('sessionToken', {secure: true});
      $cookies.remove('displayName', {secure: true});

    }
  }

  return service;
});