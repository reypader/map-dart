define([], function () {
  'use strict';

  service.$inject = [
    '$q',
    '$http'
  ];

  function service($q, $http) {
    var _this = this;

    this.upload = function upload(queue) {
      var index = 0;

      doUpload(nextUpload());

      function nextUpload() {
        return queue[index++];
      }

      function doUpload(file) {
        if (file) {
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
              file.servingUrl = response.data.servingUrl;
              doUpload(nextUpload());
            }, function (error) {
              console.log(JSON.stringify(error));
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