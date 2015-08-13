/**
 * Based on Dejan Vasic's work.
 * https://dejanvasic.wordpress.com/2015/02/07/angularjs-password-strength-indicator/
 */

define(['angular', 'bootstrap', 'jquery'], function () {
  'use strict';
  
  angular.module('passwordStrengthModule', [])
    .directive('passwordStrength', [
      function () {
        return {
          require: 'ngModel',
          restrict: 'E',
          scope: {
            password: '=ngModel',
            val: '='
          },
          link: function (scope, elem, attrs, ctrl) {
            scope.$watch('password', function (newVal) {

              scope.strength = isSatisfied(newVal && newVal.length >= 8) +
                isSatisfied(newVal && /[A-z]/.test(newVal)) +
                isSatisfied(newVal && /(?=.*\W)/.test(newVal)) +
                isSatisfied(newVal && /\d/.test(newVal));

              scope.val = scope.strength;
              function isSatisfied(criteria) {
                return criteria ? 1 : 0;
              }
            }, true);
          },
          template: '<div class="progress">' +
          '<div class="progress-bar progress-bar-danger" style="width: {{strength >= 1 ? 25 : 0}}%"></div>' +
          '<div class="progress-bar progress-bar-warning" style="width: {{strength >= 2 ? 25 : 0}}%"></div>' +
          '<div class="progress-bar progress-bar-warning" style="width: {{strength >= 3 ? 25 : 0}}%"></div>' +
          '<div class="progress-bar progress-bar-success" style="width: {{strength >= 4 ? 25 : 0}}%"></div>' +
          '</div>'
        }
      }
    ]);

});