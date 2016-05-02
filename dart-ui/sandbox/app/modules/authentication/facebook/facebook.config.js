define([
  'facebook'
], function (FB) {
  'use strict';

  config.$inject = [];

  function config() {
    FB.init({
      appId: '1597342647199773',
      version: 'v2.4',
      status: true,
      cookie: false,
      xfbml: false
    });
  }

  return config;
});