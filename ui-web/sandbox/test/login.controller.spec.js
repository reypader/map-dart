define(['angular', 'login.controller', 'authentication/facebook/facebook.module', 'authentication/authentication.module'], function () {
  'use strict';

  describe("Login Controller", function () {
    var facebookService;
    var authenticationService;

    beforeEach(module('loginModule'));

    beforeEach(inject(function (_authenticationService_, _facebookService_) {
      authenticationService = _authenticationService_;
      facebookService = _facebookService_;
    }));

    it("should call fbLogin with the redirectToHome callback", inject(function ($controller) {
      spyOn(facebookService, 'fbLogin');

      var controller = $controller('LoginController');

      controller.fbLogin();

      expect(facebookService.fbLogin).toHaveBeenCalledWith(controller.redirectToHome);
    }));

    it("should call authenticate with the redirectToHome callback", inject(function ($controller) {
      spyOn(authenticationService, 'authenticate');

      var controller = $controller('LoginController');
      controller.email = "test@email";
      controller.password = "unencrypted";

      controller.basicLogin();

      expect(authenticationService.authenticate).toHaveBeenCalledWith('test@email', {
        provider: 'self',
        token: 'bc276c3b995088c08cf933c43657bd73854864ae75168aa777159bcf3f882a6d'
      }, controller.redirectToHome);
    }));

  });
});