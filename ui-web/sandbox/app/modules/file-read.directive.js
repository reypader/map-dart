/**
 * Based on Endy Tjahjono's answer in
 * http://stackoverflow.com/questions/17063000/ng-model-for-input-type-file
 */

define(['cryptojs.md5', 'cryptojs.lib', 'angular', 'angular-ui-uploader', 'rest-client/photo/photo.rest-client.module'], function () {
  angular.module('fileReadModule', ['ui.uploader', 'photoRestClient']).directive("fileRead", ['uiUploader', 'photoRestClientService', function (uiUploader, photoRestClientService) {
    return {
      scope: {
        fileRead: "="
      },
      link: function (scope, element, attributes) {
        element.bind("change", function (changeEvent) {
          scope.$apply(function () {
            var files = changeEvent.target.files;
            scope.fileRead = files;

            for (var index = 0; index < files.length; ++index) {
              var entry = files[index];
              var reader = new FileReader();
              reader.onload = function (event) {
                var wordArray = CryptoJS.lib.WordArray.create(event.target.result);
                var md5 = CryptoJS.MD5(wordArray);
                photoRestClientService.getUploadURL(md5, entry.type).then(function (data) {
                  alert(JSON.stringify(data));
                });
              };
              reader.readAsArrayBuffer(entry);
            };
          });
        });
      }
    }
  }]);
});