define([], function () {
  'use strict';

  function controller() {
    var _this = this;
    _this.activeTab = 'Home';

    _this.changeTab = function changeTab(tabName) {
      _this.activeTab = tabName;
    }
  }

  controller.$inject = [];

  return controller;
});