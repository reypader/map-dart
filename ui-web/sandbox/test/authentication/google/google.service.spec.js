define(['gapi.loader', 'angular', 'angular-mocks', 'authentication/google/google.module', 'authentication/authentication.module'], function (gapi) {
  'use strict';

  describe("Google Service", function () {
    var authenticationService;
    var googleService;

    beforeEach(module('googleModule'));

    beforeEach(inject(function (_authenticationService_, _googleService_) {
      authenticationService = _authenticationService_;
      googleService = _googleService_;
    }));

    it("should call the authenticationService.authenticate only if the loginCallback is called with a response object",
      function () {
        var basicProfile = {
          getEmail: function () {
            return 'test@email';
          },
          getName: function () {
            return 'John Doe';
          },
          getImageUrl: function () {
            return 'url'
          }
        };
        var authInstance = {
          currentUser: {
            get: function () {
              return {
                hasGrantedScopes: function () {
                  return true;
                },
                getBasicProfile: function () {
                  return basicProfile;
                },
              }
            }
          },
          isSignedIn: {
            get: function () {
              return true;
            }
          }
        }
        gapi.auth2 = {
          getAuthInstance: function () {
            return authInstance;
          }
        };
        spyOn(authenticationService, 'authenticate');
        googleService.loginCallback({});
        expect(authenticationService.authenticate).not.toHaveBeenCalled();

        googleService.loginCallback({id_token: 'token'});
        expect(authenticationService.authenticate).toHaveBeenCalledWith('test@email', {
          provider: 'google',
          token: 'token',
          data: 'id=test%40email;name=John%20Doe;photoUrl=url'
        }, undefined);
      }
    );
  });
});