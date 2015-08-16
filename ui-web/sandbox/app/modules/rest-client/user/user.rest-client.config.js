define(['gapi.loader','progressbar'], function (gapi,progress) {
  'use strict';

  function config() {
    var ROOT = 'http://localhost:8080/_ah/api';
    gapi.client.load('user', 'v1', function () {
      progress.scriptLoad();
      console.log("Loading Pings User API scripts...");
    }, ROOT);
  }

  config.$inject = [];

  return config;
});