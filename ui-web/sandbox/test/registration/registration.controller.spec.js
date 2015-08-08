define(['angular', 'registration/registration.module'], function () {
  'use strict';
  describe("Registration Controller", function () {
    var registrationService;
    var $q;
    var $rootScope;

    beforeEach(module('registrationModule'));

    beforeEach(inject(function (_$rootScope_, _registrationService_, _$q_) {
      $rootScope = _$rootScope_;
      registrationService = _registrationService_;
      $q = _$q_;
    }));

    it("should call RegistrationService.registerUser(newUserObject) when registerUser is invoked",
      inject(function ($controller) {
        spyOn(registrationService, 'registerUser').and.returnValue($q.when({}));
        var controller = $controller('RegistrationController');
        controller.newUser.email = 'test@email.com';
        controller.newUser.name = 'John Doe';
        controller.newUser.password = 'unencrypted';

        controller.registerUser();

        expect(registrationService.registerUser).toHaveBeenCalledWith(
          {
            email: 'test@email.com',
            name: 'John Doe',
            password: 'unencrypted'
          }
        );
      })
    );

    it("should change to /success after a successful registration",
      inject(function ($controller, $location, $rootScope) {
        spyOn(registrationService, 'registerUser').and.returnValue($q.when({}));
        spyOn($location, 'path');

        var controller = $controller('RegistrationController');

        controller.registerUser();

        $rootScope.$digest();
        expect($location.path).toHaveBeenCalledWith('/success');
      })
    );
  });
});