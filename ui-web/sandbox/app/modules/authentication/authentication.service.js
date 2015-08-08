define([], function () {
  'use strict';

  function service(restClientService, $cookies) {
    var _this = this;
    _this.authenticated = false;

    _this.authenticate = function (email, mode, callback) {
      restClientService.authenticate({
        email: email,
        provider: mode.provider,
        token: mode.token
      }).then(function (response) {
        if (response.token) {
          _this.setSession(response.token);
          _this.authenticated = true;
          if (callback) {
            callback(true);
          }
        } else {
          if (callback) {
            callback(false);
          }
        }
      });
    }

    _this.setSession = function (token) {
      $cookies.put('sessionToken', token, {secure: true});
    }
  }


  service.$inject = ['restClientService', '$cookies'];

  return service;
});