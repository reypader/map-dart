'use strict';
define([ "basic/module" ], function(notepad) {
  describe('Basic Controller', function() {
    
    beforeAll(function() {
      angular.bootstrap(document, [ 'basicModule' ]);
    });
    
    beforeEach(module('basicModule'));

    var $controller;

    beforeEach(inject(function(_$controller_) {
      // The injector unwraps the underscores (_) from around the parameter
      // names when matching
      $controller = _$controller_;
    }));
    it('should define basic controller', inject(function($controller) {
      var BasicCtrl = $controller('BasicCtrl');
      expect(BasicCtrl).toBeDefined();
    }));
  });
});