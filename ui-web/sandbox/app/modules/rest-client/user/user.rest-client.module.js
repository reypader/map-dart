define([
  './user.rest-client.config',
  './user.rest-client.service',
  'angular'
], function (config, service) {
  'use strict';

  angular.module('userRestClient', [])
    .config(config)
    .service('userRestClientService', service);
});