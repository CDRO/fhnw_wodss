'use strict';

var HeaderCtrl = function ($scope, $location, config) {
  $scope.isActive = function (viewLocation) {
    return $location.path().indexOf(viewLocation) === 0;
  };
  $scope.isLoggedIn = function () {
    return config.isLoggedIn();
  };
};

HeaderCtrl.$inject = ['$scope', '$location', 'ConfigService'];

angular.module('angularWebclientApp').controller('HeaderCtrl', HeaderCtrl);
