require.config({
  paths: {
    facebook: '//connect.facebook.net/en_US/sdk',
    cryptojs: '//crypto-js.googlecode.com/svn/tags/3.1.2/build/rollups/sha256',
    angular: '../../bower_components/angular/angular',
    'angular-route': '../../bower_components/angular-route/angular-route',
    bootstrap: '../../bower_components/bootstrap/dist/js/bootstrap',
    jquery: '../../bower_components/jquery/dist/jquery',
    'angular-bootstrap': '../../bower_components/angular-bootstrap/ui-bootstrap-tpls',
    'font-awesome': '../../bower_components/font-awesome/fonts/*',
    'angular-animate': '../../bower_components/angular-animate/angular-animate',
    'angular-ui-validate': '../../bower_components/angular-ui-validate/dist/validate',
    'ng-password-strength': '../../bower_components/ng-password-strength/dist/scripts/ng-password-strength',
    lodash: '../../bower_components/lodash/lodash',
    'angular-cookies': '../../bower_components/angular-cookies/angular-cookies'
  },
  shim: {
    bootstrap: {
      deps: [
        'jquery'
      ]
    },
    angular: {
      exports: 'angular'
    },
    'angular-route': {
      deps: [
        'angular'
      ]
    },
    'angular-animate': {
      deps: [
        'angular'
      ]
    },
    'angular-bootstrap': {
      deps: [
        'angular',
        'angular-animate'
      ]
    },
    'angular-cookies': {
      deps: [
        'angular'
      ]
    },
    facebook: {
      exports: 'FB'
    }
  },
  packages: [

  ]
});
require([
  'angular',
  'angular-cookies',
  'navigation/navigation.module', 'home/home.module'], function () {
  'use strict';

  angular.bootstrap(document, ['navigationModule', 'homeModule']);
});