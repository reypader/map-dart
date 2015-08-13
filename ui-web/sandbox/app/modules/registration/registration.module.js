define([
  'registration/registration.config',
  'registration/registration.controller',
  'registration/newEmail.registration.directive',
  'registration/strictEmail.registration.directive',
  'angular',
  'angular-bootstrap',
  'angular-route',
  'cryptojs',
  'angular-recaptcha',
  'rest-client/user/user.rest-client.module'], function (config, controller, newEmailDirective, strictEmailDirective) {
  'use strict';

  var registration = angular.module('registrationModule', ['userRestClient', 'ui.bootstrap', 'ngRoute','vcRecaptcha']);
  registration.config(config);
  registration.controller('RegistrationController', controller);
  registration.directive('newEmail', newEmailDirective);
  registration.directive('strictEmail', strictEmailDirective);

});