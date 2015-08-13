require.config({
  paths: {
    facebook: '//connect.facebook.net/en_US/sdk',
    cryptojs: '//crypto-js.googlecode.com/svn/tags/3.1.2/build/rollups/sha256',
    async: '../../bower_components/requirejs-plugins/src/async',
    angular: '../../bower_components/angular/angular',
    bootstrap: '../../bower_components/bootstrap/dist/js/bootstrap',
    jquery: '../../bower_components/jquery/dist/jquery',
    'angular-bootstrap': '../../bower_components/angular-bootstrap/ui-bootstrap-tpls',
    'angular-animate': '../../bower_components/angular-animate/angular-animate',
    'angular-ui-validate': '../../bower_components/angular-ui-validate/dist/validate',
    'angular-route': '../../bower_components/angular-route/angular-route',
    'angular-cookies': '../../bower_components/angular-cookies/angular-cookies',
    'ng-password-strength': '../../bower_components/ng-password-strength/app/scripts/scripts/ng-password-strength',
    lodash: '../../bower_components/lodash/lodash',
    'angular-recaptcha': '../../bower_components/angular-recaptcha/release/angular-recaptcha',
    nprogress: '../../bower_components/nprogress/nprogress'
  },
  shim: {
    bootstrap: {
      deps: ['jquery']
    },
    angular: {
      exports: 'angular'
    },
    'angular-route': {
      deps: ['angular']
    },
    'angular-animate': {
      deps: ['angular']
    },
    'angular-bootstrap': {
      deps: ['angular', 'angular-animate']
    },
    'angular-ui-validate': {
      deps: ['angular']
    },
    'angular-cookies': {
      deps: ['angular']
    },
    'ng-password-strength': {
      deps: ['angular', 'lodash']
    },
    'angular-recaptcha': {
      deps: ['angular', 'recaptcha.loader']
    },
    facebook: {
      exports: 'FB'
    },
    nprogress: {
      exports: 'NProgress'
    }
  },
  packages: []
});
require([
  'progressbar',
  'gapi.loader',
  'angular',
  'angular-bootstrap',
  'angular-ui-validate',
  'password-strength.directive',
  'anchor-smooth-scroll.directive',
  'login.controller',
  'registration/registration.module'
], function (progress) {
  'use strict';

  progress.increment();
  console.log("Loading other dependencies...");
  angular.bootstrap(document, [
    'ngAnimate',
    'passwordStrengthModule',
    'anchorSmoothScrollModule',
    'ui.bootstrap',
    'ui.validate',
    'registrationModule',
    'loginModule'
  ]);
});