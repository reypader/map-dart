define([], function () {
  var service = function service($q) {
    var _this = this;

    _this.checkEmail = function (email) {
      var bool = email === "used@email.com"

      var dummyResponse = {
        emailUsed: bool
      };
      var p = $q.defer();
      setTimeout(function () {
        alert("checkEmail for: '" + email + "' responding with: " + JSON.stringify(dummyResponse));
        p.resolve(dummyResponse);
      }, 1000);
      return p.promise;
    };

    _this.registerUser = function (newUser) {
      var p = $q.defer();
      setTimeout(function () {
        alert("registerUser for: '" + JSON.stringify(newUser) + "' successful");
        p.resolve();
      }, 1000);
      return p.promise;
    };

    _this.authenticate = function (request) {
      var p = $q.defer();
      setTimeout(function () {
        if (request.email == 'bad@email.com') {
          alert("authentication for: '" + JSON.stringify(request) + "' failed");
          p.resolve({});
        } else {
          alert("authentication for: '" + JSON.stringify(request) + "' successful");
          p.resolve({token: "token"});
        }
      }, 1000);
      return p.promise;
    };

    _this.validateRecaptcha = function (recaptchaResult) {
      var p = $q.defer();
      setTimeout(function () {
        if (Math.random() > 0.2) {
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