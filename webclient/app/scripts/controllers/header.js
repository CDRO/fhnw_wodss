'use strict';

var HeaderCtrl = function ($scope, $location, config, alertService) {
  $scope.isActive = function (viewLocation) {
    return $location.path().indexOf(viewLocation) === 0;
  };
  $scope.isLoggedIn = function () {
    return config.isLoggedIn();
  };

  // Bind close alert to header
  $scope.closeAlert = alertService.closeAlert;
};

HeaderCtrl.$inject = ['$scope', '$location', 'ConfigService', 'AlertService'];

angular.module('angularWebclientApp').controller('HeaderCtrl', HeaderCtrl);
