FB = {
  init: function () {
  },
  api: function (url, fields, callback) {
    callback({email: "test@email"});
  }
};
define(['angular', 'angular-mocks', 'authentication/facebook/facebook.module', 'authentication/authentication.module'], function () {
  'use strict';

  describe("Facebook Service", function () {
    var authenticationService;
    var facebookService;

    beforeEach(module('facebookModule'));

    beforeEach(inject(function (_authenticationService_, _facebookService_) {
      authenticationService = _authenticationService_;
      facebookService = _facebookService_;
    }));

    it("should call the authenticationService.authenticate only if the loginCallback is called with a response.authResponse",
      function () {
        spyOn(authenticationService, 'authenticate');
        facebookService.loginCallback({});
        expect(authenticationService.authenticate).not.toHaveBeenCalled();

        facebookService.loginCallback({authResponse: {access_token: 'token'}});
        expect(authenticationService.authenticate).toHaveBeenCalledWith('test@email', {
          provider: 'facebook',
          token: 'token'
        }, undefined);
      }
    );
  });
});