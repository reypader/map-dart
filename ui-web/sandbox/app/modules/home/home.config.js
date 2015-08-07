define([], function () {
  'use strict';

  function config($routeProvider) {
    $routeProvider.when('/home', {
      templateUrl: 'home.partial.html',
      controller: 'HomeCtrl',
      controllerAs: 'homeCtrl'
    });
  }

  config.$inject = ['$routeProvider'];

  return config;
});