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

  this.boards = [{id: 2, title:"Test"}, {id: 3, title:"Board2"}];

  this.people = [{id: 3, name: "Person1"}, {id: 4, name:"Person2"}];

  this.selectedBoard = params.boardId;
  /* Select the selected Board */
  this.boards.forEach(function(board){
      if(board.id === self.selectedBoard){
          board.selected = true;
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
    var selectedBoard = {};
    if(self.selectedBoard){
      selectedBoard.boardId = self.selectedBoard;
    }

    openModal(true, selectedBoard);
  };

  this.update = function(model){
    openModal(false, model);
  };
};

taskController.$inject = ['TaskService', '$stateParams', '$uibModal'];

module.controller('TasksCtrl', taskController);
