define([
  'gapi.loader',
  'progressbar'
], function (gapi, progress) {
  'use strict';

  config.$inject = [];

  function config() {
    var ROOT = 'http://localhost:8080/_ah/api';
    console.log("Loading Pings User API scripts...");
    gapi.client.load('user', 'v1', function () {
      progress.scriptLoad();
    }, ROOT);
  }

  return config;
});