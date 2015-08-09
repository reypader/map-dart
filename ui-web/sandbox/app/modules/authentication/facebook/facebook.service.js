define([], function () {
  'use strict';

  function service(authenticationService) {
    var _this = this;

    _this.loginCallback = function (response) {
      if (response.authResponse) {
        var _authResponse = response.authResponse;
        FB.api('/me', {fields: 'email'}, function (response) {
          var mode = {
            provider: 'facebook',
            token: _authResponse.access_token,
            data: 'id=' + _authResponse.userID
          };
          authenticationService.authenticate(response.email, mode, _this.callback);
        });
      }
    };

    _this.fbLogin = function (callback) {
      _this.callback = callback;
      FB.login(_this.loginCallback, {
        scope: 'email'
      });
    }
  }

  service.$inject = ['authenticationService'];

  return service;
});