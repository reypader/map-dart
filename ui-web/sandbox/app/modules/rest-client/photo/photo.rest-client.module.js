define([
  './photo.rest-client.config',
  './photo.rest-client.service',
  'angular',
  'angular-cookies'
], function (config, service) {
  'use strict';

  angular.module('photoRestClient', ['ngCookies'])
    .config(config)
    .service('photoRestClientService', service)
    .run(['$http', '$cookies', function ($http, $cookies) {
      var token = $cookies.get('sessionToken');
      if (token) {
        $http.defaults.headers.common.Authorization = $cookies.get('user') + ' ' + $cookies.get('sessionToken');
      }
    }]);
});