define([], function () {
  'use strict';

  directive.$inject = [];

  function directive() {
    return {
      restrict: 'A',
      scope: {
        followPointer: '='
      },
      link: function (scope, element) {
        $('body').mousemove(function (event) {
          if (scope.followPointer) {
            element.css({
              top: (event.clientY - 50) + 'px',
              left: (event.clientX - 10) + 'px'
            });
          }
        });

        $('body').mousedown(function (event) {
          switch (event.which) {
            case 1:
              if (scope.followPointer) {
                element.css({
                  top: (event.clientY - 50) + 'px',
                  left: (event.clientX - 10) + 'px'
                });
              }
              break;
            case 3:
              element.css({
                top: (event.clientY - 50) + 'px',
                left: (event.clientX - 10) + 'px'
              });
              break;
          }
        });

      }
    };
  }

  return directive;
});