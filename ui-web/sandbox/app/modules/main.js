require.config({
  paths: {
    facebook: '//connect.facebook.net/en_US/sdk',
    async: '../../bower_components/requirejs-plugins/src/async',
    angular: '../../bower_components/angular/angular',
    bootstrap: '../../bower_components/bootstrap/dist/js/bootstrap',
    jquery: '../../bower_components/jquery/dist/jquery',
    'angular-bootstrap': '../../bower_components/angular-bootstrap/ui-bootstrap-tpls',
    'angular-animate': '../../bower_components/angular-animate/angular-animate',
    'angular-ui-validate': '../../bower_components/angular-ui-validate/dist/validate',
    'angular-route': '../../bower_components/angular-route/angular-route',
    'angular-cookies': '../../bower_components/angular-cookies/angular-cookies',
    'angular-google-maps': '../../bower_components/angular-google-maps/dist/angular-google-maps',
    'ng-password-strength': '../../bower_components/ng-password-strength/dist/scripts/ng-password-strength',
    lodash: '../../bower_components/lodash/lodash',
    'angular-recaptcha': '../../bower_components/angular-recaptcha/release/angular-recaptcha',
    'font-awesome': '../../bower_components/font-awesome/fonts/*',
    nprogress: '../../bower_components/nprogress/nprogress',
    'angular-rangeslider': '../../bower_components/angular-rangeslider/angular.rangeSlider',
    'angular-ui-slider': '../../bower_components/angular-ui-slider/src/slider',
    'jquery-ui': '../../bower_components/jquery-ui/ui/jquery-ui',
    'angularjs-slider': '../../bower_components/angularjs-slider/rzslider',
    'angular-slimscroll': '../../bower_components/angular-slimscroll/angular-slimscroll',
    slimScroll: '../../bower_components/slimScroll/jquery.slimscroll.min',
    'jquery-slimscroll': '../../bower_components/jquery-slimscroll/jquery.slimscroll.min'
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
    'angular-recaptcha': {
      deps: [
        'angular',
        'recaptcha.loader'
      ]
    },
    'angular-google-maps': {
      deps: [
        'angular',
        'lodash'
      ]
    },
    'angular-rangeslider': {
      deps: [
        'angular',
        'jquery'
      ]
    },
    'angular-slimscroll': {
      deps: [
        'angular',
        'jquery',
        'jquery-slimscroll'
      ]
    },
    facebook: {
      exports: 'FB'
    },
    nprogress: {
      exports: 'NProgress'
    }
  },
  packages: [

  ],
  waitSeconds: 60
});
require([
  'progressbar',
  'ie10-fix',
  'map/map.module',
  //'bootstrap',
  'angular',
  'angular-bootstrap',
  'angular-cookies',
  'angular-slimscroll',
  'navigation/navigation.module', 'home/home.module'
], function (progress) {
  'use strict';

  progress.setScriptCount(2);
  progress.scriptLoad();
  console.log("Loading other dependencies...");
  angular.bootstrap(document, ['ui.slimscroll', 'ui.bootstrap','ngAnimate', 'mapModule', 'navigationModule', 'homeModule']);
});
