define(['angular', 'angular-mocks', 'navigation/navigation_module'], function () {
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