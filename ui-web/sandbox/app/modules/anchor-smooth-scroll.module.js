/**
 * Taken from justinmc's work.
 *
 * https://gist.github.com/justinmc/d72f38339e0c654437a2
 */

define(['angular'], function () {
  /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
   * Anchor Smooth Scroll - Smooth scroll to the given anchor on click
   *   adapted from this stackoverflow answer: http://stackoverflow.com/a/21918502/257494
   ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
  angular.module('anchorSmoothScrollModule', []).directive('anchorSmoothScroll', function ($location) {
    'use strict';

    return {
      restrict: 'A',
      replace: false,
      scope: {
        'anchorSmoothScroll': '@'
      },

      link: function ($scope, $element, $attrs) {

        initialize();

        /* initialize -
         ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
        function initialize() {
          createEventListeners();
        }

        /* createEventListeners -
         ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
        function createEventListeners() {
          // listen for a click
          $element.on('click', function () {
            // smooth scroll to the passed in element
            scrollTo($scope.anchorSmoothScroll);
          });
        }

        /* scrollTo -
         ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
        function scrollTo(eID) {

          // This scrolling function
          // is from http://www.itnewb.com/tutorial/Creating-the-Smooth-Scroll-Effect-with-JavaScript

          var stopY = elmYPosition(eID);
          //window.scrollTo(0, stopY);
          $('html, body').animate({
            scrollTop: stopY
          }, 1000, function () {
          });
          return;
        }

        /* scrollTo -
         ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
        function elmYPosition(eID) {
          var elm = document.getElementById(eID);
          var y = elm.offsetTop;
          var node = elm;
          while (node.offsetParent && node.offsetParent != document.body) {
            node = node.offsetParent;
            y += node.offsetTop;
          }
          return y;
        }
      }
    };
  });

});