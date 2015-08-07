define([], function () {
  function config($tooltipProvider) {
    $tooltipProvider.setTriggers({
      'mouseenter': 'mouseleave',
      'click': 'click',
      'focus': 'blur',
    });
  }

  config.$inject = ['$tooltipProvider'];

  return config;
});