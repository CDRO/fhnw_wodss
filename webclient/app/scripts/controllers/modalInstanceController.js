'use strict';

/**
 * This controller controls all uibmodal to update and create model
 * @param $scope
 * @param $uibModalInstance
 * @param isNew boolean represents if its a new model
 * @param model object to update
 * @constructor
 */
var ModalInstanceCtrl = function($scope, $uibModalInstance, isNew, model) {
  $scope.model = model;
  $scope.isNew = isNew;
  var modal = $uibModalInstance;

  $scope.ok = function () {
    modal.close(isNew, $scope.model);
  };

  $scope.cancel = function () {
    modal.dismiss('cancel');
  };
};

angular.module('angularWebclientApp').controller('ModalInstanceCtrl', ModalInstanceCtrl);
