define([
  './registration.config',
  './registration.controller',
  './newEmail.registration.directive',
  './strictEmail.registration.directive',
  'angular',
  'angular-bootstrap',
  'angular-route',
  'cryptojs',
  'angular-recaptcha',
  'rest-client/user/user.rest-client.module'
], function (config, controller, newEmailDirective, strictEmailDirective) {
  'use strict';

  angular.module('registrationModule', ['userRestClient', 'ui.bootstrap', 'ngRoute', 'vcRecaptcha'])
    .config(config)
    .controller('RegistrationController', controller)
    .directive('newEmail', newEmailDirective)
    .directive('strictEmail', strictEmailDirective);

});