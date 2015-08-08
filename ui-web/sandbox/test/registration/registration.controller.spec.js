define(['angular', 'angular-mocks', 'registration/registration.module'], function () {
  'use strict';

  describe("Registration Controller", function () {
    var restClientService;
    var $q;
    var $rootScope;

    beforeEach(module('registrationModule'));

    beforeEach(inject(function (_$rootScope_, _restClientService_, _$q_) {
      $rootScope = _$rootScope_;
      restClientService = _restClientService_;
      $q = _$q_;
    }));

    it("should call RegistrationService.registerUser(newUserObject) when registerUser is invoked",
      inject(function ($controller) {
        spyOn(restClientService, 'registerUser').and.returnValue($q.when({}));
        var controller = $controller('RegistrationController');
        controller.newUser.email = 'test@email.com';
        controller.newUser.name = 'John Doe';
        controller.newUser.password = 'unencrypted';

        controller.registerUser();

        expect(restClientService.registerUser).toHaveBeenCalledWith(
          {
            email: 'test@email.com',
            name: 'John Doe',
            password: 'unencrypted'
          }
        );
      })
    );

    it("should change to /success after a successful registration",
      inject(function ($controller) {
        spyOn(restClientService, 'registerUser').and.returnValue($q.when({}));

        var controller = $controller('RegistrationController');
        expect(controller.registrationDone).toBe(false);
        controller.registerUser();

        $rootScope.$digest();
        expect(controller.registrationDone).toBe(true);
      })
    );
  });
});