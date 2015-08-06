require.config({
  paths: {
    facebook: '//connect.facebook.net/en_US/sdk',
    angular: '../../bower_components/angular/angular',
    'angular-bootstrap': '../../bower_components/angular-bootstrap/ui-bootstrap-tpls'
  },
  shim: {
    angular: {
      exports: 'angular'
    },
    facebook: {
      exports: 'FB'
    }
  },
  packages: []
});
require(['facebook'], function () {
  'use strict';
  angular.module('welcome', ['ui.bootstrap']);
  angular.bootstrap(document, ['welcome']);
});