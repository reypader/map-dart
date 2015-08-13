define(['authentication/authentication.service', 'angular', 'angular-cookies', 'cryptojs','rest-client/user/user.rest-client.module'], function (service) {
  'use strict';

  var authentication = angular.module('authenticationModule', ['userRestClient', 'ngCookies']);
  authentication.service('authenticationService', service);
});