define(['angular', 'angular-route', 'home/home_config', 'home/home_controller'], function (angular, angularRoute, config, controller) {
  'use strict';

  var app = angular.module('homeModule', ['ngRoute']);
  app.config(config);
  app.controller('HomeCtrl', controller);
});