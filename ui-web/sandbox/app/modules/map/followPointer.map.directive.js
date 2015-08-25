define([], function () {
  'use strict';

  directive.$inject = [];

  function directive() {
    function setLocation(element, event) {
      element.css({
        top: (event.clientY - 115) + 'px',
        left: (event.clientX - 37) + 'px'
      });
    }

    return {
      restrict: 'A',
      scope: {
        followPointer: '='
      },
      link: function (scope, element) {
        $('#map').mousemove(function (event) {
          if (scope.followPointer) {
            setLocation(element, event);
          }
        });

        $('#map').mousedown(function (event) {
          setLocation(element, event);
        });

      }
    };
  }

  return directive;
});