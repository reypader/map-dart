define(["gapi.loader"], function (gapi) {
  var service = function service($q) {
    var _this = this;

    _this.checkEmail = function (email) {
      var def = $q.defer();
      gapi.client.user.email.check({email: email}).then(function (response) {
        def.resolve(response.result);
      });
      return def.promise;
    };

    _this.registerUser = function (newUser) {
      var def = $q.defer();
      gapi.client.user.register(newUser).then(function (response) {
        def.resolve(response.result);
      });
      return def.promise;
    };

    _this.authenticate = function (request) {
      var def = $q.defer();
      if (request.resource.provider === 'self') {
        gapi.client.user.auth.basic(request).then(function (response) {
          def.resolve(response.result);
        });
      } else if (request.resource.provider === 'facebook') {
        gapi.client.user.auth.facebook(request).then(function (response) {
          def.resolve(response.result);
        });
      } else if (request.resource.provider === 'google') {
        gapi.client.user.auth.google(request).then(function (response) {
          def.resolve(response.result);
        });
      }
      return def.promise;
    };

    _this.validateRecaptcha = function (recaptchaResult) {
      var p = $q.defer();
      setTimeout(function () {
        if (false) {
          alert("captcha for: '" + JSON.stringify(recaptchaResult) + "' failed");
          p.resolve({userIsHuman: false});
        } else {
          alert("captcha for: '" + JSON.stringify(recaptchaResult) + "' successful");
          p.resolve({userIsHuman: true});
        }
      }, 1000);
      return p.promise;
    }
  }

  service.$inject = ['$q'];

  return service;
})