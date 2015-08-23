define([], function () {
  'use strict';

  config.$inject = ['uiGmapGoogleMapApiProvider'];

  function config(uiGmapGoogleMapApiProvider) {
    uiGmapGoogleMapApiProvider.configure({
      key: 'AIzaSyCExJu9N1gEFH7DxckuIWOOofcqf5Ty03g',
      v: '3.17'
      //libraries: 'weather,geometry,visualization'
    });
  }

  return config;
});