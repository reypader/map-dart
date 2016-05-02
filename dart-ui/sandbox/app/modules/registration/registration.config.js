define(['require'], function (require) {
  'use strict';

  config.$inject = [
    '$routeProvider'
  ];

  function config($routeProvider) {
    $routeProvider.when('/', {
      templateUrl: require.toUrl('./registration.partial.html'),
      controller: 'RegistrationController',
      controllerAs: 'vm'
    }).otherwise({redirectTo: '/'});

  }

  return config;
});