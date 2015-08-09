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
    cryptojs: '//crypto-js.googlecode.com/svn/tags/3.1.2/build/rollups/sha256',
    angular: '../../bower_components/angular/angular',
    async: '../../bower_components/requirejs-plugins/src/async',
    'angular-route': '../../bower_components/angular-route/angular-route',
    'angular-mocks': '../../node_modules/angular-mocks/angular-mocks',
    'angular-bootstrap': '../../bower_components/angular-bootstrap/ui-bootstrap-tpls',
    'angular-animate': '../../bower_components/angular-animate/angular-animate',
    'angular-cookies': '../../bower_components/angular-cookies/angular-cookies'
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
    }
  },

  baseUrl: '/base/app/modules',

  // dynamically load all test files
  deps: allTestFiles,

  // we have to kickoff jasmine, as it is asynchronous
  callback: window.__karma__.start
});
