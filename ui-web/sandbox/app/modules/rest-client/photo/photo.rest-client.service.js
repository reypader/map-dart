define([], function () {
  'use strict';

  service.$inject = [
    '$q',
    '$http',
    '$cookies'
  ];

  function service($q, $http, $cookies) {
    var _this = this;
    _this.executors = 3;

    this.upload = function upload(files) {
      var index = 0;
      var queue = [];

      return execute();
      ///

      function execute() {
        for (var i = 0; i < files.length; i++) {
          queue.push({
            servingUrl: URL.createObjectURL(files[i]),
            file: files[i]
          });
        }
        for (var i = 0; i < _this.executors; i++) {
          doUpload(nextUpload());
        }
        return queue;
      }

      function nextUpload() {
        return queue[index++];
      }

      function doUpload(file) {
        if (file.file) {
          $http.get('http://photo-api.travler.com/api/photo/upload/gcs/create').then(function (response) {
            file.file.active = true;
            $http({
              method: 'POST',
              url: response.data.uploadURL,
              headers: {
                'Content-Type': undefined
              },
              data: {
                file: file.file
              },
              transformRequest: function (data) {
                var fd = new FormData();
                angular.forEach(data, function (value, key) {
                  fd.append(key, value);
                });
                return fd;
              }
            }).then(function (response) {
              file.file.active = false;
              file.file.done = true;
              file.servingUrl = response.servingURL;
              doUpload(nextUpload);
            });
          });
        }
      }
    }

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
});