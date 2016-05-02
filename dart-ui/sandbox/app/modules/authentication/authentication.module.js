define([
  './authentication.service',
  'angular',
  'angular-cookies',
  'rest-client/user/user.rest-client.module'
], function (service) {
  'use strict';

  angular
    .module('authenticationModule', ['userRestClient', 'ngCookies'])
    .service('authenticationService', service);

});