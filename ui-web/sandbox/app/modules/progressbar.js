define(['nprogress'], function (NProgress) {

  var progress = function () {
    var _this = this;
    _this.counter = 0;
    NProgress.configure({ showSpinner: false });

    return {
      increment: function () {
        _this.counter++;
        NProgress.inc();
        if ( _this.counter >= 5) {
          NProgress.done();
        }
      }
    };
  };

  return progress();
});