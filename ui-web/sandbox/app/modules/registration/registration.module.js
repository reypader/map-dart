define([
  'registration/registration.config',
  'registration/registration.controller',
  'registration/newEmail.registration.directive',
  'angular'], function (config, controller, directive) {

  var registration = angular.module('registrationModule', ['restClient']);
  registration.config(config);
  registration.controller('RegistrationController', controller);
  registration.directive('newEmail', directive);

});