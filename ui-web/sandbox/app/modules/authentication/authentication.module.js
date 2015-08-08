define(['authentication/authentication.service', 'angular', 'angular-cookies'], function (service) {
  'use strict';

  var authentication = angular.module('authenticationModule', ['restClient', 'ngCookies']);
  authentication.service('authenticationService', service);
});