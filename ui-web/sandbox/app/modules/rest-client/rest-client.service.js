define([], function () {
  var service = function service($q) {
    return {
      checkEmail: function (email) {
        var bool = email === "used@email.com"

        var dummyResponse = {
          emailUsed: bool
        }
        var p = $q.defer();
        setTimeout(function () {
          p.resolve(dummyResponse);
        }, 1000);
        return p.promise;
      }
    };
  }

  service.$inject = ['$q'];

  return service;
})