define([], function () {
  'use strict';

  function config($routeProvider) {
    $routeProvider.when('/', {
      templateUrl: 'modules/registration/main.registration.partial.html',
      controller: 'RegistrationController',
      controllerAs: 'vm'
    }).otherwise({redirectTo: '/'});

  }

  config.$inject = ['$routeProvider'];

  return config;
});