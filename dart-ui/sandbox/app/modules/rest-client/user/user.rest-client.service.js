define([], function () {
  'use strict';

  service.$inject = [
    '$q',
    '$http'
  ];

  function service($q, $http) {
    var _this = this;

    _this.checkEmail = function (email) {
      var def = $q.defer();
      $http.get('http://user-api.travler.com/api/user/email/check?email=' + email)
        .then(function (response) {
          def.resolve(response.data);
        }, function (response) {
          def.reject(response.data);
        });
      return def.promise;
    };

    _this.registerUser = function (newUser) {
      var def = $q.defer();
      $http.post('http://user-api.travler.com/api/user/register', newUser)
        .then(function (response) {
          def.resolve(response.data);
        }, function (response) {
          def.reject(response.data);
        });
      return def.promise;
    };

    _this.authenticate = function (request) {
      var def = $q.defer();
      http({
          method: 'POST',
          url: 'http://user-api.travler.com/api/user/auth/' + request.provider,
          headers: {'Content-Type': 'application/x-www-form-urlencoded'},
          transformRequest: function(obj) {
              var str = [];
              for(var p in obj)
              str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
              return str.join("&");
          },
          data: request
      }).then(
           function (response) {
             def.resolve(response.data);
           },
           function (response) {
             def.reject(response.data);
           }
      );

      return def.promise;
    };

    _this.validateRecaptcha = function (request) {
      var def = $q.defer();
      $http.post('http://user-api.travler.com/api/user/recaptcha', request).then(function (response) {
        def.resolve(response.data);
      }, function (response) {
        def.reject(response.data);
      });
      return def.promise;
    }
  }

  return service;
})