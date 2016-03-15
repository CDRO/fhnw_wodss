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
  var self = this;
  this.list = [];

  this.selectedBoard = params.boardId;

  if(params.boardId){
    taskService.getByBoard(params.boardId).then(function(data){
        self.list = data;
    });
  }else{
    taskService.getAll().then(function(data){
      self.list = data;
    });
  }

  this.add = function(task){
      taskService.add(task).then(function(data){
          self.list.push(data);
      });
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
