require.config({
  paths: {
    facebook: '//connect.facebook.net/en_US/sdk',
    angular: '../../bower_components/angular/angular',
    bootstrap: '../../bower_components/bootstrap/dist/js/bootstrap',
    jquery: '../../bower_components/jquery/dist/jquery',
    'angular-bootstrap': '../../bower_components/angular-bootstrap/ui-bootstrap-tpls',
    'angular-animate': '../../bower_components/angular-animate/angular-animate',
    'angular-ui-validate': '../../bower_components/angular-ui-validate/dist/validate',
    'angular-route': '../../bower_components/angular-route/angular-route',
    'angular-cookies': '../../bower_components/angular-cookies/angular-cookies',
    'ng-password-strength': '../../bower_components/ng-password-strength/app/scripts/scripts/ng-password-strength',
    lodash: '../../bower_components/lodash/lodash'
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
    facebook: {
      exports: 'FB'
    }
  },
  packages: []
});
require([
  'angular',
  'angular-bootstrap',
  'angular-ui-validate',
  'password-strength.directive',
  'anchor-smooth-scroll.directive',
  'facebook',
  'oauth.controller',
  'authentication/authentication.module',
  'authentication/facebook/facebook.module',
  'rest-client/rest-client.module',
  'registration/registration.module'
], function () {
  'use strict';

  angular.bootstrap(document, [
    'ngAnimate',
    'passwordStrengthModule',
    'anchorSmoothScrollModule',
    'authenticationModule',
    'facebookModule',
    'oauthModule',
    'ui.bootstrap',
    'ui.validate', 'restClient',
    'registrationModule'
  ]);
});