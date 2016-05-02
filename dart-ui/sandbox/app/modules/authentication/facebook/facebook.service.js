define([
  'facebook'
], function (FB) {
  'use strict';

  service.$inject = [
    'authenticationService'
  ];

  function service(authenticationService) {
    var _this = this;

    _this.loginCallback = function (response) {
      if (response.authResponse) {
        var _authResponse = response.authResponse;
        FB.api('/me', {fields: 'email,name,picture'}, function (response) {
          var mode = {
            provider: 'facebook',
            token: _authResponse.accessToken,
            data: {
              id: _authResponse.userID,
              name: response.name,
              photoUrl: response.picture.data.url
            }
          };
          authenticationService.authenticate(response.email, mode, _this.secondaryCallback, _this.failCallback);
        });
      } else {
        if (_this.failCallback) {
          _this.failCallback();
        }
      }
    };

    _this.fbLogin = function (secondaryCallback, failCallback) {
      _this.secondaryCallback = secondaryCallback;
      _this.failCallback = failCallback;
      FB.login(_this.loginCallback, {
        scope: 'email'
      });
    }
  }

  return service;
});