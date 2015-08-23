define([
  './facebook.config',
  './facebook.service',
  'angular',
  'authentication/authentication.module'
], function (config, service) {
  'use strict';

  angular
    .module('facebookModule', ['authenticationModule']).config(config)
    .service('facebookService', service);

});
