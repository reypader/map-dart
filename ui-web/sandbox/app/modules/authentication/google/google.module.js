define(['gapi.loader', 'authentication/google/google.service', 'angular', 'authentication/authentication.module'], function (gapi, service) {
  'use strict';

  var google = angular.module('googleModule', ['authenticationModule']);
  google.config(function () {
    gapi.load('auth2', function () {
      gapi.client.load('plus', 'v1').then(function () {
        gapi.auth2.init({
          client_id: '218178306686-83pqm9g2a7s214hp03kv51lcgm5nniid.apps.googleusercontent.com',
          fetch_basic_profile: true
        });
      });
    });
  });
  google.service('googleService', service);
});