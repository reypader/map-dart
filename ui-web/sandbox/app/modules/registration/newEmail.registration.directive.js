define([], function () {
  'use strict';

  function directive(userRestClientService, $q) {
    var EMAIL_REGEXP = /[A-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?/;
    return {
      restrict: 'A',
      require: 'ngModel',
      link: function (scope, element, attr, ctrl) {
        if (ctrl && ctrl.$validators.email) {
          ctrl.$asyncValidators.usedEmail = function (modelValue, viewValue) {

            var val = modelValue || viewValue;
            if (EMAIL_REGEXP.test(val) && ctrl.$validators.email) {
              var def = $q.defer();
              userRestClientService.checkEmail(val).then(function (response) {
                if (response.emailUsed) {
                  def.reject();
                } else {
                  def.resolve();
                }
              });
              return def.promise;
            } else {
              return $q.when({});
            }

          };
        }
      }
    };
  }

  directive.$inject = ['userRestClientService', '$q'];

  return directive;
});