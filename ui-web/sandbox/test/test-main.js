'use strict';

var allTestFiles = [];
var TEST_REGEXP = /(spec|test)\.js$/i;

// Get a list of all the test files to include
Object.keys(window.__karma__.files).forEach(function (file) {
  if (TEST_REGEXP.test(file)) {
    // Normalize paths to RequireJS module names.
    // If you require sub-dependencies of test files to be loaded as-is (requiring file extension)
    // then do not normalize the paths
    //var normalizedTestModule = file.replace(/^\/base\/|\.js$/g, '');
    allTestFiles.push(file);
  }
});

require.config({

  paths: {
    facebook: '//connect.facebook.net/en_US/sdk',
    angular: '../../bower_components/angular/angular',
    async: '../../bower_components/requirejs-plugins/src/async',
    nprogress: '../../bower_components/nprogress/nprogress',
    'angular-route': '../../bower_components/angular-route/angular-route',
    'angular-mocks': '../../node_modules/angular-mocks/angular-mocks',
    'angular-bootstrap': '../../bower_components/angular-bootstrap/ui-bootstrap-tpls',
    'angular-animate': '../../bower_components/angular-animate/angular-animate',
    'angular-cookies': '../../bower_components/angular-cookies/angular-cookies',
    'angular-recaptcha': '../../bower_components/angular-recaptcha/release/angular-recaptcha',
    'cryptojs.core': "../../bower_components/cryptojslib/components/core",
    'cryptojs.base64': "../../bower_components/cryptojslib/components/enc-base64",
    'cryptojs.sha256': "../../bower_components/cryptojslib/components/sha256",
    'cryptojs.md5': "../../bower_components/cryptojslib/components/md5"
  },
  shim: {
    angular: {
      exports: 'angular'
    },
    'angular-mocks': {
      deps: ['angular']
    },
    'angular-route': {
      deps: ['angular']
    },
    'angular-cookies': {
      deps: ['angular']
    },
    'angular-bootstrap': {
      deps: ['angular', 'angular-animate']
    },
    'angular-recaptcha': {
      deps: [
        'angular',
        'recaptcha.loader'
      ]
    },
    facebook: {
      exports: 'FB'
    },
    nprogress: {
      exports: 'NProgress'
    },
    'cryptojs.core': {
      exports: "CryptoJS"
    },
    'cryptojs.sha256': {
      deps: ['cryptojs.core'],
      exports: "CryptoJS.SHA256"
    },
    'cryptojs.base64': {
      deps: ['cryptojs.core'],
      exports: "CryptoJS.enc.Base64"
    },
    'cryptojs.md5': {
      deps: ['cryptojs.core'],
      exports: "CryptoJS.MD5"
    }
  },

  baseUrl: '/base/app/modules',

  // dynamically load all test files
  deps: allTestFiles,

  // we have to kickoff jasmine, as it is asynchronous
  callback: window.__karma__.start
});
