define(['gapi.loader'], function (gapi) {
  'use strict';

  function config() {
    var ROOT = 'http://localhost:8080/_ah/api';
    gapi.client.load('user', 'v1', function () {
    }, ROOT);
  }

  config.$inject = [];

  return config;
});