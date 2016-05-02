define([], function () {
  'use strict';

  directive.$inject = [];

  function directive() {
    function setLocation(element, event) {
      element.css({
        top: (event.clientY - 115) + 'px',
        left: (event.clientX - 27) + 'px'
      });
    }

    return {
      restrict: 'A',
      scope: {
        followPointer: '=',
        canvas:'=followPointerCanvas'
      },
      link: function (scope, element) {
        $(scope.canvas).mousemove(function (event) {
          if (scope.followPointer) {
            setLocation(element, event);
          }
        });

        $(scope.canvas).mousedown(function (event) {
          setLocation(element, event);
        });

      }
    };
  }

  return directive;
});