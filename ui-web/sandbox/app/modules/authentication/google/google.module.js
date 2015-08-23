define([
  './google.config',
  './google.service',
  'angular',
  'authentication/authentication.module'
], function (config, service) {
  'use strict';

  angular
    .module('googleModule', ['authenticationModule'])
    .config(config)
    .service('googleService', service);

});