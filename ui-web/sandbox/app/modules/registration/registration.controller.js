define([], function () {
  'use strict';

  function controller(registrationService, $location) {
    this.newUser = {};

    this.registerUser = function registerUser() {
      registrationService.registerUser(this.newUser).then(function () {
        $location.path('/success');
      });
    }
  }

  controller.$inject = ['registrationService', '$location'];

  return controller;
});