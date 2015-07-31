define([], function() {
  function config($routeProvider) {
    $routeProvider.when('/', {
      templateUrl : 'home.html'
    }).when('/basic/:name', {
      templateUrl : 'js/basic/basic.html',
      controller : 'BasicCtrl'
    }).otherwise({
      redirectTo : '/'
    });
  }
  config.$inject = [ '$routeProvider' ];

  return config;
});