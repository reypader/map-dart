define([
  'progressbar',
  'gapi.loader'
], function (progress, gapi) {
  'use strict';

  config.$inject = [];

  function config() {
    console.log("Loading Google auth2 scripts...");
    gapi.load('auth2', function () {
      progress.scriptLoad();
      console.log("Loading GooglePlus scripts...");
      gapi.client.load('plus', 'v1').then(function () {
        progress.scriptLoad();
        gapi.auth2.init({
          client_id: '218178306686-83pqm9g2a7s214hp03kv51lcgm5nniid.apps.googleusercontent.com',
          fetch_basic_profile: true
        });
      });
    });
  }

  return config;
})