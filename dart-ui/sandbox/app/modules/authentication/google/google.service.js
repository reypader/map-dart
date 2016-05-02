define([
  'gapi.loader'
], function (gapi) {
  'use strict';

  service.$inject = [
    'authenticationService'
  ];

  function service(authenticationService) {
    var _this = this;

    _this.loginCallback = function (response) {
      var authInstance = gapi.auth2.getAuthInstance();
      var user = authInstance.currentUser.get();
      if (authInstance.isSignedIn.get() && response.id_token && user.hasGrantedScopes("profile email")) {
        var email = user.getBasicProfile().getEmail();
        var name = user.getBasicProfile().getName();
        var photoUrl = user.getBasicProfile().getImageUrl();
        var mode = {
          provider: 'google',
          token: response.id_token,
          data: {
            id: email,
            name: name,
            photoUrl: photoUrl
          }
        };
        authenticationService.authenticate(email, mode, _this.secondaryCallback, _this.failCallback);
      } else {
        if (_this.failCallback) {
          _this.failCallback();
        }
      }
    };

    _this.gpLogin = function (secondaryCallback, failCallback) {
      _this.secondaryCallback = secondaryCallback;
      _this.failCallback = failCallback;
      var authInstance = gapi.auth2.getAuthInstance();
      authInstance.signIn().then(function () {
        var user = gapi.auth2.getAuthInstance().currentUser.get();
        _this.loginCallback(user.getAuthResponse());
      }, _this.failCallback);
    }
  }

  return service;
});