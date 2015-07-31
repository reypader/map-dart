'use strict';
define([ "basic/module" ], function(notepad) {
  describe('Basic Controller', function() {
    
    beforeEach(module('basicModule'));

    it('should define basic controller', inject(function($controller) {
      var $routeParams = {name:'strong'};
      var BasicCtrl = $controller('BasicCtrl',{$routeParams:$routeParams});
      expect(BasicCtrl).toBeDefined();
      expect(BasicCtrl.ideaName).toEqual('strong');
    }));
  });
});