define(['angular', 'angular-mocks', 'registration/registration.module'], function () {
  'use strict';

  describe("Registration Module Configuration", function () {
    var $location;
    var $route;
    var $rootScope;
    var $httpBackend;

    beforeEach(module('registrationModule'));

    beforeEach(inject(function (_$location_, _$route_, _$rootScope_, _$httpBackend_) {
      $location = _$location_;
      $route = _$route_;
      $rootScope = _$rootScope_;
      $httpBackend = _$httpBackend_
    }));

    afterEach(function () {
      $httpBackend.verifyNoOutstandingExpectation();
      $httpBackend.verifyNoOutstandingRequest();
    });

    it('should redirect to the /login path on non-existent route', function () {
      $httpBackend.expectGET('modules/registration/main.registration.partial.html').respond(200, '');
      expect($location.path()).toBe('');

      $location.path('/derp');

      $rootScope.$digest();

      expect($location.path()).toBe('/');
      expect($route.current.controller).toBe('RegistrationController');
      $httpBackend.flush(1);
    });

  });
});