define([], function () {
    'use strict';

    function config($routeProvider) {
        $routeProvider.otherwise({
            redirectTo: '/'
        });
    }

    config.$inject = ['$routeProvider'];

    return config;
});