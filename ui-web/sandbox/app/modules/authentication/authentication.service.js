define([], function () {
  'use strict';

  function service(userRestClientService, $cookies) {
    var _this = this;
    _this.authenticated = false;

    _this.authenticate = function (email, mode, callback) {
      userRestClientService.authenticate({
        email: email,
        provider: mode.provider,
        token: mode.token,
        data: mode.data
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
      alert("storing " + token);
      $cookies.put('sessionToken', token, {secure: true});
    }
  }


  service.$inject = ['userRestClientService', '$cookies'];

  return service;
});