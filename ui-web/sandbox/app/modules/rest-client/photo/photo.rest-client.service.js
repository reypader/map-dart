define([], function () {
  'use strict';

  service.$inject = [
    '$q',
    '$http',
    '$cookies'
  ];

  function service($q, $http, $cookies) {
    var _this = this;

    _this.getUploadURL = function (md5, type) {
      var def = $q.defer();
      $http.get('http://photo-api.travler.com/api/photo/upload/create?md5=' + md5 + '&type=' + encodeURIComponent(type))
        .then(function (response) {
          def.resolve(response.data);
        }, function (response) {
          def.reject(response.data);
        });
      return def.promise;
    };
  }

  return service;
})