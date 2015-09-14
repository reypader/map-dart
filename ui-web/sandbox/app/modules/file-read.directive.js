/**
 * Based on Endy Tjahjono's answer in
 * http://stackoverflow.com/questions/17063000/ng-model-for-input-type-file
 */
define([
  'angular',
  'rest-client/photo/photo.rest-client.module'
], function () {
  'use strict';
  angular.module('fileReadModule', ['photoRestClient'])
    .directive('fileRead', [
      'photoRestClientService',
      '$timeout',
      function (photoRestClientService, $timeout) {
        return {
          scope: {
            fileRead: '='
          },
          link: function (scope, element, attributes) {
            element.bind('change', function (changeEvent) {
              scope.$apply(function () {
                var files = changeEvent.target.files;
                var queue = [];
                for (var i = 0; i < files.length; i++) {
                  var f = {
                    servingUrl: URL.createObjectURL(files[i]),
                    file: files[i]
                  };
                  f.file.active = false;
                  f.file.done = false;
                  queue.push(f);
                }

                $timeout(function () {
                  photoRestClientService.upload(queue);
                }, 0);

                scope.fileRead = queue;
              });
            });
          }
        }
      }]);
})