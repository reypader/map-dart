define([], function () {
  'use strict';

  function config($routeProvider) {
    $routeProvider.when('/', {
      templateUrl: 'modules/home/home.partial.html',
      controller: 'HomeCtrl',
      controllerAs: 'homeCtrl'
    });
  }

  config.$inject = ['$routeProvider'];

  return config;
});