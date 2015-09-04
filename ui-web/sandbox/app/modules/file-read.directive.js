/**
 * Based on Endy Tjahjono's answer in
 * http://stackoverflow.com/questions/17063000/ng-model-for-input-type-file
 */
define([
  'angular',
  'rest-client/photo/photo.rest-client.module'
], function () {
  'use strict';
  angular.module('fileReadModule', ['photoRestClient']).directive("fileRead", ['photoRestClientService', function (photoRestClientService) {

    return {
      scope: {
        fileRead: "="
      },
      link: function (scope, element, attributes) {
        element.bind("change", function (changeEvent) {
          scope.$apply(function () {
            var files = changeEvent.target.files;
            var uploads = photoRestClientService.upload(files);
            scope.fileRead = uploads;
          });
        });
      }
    }
  }]);
})