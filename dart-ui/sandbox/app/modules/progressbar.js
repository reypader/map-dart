define(['nprogress'], function (NProgress) {

  var progress = function () {
    var _this = this;
    _this.counter = 0;
    _this.count = 0;
    NProgress.configure({showSpinner: false,trickleSpeed: 1200});

    return {
      done: function () {
        NProgress.done();
      },
      increment: function () {
        NProgress.inc();
      },
      scriptLoad: function () {
        _this.counter++;
        NProgress.inc();
        if (_this.counter >= _this.count) {
          console.log("Done loading scripts.");
          NProgress.done();
        }
      },
      setScriptCount: function (count) {
        _this.count = count;
      }
    };
  };

  return progress();
});