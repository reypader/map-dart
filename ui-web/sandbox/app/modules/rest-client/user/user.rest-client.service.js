define(["gapi.loader"], function (gapi) {
  'use strict';

  var service = function service($q) {
    var _this = this;

    _this.checkEmail = function (email) {
      var def = $q.defer();
      gapi.client.user.email.check({email: email}).then(function (response) {
        def.resolve(response.result);
      }, function (response) {
        def.reject(response.result);
      });
      return def.promise;
    };

    _this.registerUser = function (newUser) {
      var def = $q.defer();
      gapi.client.user.register(newUser).then(function (response) {
        def.resolve(response.result);
      }, function (response) {
        def.reject(response.result);
      });
      return def.promise;
    };

    _this.authenticate = function (request) {
      var cloudEndpointRequest = {
        additional_data: request.data,
        resource: {
          email: request.email,
          provider: request.provider,
          token: request.token,
        }
      };
      var def = $q.defer();
      if (request.provider === 'self') {
        gapi.client.user.auth.basic(cloudEndpointRequest).then(function (response) {
          def.resolve(response.result);
        }, function (response) {
          def.reject(response.result);
        });
      } else if (request.provider === 'facebook') {
        gapi.client.user.auth.facebook(cloudEndpointRequest).then(function (response) {
          def.resolve(response.result);
        }, function (response) {
          def.reject(response.result);
        });
      } else if (request.provider === 'google') {
        gapi.client.user.auth.google(cloudEndpointRequest).then(function (response) {
          def.resolve(response.result);
        }, function (response) {
          def.reject(response.result);
        });
      }
      return def.promise;
    };

    _this.validateRecaptcha = function (recaptchaResult) {
      var def = $q.defer();
      gapi.client.user.recaptcha({recaptcha_result: recaptchaResult}).then(function (response) {
        def.resolve(response.result);
      }, function (response) {
        def.reject(response.result);
      });
      return def.promise;
    }
  }

  service.$inject = ['$q'];

  return service;
})