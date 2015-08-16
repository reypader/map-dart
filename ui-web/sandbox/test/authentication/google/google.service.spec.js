define(['gapi.loader', 'angular', 'angular-mocks', 'authentication/google/google.module', 'authentication/authentication.module'], function (gapi) {
  'use strict';

  describe("Google Service", function () {
    var authenticationService;
    var googleService;
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
    };
    gapi.auth2 = {
      getAuthInstance: function () {
        return authInstance;
      }
    };

    beforeEach(module('googleModule'));

    beforeEach(inject(function (_authenticationService_, _googleService_) {
      authenticationService = _authenticationService_;
      googleService = _googleService_;
    }));

    it("should call the authenticationService.authenticate only if the loginCallback is called with a response object",
      function () {
        googleService.secondaryCallback = function () {
          console.log("secondary callback")
        };
        googleService.failCallback = function () {
          console.log("fail callback")
        };
        spyOn(authenticationService, 'authenticate');
        spyOn(googleService, 'secondaryCallback');
        spyOn(googleService, 'failCallback');

        googleService.loginCallback({id_token: 'token'});
        expect(authenticationService.authenticate).toHaveBeenCalledWith('test@email', {
          provider: 'google',
          token: 'token',
          data: 'id=test%40email;name=John%20Doe;photoUrl=url'
        }, googleService.secondaryCallback, googleService.failCallback);
        expect(googleService.failCallback).not.toHaveBeenCalled();
      }
    );

    it("should call the failCallback if the user did not authenticate properly with Google",
      function () {
        googleService.secondaryCallback = function () {
          console.log("secondary callback")
        };
        googleService.failCallback = function () {
          console.log("fail callback")
        };
        spyOn(authenticationService, 'authenticate');
        spyOn(googleService, 'secondaryCallback');
        spyOn(googleService, 'failCallback');
        googleService.loginCallback({});
        expect(authenticationService.authenticate).not.toHaveBeenCalled();

        expect(googleService.failCallback).toHaveBeenCalled();
      }
    );
  });
});