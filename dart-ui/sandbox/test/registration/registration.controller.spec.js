define(['angular', 'angular-mocks', 'registration/registration.module'], function () {
  'use strict';

  describe("Registration Controller", function () {
    var userRestClientService;
    var vcRecaptchaService;
    var $q;
    var $rootScope;

    beforeEach(module('registrationModule'));

    beforeEach(inject(function (_$rootScope_, _vcRecaptchaService_, _userRestClientService_, _$q_) {
      $rootScope = _$rootScope_;
      userRestClientService = _userRestClientService_;
      vcRecaptchaService = _vcRecaptchaService_;
      $q = _$q_;
    }));

    it("should call RegistrationService.registerUser(newUserObject) when registerUser is invoked",
      inject(function ($controller) {
        spyOn(userRestClientService, 'registerUser').and.returnValue($q.when({}));
        spyOn(userRestClientService, 'validateRecaptcha').and.returnValue($q.when({userIsHuman: true}));

        var controller = $controller('RegistrationController');
        controller.newUser.email = 'test@email.com';
        controller.newUser.name = 'John Doe';
        controller.npassword = 'unencrypted';

        controller.registerUser();

        $rootScope.$digest();
        expect(userRestClientService.validateRecaptcha).toHaveBeenCalled();
        expect(userRestClientService.registerUser).toHaveBeenCalledWith(
          {
            email: 'test@email.com',
            name: 'John Doe',
            password: 'vCdsO5lQiMCM+TPENle9c4VIZK51FoqndxWbzz+IKm0='
          }
        );
      })
    );

    it("should change to /success after a successful registration",
      inject(function ($controller) {
        spyOn(userRestClientService, 'registerUser').and.returnValue($q.when({}));
        spyOn(userRestClientService, 'validateRecaptcha').and.returnValue($q.when({userIsHuman: true}));

        var controller = $controller('RegistrationController');
        expect(controller.registrationDone).toBe(false);
        controller.registerUser();

        $rootScope.$digest();
        expect(controller.registrationDone).toBe(true);
      })
    );

    it("should reload recaptcha if user is not human",
      inject(function ($controller) {
        spyOn(userRestClientService, 'registerUser');
        spyOn(userRestClientService, 'validateRecaptcha').and.returnValue($q.when({userIsHuman: false}));
        spyOn(vcRecaptchaService, 'reload');

        var controller = $controller('RegistrationController');
        expect(controller.registrationDone).toBe(false);
        controller.registerUser();

        $rootScope.$digest();
        expect(controller.registrationDone).toBe(false);
        expect(userRestClientService.registerUser).not.toHaveBeenCalled();
        expect(userRestClientService.validateRecaptcha).toHaveBeenCalled();
        expect(vcRecaptchaService.reload).toHaveBeenCalled();
      })
    );
  });
});