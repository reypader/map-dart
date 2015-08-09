define(['angular', 'angular-mocks', 'registration/registration.module'], function () {
  'use strict';

  describe("Registration Controller", function () {
    var restClientService;
    var vcRecaptchaService;
    var $q;
    var $rootScope;

    beforeEach(module('registrationModule'));

    beforeEach(inject(function (_$rootScope_, _vcRecaptchaService_, _restClientService_, _$q_) {
      $rootScope = _$rootScope_;
      restClientService = _restClientService_;
      vcRecaptchaService = _vcRecaptchaService_;
      $q = _$q_;
    }));

    it("should call RegistrationService.registerUser(newUserObject) when registerUser is invoked",
      inject(function ($controller) {
        spyOn(restClientService, 'registerUser').and.returnValue($q.when({}));
        spyOn(restClientService, 'validateRecaptcha').and.returnValue($q.when({userIsHuman: true}));

        var controller = $controller('RegistrationController');
        controller.newUser.email = 'test@email.com';
        controller.newUser.name = 'John Doe';
        controller.npassword = 'unencrypted';

        controller.registerUser();

        $rootScope.$digest();
        expect(restClientService.validateRecaptcha).toHaveBeenCalled();
        expect(restClientService.registerUser).toHaveBeenCalledWith(
          {
            email: 'test@email.com',
            name: 'John Doe',
            password: 'bc276c3b995088c08cf933c43657bd73854864ae75168aa777159bcf3f882a6d'
          }
        );
      })
    );

    it("should change to /success after a successful registration",
      inject(function ($controller) {
        spyOn(restClientService, 'registerUser').and.returnValue($q.when({}));
        spyOn(restClientService, 'validateRecaptcha').and.returnValue($q.when({userIsHuman: true}));

        var controller = $controller('RegistrationController');
        expect(controller.registrationDone).toBe(false);
        controller.registerUser();

        $rootScope.$digest();
        expect(controller.registrationDone).toBe(true);
      })
    );

    it("should reload recaptcha if user is not human",
      inject(function ($controller) {
        spyOn(restClientService, 'registerUser');
        spyOn(restClientService, 'validateRecaptcha').and.returnValue($q.when({userIsHuman: false}));
        spyOn(vcRecaptchaService, 'reload');

        var controller = $controller('RegistrationController');
        expect(controller.registrationDone).toBe(false);
        controller.registerUser();

        $rootScope.$digest();
        expect(controller.registrationDone).toBe(false);
        expect(restClientService.registerUser).not.toHaveBeenCalled();
        expect(restClientService.validateRecaptcha).toHaveBeenCalled();
        expect(vcRecaptchaService.reload).toHaveBeenCalled();
      })
    );
  });
});