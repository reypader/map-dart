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
    'jquery-slimscroll': '../../bower_components/jquery-slimscroll/jquery.slimscroll.min',
    'cryptojs.core': '../../bower_components/cryptojslib/components/core',
    'cryptojs.md5': '../../bower_components/cryptojslib/components/md5',
    'cryptojs.base64': '../../bower_components/cryptojslib/components/enc-base64',
    'cryptojs.sha256': '../../bower_components/cryptojslib/components/sha256',
    'cryptojs.lib': '../../bower_components/cryptojslib/components/lib-typedarrays-min',
    'angular-ui-uploader': '../../bower_components/angular-ui-uploader/dist/uploader'
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
        'jquery-slimscroll'
      ]
    },
    'angular-ui-uploader': {
      deps: [
        'angular'
      ]
    },
    'jquery-slimscroll': {
      deps: [
        'jquery'
      ]
    },
    facebook: {
      exports: 'FB'
    },
    nprogress: {
      exports: 'NProgress'
    },
    'cryptojs.core': {
      exports: 'CryptoJS'
    },
    'cryptojs.md5': {
      deps: [
        'cryptojs.core'
      ],
      exports: 'CryptoJS.MD5'
    },
    'cryptojs.lib': {
      deps: [
        'cryptojs.core'
      ],
      exports: 'CryptoJS.lib'
    },
    'cryptojs.sha256': {
      deps: [
        'cryptojs.core'
      ],
      exports: 'CryptoJS.SHA256'
    },
    'cryptojs.base64': {
      deps: [
        'cryptojs.core'
      ],
      exports: 'CryptoJS.enc.Base64'
    }
  },
  packages: [

  ],
  waitSeconds: 60
});
require([
  'progressbar',
  'ie10-fix',
  'file-read.directive',
  'map/map.module',
  'authentication/authentication.module',
  'profile/profile.module',
  'blog/blog.module',
  //'bootstrap',
  'angular',
  'angular-bootstrap',
  'angular-cookies',
  'angular-slimscroll',
  'navigation/navigation.module',
  'home/home.module'
], function (progress) {
  'use strict';

  progress.setScriptCount(2);
  progress.scriptLoad();
  console.log("Loading other dependencies...");
  angular.bootstrap(document, [
    'ui.slimscroll',
    'ui.bootstrap',
    'ngAnimate',
    'fileReadModule',
    'profileModule',
    'mapModule',
    'blogModule',
    'navigationModule',
    'homeModule',
    'authenticationModule']);
});
