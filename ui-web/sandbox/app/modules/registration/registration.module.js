define([
  'registration/registration.config',
  'registration/registration.controller',
  'registration/newEmail.registration.directive',
  'registration/strictEmail.registration.directive',
  'angular',
  'angular-bootstrap'], function (config, controller, newEmailDirective, strictEmailDirective) {

  var registration = angular.module('registrationModule', ['restClient', 'ui.bootstrap']);
  registration.config(config);
  registration.controller('RegistrationController', controller);
  registration.directive('newEmail', newEmailDirective);
  registration.directive('strictEmail', strictEmailDirective);

});