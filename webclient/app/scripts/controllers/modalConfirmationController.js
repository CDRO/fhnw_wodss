'use strict';

/**
 * This controller controls the confirmation box, i.e. deletion confirmation
 * @param $scope
 * @param $uibModalInstance
 * @param model object
 * @constructor
 */
var ModalConfirmationCtrl = function($scope, $uibModalInstance, model, confirmation) {
  var modal = $uibModalInstance;
  $scope.model = model;
  $scope.confirmation = confirmation;

  $scope.ok = function () {
    modal.close($scope.model);
  };

  $scope.cancel = function () {
    modal.dismiss('cancel');
  };
};

angular.module('angularWebclientApp').controller('ModalConfirmationCtrl', ModalConfirmationCtrl);
