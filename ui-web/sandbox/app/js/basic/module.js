define([ 'basic/config', 'basic/controller' ], function(config, controller) {
  var app = angular.module('basicModule', ['ngRoute']);
  app.config(config);
  app.controller('BasicCtrl', controller);
});