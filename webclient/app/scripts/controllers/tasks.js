'use strict';

/**
 * @ngdoc function
 * @name angularWebclientApp.controller:TasksCtrl
 * @description
 * # TasksCtrl
 * Controller of the angularWebclientApp
 */
var module = angular.module('angularWebclientApp');

var taskController = function(taskService, params, $uibModal){
  this.list = [];
  if(params.boardId){
    this.list = taskService.getByBoard(params.boardId);
  }else{
    this.list = taskService.getAll();
  }

  this.add = function(task){
      taskService.add(task);
  };

  this.remove = function(task){
    taskService.remove(task);
  };

  var openModal = function(isNew, model) {
    var modalInstance = $uibModal.open({
      templateUrl: 'views/taskOverlay.html',
      controller: 'ModalInstanceCtrl',
      size: "lg",
      resolve: {
        isNew: isNew,
        model: model
      }
    });

    modalInstance.result.then(function(isNew, model) {
      if(isNew){
        taskService.add(model);
      }else{
        taskService.update(model);
      }
    }, function () {

    });
  };

  this.create = function(){
    openModal(true);
  };

  this.update = function(model){
    openModal(false, model);
  };
};

taskController.$inject = ['TaskService', '$stateParams', '$uibModal'];

module.controller('TasksCtrl', taskController);
