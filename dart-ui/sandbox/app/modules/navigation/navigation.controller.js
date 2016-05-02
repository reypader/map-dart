define([], function () {
  'use strict';

  controller.$inject = ['authenticationService'];

  function controller(authenticationService) {
    var _this = this;

    _this.isCollapsed = true;

    _this.logout = logout;

    function logout(){
      authenticationService.clearSession();
      window.location.href = 'https://' + window.location.host + '/signin.html';
    }
  }

  return controller;
});