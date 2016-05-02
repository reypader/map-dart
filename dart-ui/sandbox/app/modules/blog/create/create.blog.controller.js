define([], function () {
  'use strict';

  controller.$inject = ['location', '$http'];

  function controller(location, $http) {
    var _this = this;
    _this.location = location;
    _this.files = [];

    function uploadUrlGenerator() {
      $http.get('http://photo-api.travler.com/api/photo/upload/gcs/create').then(function (response) {
        return response.data.uploadURL;
      });
    }

    function uploadFileDone(file, xhr) {
      file.servingUrl = JSON.parse(xhr.responseText).servingUrl;
    }

    function uploadHeaders() {
      return {
        'Authorization': $cookies.get('user') + ' ' + $cookies.get('sessionToken')
      };
    }

  }

  return controller;

});