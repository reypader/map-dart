define(['angular', 'angular-mocks', 'registration/registration.module', 'rest-client/rest-client.module'], function () {
  describe('newEmail Directive', function () {
    'use strict';
    var $compile;
    var $rootScope;
    var restClientService;

    beforeEach(module('registrationModule'));

    beforeEach(inject(function (_$compile_, _$rootScope_, _restClientService_) {
      $compile = _$compile_;
      $rootScope = _$rootScope_;
      restClientService = _restClientService_;
      spyOn(restClientService, 'checkEmail');
    }));

    it('should not check for email usage when email is invalid format', inject(function () {
      var doc = $compile("<form name='f'><input type='email' new-email ng-model='email'></form>")($rootScope);
      $rootScope.$digest();

      var el = doc.find('input');
      angular.element(el).val('derp').triggerHandler('input');

      $rootScope.$apply();
      expect(restClientService.checkEmail).not.toHaveBeenCalled();
    }));

    it('should check for email usage when email is valid format', inject(function () {
      var doc = $compile("<form name='f'><input type='email' new-email ng-model='email'></form>")($rootScope);
      $rootScope.$digest();

      var el = doc.find('input');
      angular.element(el).val('derp@derp.com').triggerHandler('input');

      $rootScope.$apply();
      expect(restClientService.checkEmail).toHaveBeenCalledWith('derp@derp.com');
    }));
  });
});