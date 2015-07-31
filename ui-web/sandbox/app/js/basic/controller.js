define([], function() {
  function basicController($routeParams) {
    var _this = this;
    _this.ideaName = $routeParams.name;
  }

  basicController.$inject = ['$routeParams' ];

  return basicController;
});