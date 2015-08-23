define([
  './login.controller',
  'angular',
  'authentication/facebook/facebook.module',
  'authentication/google/google.module',
  'authentication/authentication.module'
], function (controller) {
  'use strict';

  angular.module('loginModule', ['facebookModule', 'googleModule', 'authenticationModule'])
    .controller('LoginController', controller);
});