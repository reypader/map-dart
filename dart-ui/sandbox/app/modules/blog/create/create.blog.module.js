define([
  './create.blog.controller',
  'angular'
], function (controller) {
  'use strict';

  angular.module('createBlogModule', [])
    .controller('CreateBlogController', controller);
});