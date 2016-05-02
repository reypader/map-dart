define(['angular', 'angular-mocks', 'registration/registration.module'], function () {
  'use strict';

  describe('strictEmail Directive', function () {

    var $compile;
    var $rootScope;

    beforeEach(module('registrationModule'));

    beforeEach(inject(function (_$compile_, _$rootScope_) {
      $compile = _$compile_;
      $rootScope = _$rootScope_;
    }));

    it('should be more strict than angularjs default email validation', inject(function () {
      var doc = $compile("<form name='f'><input type='email' ng-model='email'></form>")($rootScope);
      var docStrict = $compile("<form name='fstrict'><input type='email' strict-email ng-model='emailstrict'></form>")($rootScope);
      $rootScope.$digest();

      var el = doc.find('input');
      angular.element(el).val('a@a').triggerHandler('input');

      var elStrict = docStrict.find('input');
      angular.element(elStrict).val('a@a').triggerHandler('input');

      $rootScope.$apply();
      expect($rootScope.f.$error.email).toBeUndefined();
      expect($rootScope.fstrict.$error.email).toBeDefined();
    }));

  });
});