FB = {
  init: function () {
  },
  api: function (url, fields, callback) {
    callback({email: "test@email", name: "John Doe", picture: {data: {url: 'url'}}});
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
        facebookService.secondaryCallback = function () {
          console.log("secondary callback")
        };
        facebookService.failCallback = function () {
          console.log("fail callback")
        };
        spyOn(authenticationService, 'authenticate');
        spyOn(facebookService, 'secondaryCallback');
        spyOn(facebookService, 'failCallback');

        facebookService.loginCallback({authResponse: {accessToken: 'token', userID: 'user_id'}});
        expect(authenticationService.authenticate).toHaveBeenCalledWith('test@email', {
          provider: 'facebook',
          token: 'token',
          data: {id: 'user_id', name: 'John Doe', photoUrl: 'url'}
        }, facebookService.secondaryCallback, facebookService.failCallback);
        expect(facebookService.failCallback).not.toHaveBeenCalled();
      }
    );

    it("should call the failCallback if Facebook user did not authenticate properly",
      function () {
        facebookService.secondaryCallback = function () {
          console.log("secondary callback")
        };
        facebookService.failCallback = function () {
          console.log("fail callback")
        };
        spyOn(authenticationService, 'authenticate');
        spyOn(facebookService, 'secondaryCallback');
        spyOn(facebookService, 'failCallback');
        facebookService.loginCallback({});
        expect(authenticationService.authenticate).not.toHaveBeenCalled();
        expect(facebookService.failCallback).toHaveBeenCalled();
      }
    );
  });
});