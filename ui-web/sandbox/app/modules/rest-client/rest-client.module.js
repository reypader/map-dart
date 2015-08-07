define(['rest-client/rest-client.service', 'angular'], function (service) {

  var app = angular.module('restClient', []);
  app.factory('restClientService', service);
  app.constant('restClientConfig', {
    userModuleURL: ''
  });
});