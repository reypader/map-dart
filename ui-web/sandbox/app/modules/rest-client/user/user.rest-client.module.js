define(['rest-client/user/user.rest-client.config', 'rest-client/user/user.rest-client.service', 'angular'], function (config, service) {

  var app = angular.module('userRestClient', []);
  app.config(config);
  app.service('userRestClientService', service);
});