require.config({
    paths: {
        angular: '../../bower_components/angular/angular',
        'angular-route': '../../bower_components/angular-route/angular-route',
        bootstrap: '../../bower_components/bootstrap/dist/js/bootstrap',
        jquery: '../../bower_components/jquery/dist/jquery'
    },
    shim: {
        bootstrap: {
            deps: [
                'jquery'
            ]
        },
        angular: {
            exports: 'angular'
        },
        'angular-route': {
            deps: [
                'angular'
            ]
        }
    },
    packages: [

    ]
});
require(['bootstrap', 'angular', 'navigation/navigation_module', 'home/home_module'], function () {
    'use strict';

    angular.bootstrap(document, ['navigationModule','homeModule']);
});