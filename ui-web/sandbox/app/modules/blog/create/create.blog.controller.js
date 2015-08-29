define([],function(){
  'use strict';

  controller.$inject = ['location','$scope'];

  function controller(location,$scope){
    var _this = this;
    _this.location = location;
    _this.files = undefined;

  }

  return controller;

});