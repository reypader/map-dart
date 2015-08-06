define(['navigation/navigation.config', 'navigation/navigation.controller','angular','angular-route'], function (config, controller) {
  'use strict';

  var app = angular.module('navigationModule', ['ngRoute']);
  app.config(config);
  app.controller('NavCtrl', controller);
});