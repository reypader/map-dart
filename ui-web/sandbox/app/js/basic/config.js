define([], function() {
  function config($routeProvider) {
    $routeProvider.when('/', {
      templateUrl : 'home.html'
    }).when('/basic/:name', {
      templateUrl : 'js/basic/basic.html',
      controller : 'BasicController'
    }).otherwise({
      redirectTo : '/'
    });
  }
  config.$inject = [ '$routeProvider' ];

  return config;
});