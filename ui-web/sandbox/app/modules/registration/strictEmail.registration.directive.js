define([], function () {
  'use strict';

  function directive() {
    return {
      require: 'ngModel',
      restrict: 'A',
      link: function (scope, elm, attrs, ctrl) {
        if (ctrl && ctrl.$validators.email) {
          ctrl.$validators.email = function (modelValue, viewValue) {
            var EMAIL_REGEXP = /[A-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?/;
            var val = modelValue || viewValue;
            return EMAIL_REGEXP.test(val);
          };
        }
      }
    };
  }

  directive.$inject = [];

  return directive;
});