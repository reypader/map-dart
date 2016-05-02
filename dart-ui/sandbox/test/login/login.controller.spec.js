define(['angular', 'login/login.module', 'authentication/facebook/facebook.module', 'authentication/authentication.module'], function () {
  'use strict';

  describe("Login Controller", function () {
    var facebookService;
    var authenticationService;
    var googleService;

    beforeEach(module('loginModule'));

    beforeEach(inject(function (_authenticationService_, _facebookService_, _googleService_) {
      authenticationService = _authenticationService_;
      facebookService = _facebookService_;
      googleService = _googleService_;
    }));

    it("should call facebookService.fbLogin with the redirectToHome callback on fbLogin", inject(function ($controller) {
      spyOn(facebookService, 'fbLogin');

      var controller = $controller('LoginController');

      controller.fbLogin();

      expect(facebookService.fbLogin).toHaveBeenCalledWith(controller.redirectToHome, controller.authFail);
    }));

    it("should call googleService.gpLogin with the redirectToHome callback on gpLogin", inject(function ($controller) {
      spyOn(googleService, 'gpLogin');

      var controller = $controller('LoginController');

      controller.gpLogin();

      expect(googleService.gpLogin).toHaveBeenCalledWith(controller.redirectToHome, controller.authFail);
    }));

    it("should call authenticationService.authenticate with the redirectToHome callback on basicLogin", inject(function ($controller) {
      spyOn(authenticationService, 'authenticate');

      var controller = $controller('LoginController');
      controller.email = "test@email";
      controller.password = "unencrypted";

      controller.basicLogin();

      expect(authenticationService.authenticate).toHaveBeenCalledWith('test@email', {
        provider: 'basic',
        token: 'vCdsO5lQiMCM+TPENle9c4VIZK51FoqndxWbzz+IKm0='
      }, controller.redirectToHome, controller.authFail);
    }));

  });
});