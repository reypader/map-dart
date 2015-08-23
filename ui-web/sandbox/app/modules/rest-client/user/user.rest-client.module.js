define([
  'rest-client/user/user.rest-client.config',
  'rest-client/user/user.rest-client.service',
  'angular'
], function (config, service) {
  'use strict';

  angular.module('userRestClient', [])
    .config(config)
    .service('userRestClientService', service);
});