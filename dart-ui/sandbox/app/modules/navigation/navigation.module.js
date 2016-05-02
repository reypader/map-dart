define([
  './navigation.config',
  './navigation.controller',
  'angular',
  'angular-route',
  'authentication/authentication.module'
], function (config, controller) {
  'use strict';

  var app = angular.module('navigationModule', ['ngRoute', 'authenticationModule']);
  app.config(config);
  app.controller('NavigationController', controller);
});