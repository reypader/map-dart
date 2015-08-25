define([
  'progressbar'
], function (progress) {
  'use strict';

  controller.$inject = ['$scope', '$timeout', 'uiGmapIsReady', 'uiGmapGoogleMapApi'];

  function controller($scope, $timeout, uiGmapIsReady, uiGmapGoogleMapApi) {
    var _this = this;
    _this.ready = false;
    _this.showMap = false;
    _this.map = undefined;
    _this.mapInstance = {};
    _this.showCreateButton = false;
    _this.placingMarker = false;
    _this.demo = {
      min1: 20,
      max1: 40
    };

    _this.lastClick = undefined;
    _this.isDragging = false;

    activate();

    function activate() {
      console.log("Loading Google Maps API scripts...");
      uiGmapIsReady.promise(1).then(function (instances) {
        progress.scriptLoad();
        _this.ready = true;
        instances.forEach(function (inst) {
          _this.mapInstance.map = inst.map;
          _this.mapInstance.uuid = map.uiGmap_id;
          _this.mapInstance.number = inst.instance;
        });

        _this.api.event.addListener(
          _this.mapInstance.map,
          'drag',
          function (event) {
            _this.isDragging = true;
          });
        _this.api.event.addListener(
          _this.mapInstance.map,
          'mousedown',
          function (event) {
            _this.isDragging = false;
            contextMenu(false);
          });
        _this.api.event.addListener(
          _this.mapInstance.map,
          'mouseup',
          function (event) {
            if (_this.placingMarker && !_this.isDragging) {
              _this.placingMarker = false;
              contextMenu(true);
            }
          });
      });

      uiGmapGoogleMapApi.then(function (maps) {
        _this.api = maps;
        _this.map = {
          center: {latitude: 45, longitude: -73}, zoom: 10, options: {
            mapTypeId: _this.api.MapTypeId.TERRAIN, mapTypeControl: false,
            disableDefaultUI: true,
            styles: [{
              "featureType": "administrative",
              "elementType": "geometry",
              "stylers": [{"visibility": "on", weight: 2}]
            }, {
              "featureType": "road",
              "elementType": "labels.icon",
              "stylers": [{"visibility": "off"}]
            }, {
              "featureType": "administrative.land_parcel",
              "stylers": [{"visibility": "off"}]
            }, {
              "featureType": "administrative.neighborhood",
              "stylers": [{"visibility": "off"}]
            }, {
              "featureType": "landscape.man_made",
              "stylers": [{"visibility": "off"}]
            }, {"featureType": "landscape.natural", "stylers": [{"visibility": "on"}]}]
          }
        };
      });

    }

    function contextMenu(flag) {
      $scope.$apply(function () {
        _this.showCreateButton = flag;
      });
    }
  }

  return controller;

});