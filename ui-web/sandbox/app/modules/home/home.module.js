define(['home/home.config', 'home/home.controller', 'angular', 'angular-route'], function (config, controller) {
  'use strict';

  var app = angular.module('homeModule', ['ngRoute']);
  app.config(config);
  app.controller('HomeCtrl', controller);
});