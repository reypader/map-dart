define(['angular', 'angular-mocks', '../app/modules/navigation/navigation.module'], function () {
  describe('Navigation Controller', function () {
    'use strict';
    beforeEach(module('navigationModule'));

    it('should change the active tab', inject(function ($controller) {
      var NavCtrl = $controller('NavCtrl');
      expect(NavCtrl).toBeDefined();

      NavCtrl.changeTab('time');
      expect(NavCtrl.activeTab).toEqual('time');
    }));
  });
});