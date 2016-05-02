define([
  'progressbar',
  'require'
], function (progress, require) {
  'use strict';

  controller.$inject = ['$scope', 'uiGmapIsReady', 'uiGmapGoogleMapApi', '$modal'];

  function controller($scope, uiGmapIsReady, uiGmapGoogleMapApi, $modal) {
    var _this = this;
    _this.ready = false;
    _this.showMap = false;
    _this.mapProperties = undefined;
    _this.mapInstance = {};
    _this.showCreateButton = false;
    _this.placingMarker = false;
    _this.isDragging = false;
    _this.markerLocation = {};

    _this.createMicroBlog = createMicroBlog;

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
          'zoom_changed',
          function (event) {
            contextMenu(false, event);
          });
        _this.api.event.addListener(
          _this.mapInstance.map,
          'mousedown',
          function (event) {
            _this.isDragging = false;
            contextMenu(false, event);
          });
        _this.api.event.addListener(
          _this.mapInstance.map,
          'mouseup',
          function (event) {
            if (_this.placingMarker && !_this.isDragging) {
              _this.placingMarker = false;
              contextMenu(true, event);
            }
          });
      });

      uiGmapGoogleMapApi.then(function (maps) {
        _this.api = maps;
        _this.mapProperties = {
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

    function contextMenu(flag, event) {
      $scope.$apply(function () {
        _this.showCreateButton = flag;
        if (flag) {
          _this.markerLocation.latitude = event.latLng.lat();
          _this.markerLocation.longitude = event.latLng.lng();
        }
      });
    }

    function createMicroBlog() {
      //_this.markerLocation
      var modalInstance = $modal.open({
        animation: true,
        templateUrl: require.toUrl('blog/create/create.blog.partial.html'),
        controller: 'CreateBlogController',
        controllerAs: 'cbc',
        size: 'lg',
        resolve: {
          location: function () {
            return _this.markerLocation;
          }
        }
      });

      modalInstance.result.then(function () {
        alert("success");
        //$scope.selected = selectedItem;
      }, function () {
        alert("dismiss");
        //$log.info('Modal dismissed at: ' + new Date());
      });

    }
  }

  return controller;

});