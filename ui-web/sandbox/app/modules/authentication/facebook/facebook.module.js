define([
  'progressbar',
  'authentication/facebook/facebook.service',
  'facebook',
  'angular',
  'authentication/authentication.module'
], function (progress, service) {
  'use strict';

  var facebook = angular.module('facebookModule', ['authenticationModule']).config(function () {
    FB.init({
      appId: '1597342647199773',
      version: 'v2.4',
      status: true,
      cookie: false,
      xfbml: false
    });
    progress.increment();
    console.log("Loading Facebook scripts...");
  });
  facebook.service('facebookService', service);
});
