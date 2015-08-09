define(['gapi.loader'], function (gapi) {
  'use strict';

  function service(authenticationService) {
    var _this = this;

    _this.loginCallback = function (response) {
      var authInstance = gapi.auth2.getAuthInstance();
      var user = authInstance.currentUser.get();
      if (authInstance.isSignedIn.get() && response.id_token && user.hasGrantedScopes("profile email")) {
        var email = user.getBasicProfile().getEmail();
        var mode = {
          provider: 'google',
          token: response.id_token,
          data: 'email=' + email
        };
        authenticationService.authenticate(email, mode, _this.callback);
      }
    };

    _this.gpLogin = function (callback) {
      _this.callback = callback;
      var authInstance = gapi.auth2.getAuthInstance();
      authInstance.signIn().then(function () {
          var user = gapi.auth2.getAuthInstance().currentUser.get();
          _this.loginCallback(user.getAuthResponse());
        }
      );
    }
  }

  service.$inject = ['authenticationService'];

  return service;
});