define(['angular', 'angular-mocks', 'registration/registration.module', 'rest-client/user/user.rest-client.module'], function () {
  'use strict';

  describe('newEmail Directive', function () {
    var $compile;
    var $rootScope;
    var userRestClientService;
    var $q;

    beforeEach(module('registrationModule'));

    beforeEach(inject(function (_$compile_, _$rootScope_, _userRestClientService_, _$q_) {
      $compile = _$compile_;
      $rootScope = _$rootScope_;
      userRestClientService = _userRestClientService_;
      $q = _$q_;
    }));

    it('should not check for email usage when email is invalid format', inject(function () {
      spyOn(userRestClientService, 'checkEmail');
      var doc = $compile("<form name='f'><input type='email' new-email ng-model='email'></form>")($rootScope);
      $rootScope.$digest();

      var el = doc.find('input');
      angular.element(el).val('derp').triggerHandler('input');

      $rootScope.$apply();
      expect(userRestClientService.checkEmail).not.toHaveBeenCalled();
      expect($rootScope.f.$error.usedEmail).not.toBeDefined();
    }));

    it('should check for email usage when email is valid format', inject(function () {
      spyOn(userRestClientService, 'checkEmail').and.callFake(function () {
        var dummyResponse = {
          emailUsed: false
        }
        return $q.when(dummyResponse);
      });
      var doc = $compile("<form name='f'><input type='email' new-email ng-model='email'></form>")($rootScope);
      $rootScope.$digest();

      var el = doc.find('input');
      angular.element(el).val('derp@derp.com').triggerHandler('input');

      $rootScope.$apply();
      expect(userRestClientService.checkEmail).toHaveBeenCalledWith('derp@derp.com');
      expect($rootScope.f.$error.usedEmail).not.toBeDefined();
    }));

    it('should reflect email usage response', inject(function () {
      spyOn(userRestClientService, 'checkEmail').and.callFake(function () {
        var dummyResponse = {
          emailUsed: true
        }
        return $q.when(dummyResponse);
      });
      var doc = $compile("<form name='f'><input type='email' new-email ng-model='email'></form>")($rootScope);
      $rootScope.$digest();

      var el = doc.find('input');
      angular.element(el).val('derp@derp.com').triggerHandler('input');

      $rootScope.$apply();
      expect(userRestClientService.checkEmail).toHaveBeenCalledWith('derp@derp.com');
      expect($rootScope.f.$error.usedEmail).toBeDefined();
    }));
  });
});