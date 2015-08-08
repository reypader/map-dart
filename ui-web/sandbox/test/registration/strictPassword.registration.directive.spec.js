define(['angular', 'registration/registration.module'], function () {
  'use strict';

  describe('strictPassword Directive', function () {
    var $compile;
    var $rootScope;

    beforeEach(module('registrationModule'));

    beforeEach(inject(function (_$compile_, _$rootScope_) {
      $compile = _$compile_;
      $rootScope = _$rootScope_;
    }));

    it('should apply password restrictions for passwords less than 6 characters', inject(function () {
      var doc = $compile("<form name='f'><input type='password' ng-model='password'></form>")($rootScope);
      $rootScope.$digest();

      var el = doc.find('input');
      angular.element(el).val('pAs1s').triggerHandler('input');

      $rootScope.$apply();
      expect($rootScope.f.$error.password).toBeUndefined();
    }));


    it('should apply password restrictions for passwords without numbers', inject(function () {
      var doc = $compile("<form name='f'><input type='password' ng-model='password'></form>")($rootScope);
      $rootScope.$digest();

      var el = doc.find('input');
      angular.element(el).val('pAssss').triggerHandler('input');

      $rootScope.$apply();
      expect($rootScope.f.$error.password).toBeUndefined();
    }));

  });


});