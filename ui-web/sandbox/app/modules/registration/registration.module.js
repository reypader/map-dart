define([
  'registration/registration.config',
  'registration/registration.controller',
  'registration/registration.service',
  'registration/newEmail.registration.directive',
  'registration/strictEmail.registration.directive',
  'angular',
  'angular-bootstrap'], function (config, controller, service, newEmailDirective, strictEmailDirective) {
  'use strict';

  var registration = angular.module('registrationModule', ['restClient', 'ui.bootstrap']);
  registration.config(config);
  registration.controller('RegistrationController', controller);
  registration.service('registrationService', service);
  registration.directive('newEmail', newEmailDirective);
  registration.directive('strictEmail', strictEmailDirective);

});