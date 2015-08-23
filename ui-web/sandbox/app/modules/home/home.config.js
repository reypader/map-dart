define([], function () {
  'use strict';

  function config($routeProvider) {
    $routeProvider.when('/home', {
      templateUrl: 'modules/home/home.partial.html',
      controller: 'HomeCtrl',
      controllerAs: 'homeCtrl'
    });
  }

  config.$inject = ['$routeProvider'];

  return config;
});