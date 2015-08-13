define(['angular', 'angular-mocks', 'authentication/authentication.module', 'rest-client/user/user.rest-client.module'], function () {
  'use strict';

  describe("Authentication Service", function () {
    var $rootScope;
    var authenticationService;
    var userRestClientService;
    var $q;

    beforeEach(module('authenticationModule'));

    beforeEach(inject(function (_$rootScope_, _userRestClientService_, _$q_, _authenticationService_) {
      $rootScope = _$rootScope_;
      userRestClientService = _userRestClientService_;
      $q = _$q_;
      authenticationService = _authenticationService_;
    }));

    it("should set the 'sessionToken' cookie if authentication is successful",
      function () {
        var dummy = {
          callback: function (stat) {
          }
        }
        spyOn(dummy, 'callback');
        spyOn(userRestClientService, 'authenticate').and.returnValue($q.when({
          token: 'token'
        }));
        spyOn(authenticationService, 'setSession');

        expect(authenticationService.authenticated).toBe(false);
        authenticationService.authenticate('test@email', {
          provider: 'facebook',
          token: 'token',
          data: 'data'
        }, dummy.callback);

        $rootScope.$digest();
        expect(userRestClientService.authenticate).toHaveBeenCalledWith({
          email: 'test@email',
          provider: 'facebook',
          token: 'token',
          data: 'data'
        });
        expect(authenticationService.setSession).toHaveBeenCalled();
        expect(dummy.callback).toHaveBeenCalledWith(true);
        expect(authenticationService.authenticated).toBe(true);
      }
    );

    it("should NOT set the 'sessionToken' cookie if authentication is successful",
      function () {
        var dummy = {
          callback: function (stat) {
          }
        }
        spyOn(dummy, 'callback');
        spyOn(userRestClientService, 'authenticate').and.returnValue($q.when({}));
        spyOn(authenticationService, 'setSession');

        expect(authenticationService.authenticated).toBe(false);
        authenticationService.authenticate('test@email', {
          provider: 'facebook',
          token: 'token',
          data: 'data'
        }, dummy.callback);

        $rootScope.$digest();
        expect(userRestClientService.authenticate).toHaveBeenCalledWith({
          email: 'test@email',
          provider: 'facebook',
          token: 'token',
          data: 'data'
        });
        expect(authenticationService.setSession).not.toHaveBeenCalled();
        expect(dummy.callback).toHaveBeenCalledWith(false);
        expect(authenticationService.authenticated).toBe(false);
      }
    );
  });
});