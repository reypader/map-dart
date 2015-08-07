define(['authentication/authentication.config', 'authentication/authentication.controller', 'angular'], function (config, controller) {
  var app = angular.module('authenticationModule', ['restClient']);
  app.controller('AuthenticationController', controller);
});