define(['angular', 'angular-mocks', 'navigation/navigation.module'], function () {
  'use strict';

  describe('Navigation Controller', function () {

    beforeEach(module('navigationModule'));

    xit('should change the active tab', inject(function ($controller) {
      var NavCtrl = $controller('NavCtrl');
      expect(NavCtrl).toBeDefined();

      NavCtrl.changeTab('time');
      expect(NavCtrl.activeTab).toEqual('time');
    }));
  });
});