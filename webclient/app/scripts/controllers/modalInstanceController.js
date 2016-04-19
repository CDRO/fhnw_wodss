'use strict';

/**
 * This controller controls all uibmodal to update and create model
 * @param $scope
 * @param $uibModalInstance
 * @param isNew boolean represents if its a new model
 * @param model object to update
 * @constructor
 */
var ModalInstanceCtrl = function($scope, $uibModalInstance, isNew, people, boards, model) {
  $scope.people = people;
  $scope.boards = boards;
  $scope.model = model;
  $scope.isNew = isNew;

  var modal = $uibModalInstance;

  $scope.ok = function () {
    modal.close($scope.model);
  };

  $scope.cancel = function () {
    modal.dismiss('cancel');
  };

  /* Options for Datepicker */
/*  $scope.clearDate = function() {
    $scope.dt = null;
  };*/

  $scope.inlineOptions = {
    minDate: new Date(),
    showWeeks: false
  };

  $scope.dateOptions = {
    formatYear: 'yyyy',
    maxDate: new Date().setFullYear(new Date().getFullYear() + 10),
    minDate: new Date(),
    startingDay: 1
  };

  $scope.popup1 = {
    opened: false
  };

  $scope.open1 = function() {
    $scope.popup1.opened = true;
  };

  /* Upload */
  $scope.clearUpload = function() {
    $scope.model.files = [];
  }
};

angular.module('angularWebclientApp').controller('ModalInstanceCtrl', ModalInstanceCtrl);
