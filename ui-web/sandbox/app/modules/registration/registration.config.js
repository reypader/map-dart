define([], function () {
  'use strict';

  config.$inject = [
    '$routeProvider'
  ];

  function config($routeProvider) {
    $routeProvider.when('/', {
      templateUrl: 'modules/registration/main.registration.partial.html',
      controller: 'RegistrationController',
      controllerAs: 'vm'
    }).otherwise({redirectTo: '/'});

  }

  return config;
});