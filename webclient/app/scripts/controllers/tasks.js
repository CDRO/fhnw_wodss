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

  this.boards = params.boards ? params.boards : [];

  this.people = params.assignees ? params.assignees : [];

  this.boardId = parseInt(params.boardId);

  this.selectedBoard = {};
  this.boards.forEach(function(board){
      if(self.boardId === board.id){
          self.selectedBoard = board;
      }
  });

  /* Show tasks from board or all tasks */
  if(params.boardId){
    taskService.getByBoard(params.boardId).then(function(data){
        self.list = data;
    });
  }else{
    taskService.getAll().then(function(data){
      self.list = data;
    });
  }

  /* Add a task to list, sync with api */
  this.add = function(task){
      taskService.add(task).then(function(data){
          self.list.push(data);
      });
  };

  /* Remove tasks from list */
  this.remove = function(task){
    taskService.remove(task).then(function(data){
      self.list = self.list.filter(function(current) {
          if(task.id = current.id){
            return false;
          }else{
            return true;
          }
      });
    });
  };

  /* Open Overlay to create/update the task */
  var openModal = function(isNew, model){
    var modalInstance = $uibModal.open({
      templateUrl: 'views/taskOverlay.html',
      controller: 'ModalInstanceCtrl',
      size: "lg",
      resolve: {
        isNew: isNew,
        people: function() {
            return self.people;
        },
        boards: function(){
          return self.boards;
        },
        model: function(){
          return model
        }
      }
    });

    modalInstance.result.then(function(model) {
      if(model.id){
        taskService.update(model);
      }else{
        taskService.add(model);
      }
    }, function () {

    });
  };

  this.create = function(){
    openModal(true, {board: self.selectedBoard});
  };

  this.update = function(model){
    openModal(false, model);
  };
};

taskController.$inject = ['TaskService', '$stateParams', '$uibModal'];

module.controller('TasksCtrl', taskController);
