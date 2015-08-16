define([
  'progressbar',
  'gapi.loader',
  'authentication/google/google.service',
  'angular',
  'authentication/authentication.module'
], function (progress,gapi, service) {
  'use strict';

  var google = angular.module('googleModule', ['authenticationModule']);
  google.config(function () {
    console.log("Loading Google auth2 scripts...");
    gapi.load('auth2', function () {
      progress.scriptLoad();
      console.log("Loading GooglePlus scripts...");
      gapi.client.load('plus', 'v1').then(function () {
        progress.scriptLoad();
        gapi.auth2.init({
          client_id: '218178306686-83pqm9g2a7s214hp03kv51lcgm5nniid.apps.googleusercontent.com',
          fetch_basic_profile: true
        });
      });
    });
  });
  google.service('googleService', service);
});