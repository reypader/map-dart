define([], function () {
  function directive(restClientService, $q) {
    return {
      restrict: 'A',
      require: 'ngModel',
      link: function (scope, element, attr, ctrl) {
        var re = /[A-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?/;

        ctrl.$asyncValidators.usedEmail = function (modelValue, viewValue) {
          var def = $q.defer();
          var val = modelValue || viewValue;
          if (re.test(val)) {
            if (restClientService.checkEmail(val)) {
              def.resolve();
            } else {
              def.reject();
            }
          } else {
            def.reject()
          }
          return def.promise;
        };
      }
    };
  }

  directive.$inject = ['restClientService', '$q'];

  return directive;
});