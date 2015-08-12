define(['authentication/facebook/facebook.service', 'angular', 'authentication/authentication.module','rest-client/rest-client.module'], function (service) {
  'use strict';

  var facebook = angular.module('facebookModule', ['authenticationModule']).config(function () {
    FB.init({
      appId: '1597342647199773',
      version: 'v2.4',
      status: true,
      cookie: false,
      xfbml: false
    });

  });
  facebook.service('facebookService', service);
});
