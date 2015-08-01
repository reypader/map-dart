define(['angular', 'angular-route', 'navigation/navigation_config', 'navigation/navigation_controller'], function (angular, angularRoute, config, controller) {
    'use strict';

    var app = angular.module('navigationModule', ['ngRoute']);
    app.config(config);
    app.controller('NavCtrl', controller);
});