define(['navigation/navigation.config', 'navigation/navigation.controller', 'angular', 'angular-route', 'angular-cookies'], function (config, controller) {
  'use strict';

  var app = angular.module('navigationModule', ['ngRoute', 'ngCookies']);
  app.config(config);
  app.controller('NavCtrl', controller);
  app.run(function ($cookies) {
    alert($cookies.get('sessionToken'));
  })
});