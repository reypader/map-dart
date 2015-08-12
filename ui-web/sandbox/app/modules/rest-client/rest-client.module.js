define(['rest-client/rest-client.config', 'rest-client/rest-client.service', 'angular'], function (config, service) {

  var app = angular.module('restClient', []);
  app.config(config);
  app.service('restClientService', service);
});