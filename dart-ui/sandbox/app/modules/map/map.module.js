define([
  './map.config',
  './map.controller',
  './followPointer.map.directive',
  'angular',
  'angular-google-maps'
], function (config, controller, followPointerDirective) {

  angular.module('mapModule', ['uiGmapgoogle-maps'])
    .config(config)
    .controller('MapController', controller)
    .directive("followPointer", followPointerDirective);
})