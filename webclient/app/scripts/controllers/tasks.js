'use strict';

/**
 * @ngdoc function
 * @name angularWebclientApp.controller:TasksCtrl
 * @description
 * # TasksCtrl
 * Controller of the angularWebclientApp
 */
var module = angular.module('angularWebclientApp');

var taskController = function(taskService, attachmentService, params, $uibModal, $scope){
  var self = this;
  this.todoList = [];
  this.doingList = [];
  this.doneList = [];

  this.attachmentUrl = attachmentService.getPath();

  this.boards = params.boards ? params.boards : [];

  this.assignees = params.assignees ? params.assignees : [];
  if(self.assignees.length === 0){
      var assignees = [];
      angular.forEach(self.boards, function(value, key){
          angular.forEach(value.users, function(user){
            assignees.push(user);
          });
      });

      // Remove duplicates
      self.assignees = assignees.filter(function(item, pos) {
          return assignees.indexOf(item) == pos;
      });
  }

  this.boardId = parseInt(params.boardId);

  this.selectedBoard = {};
  this.boards.forEach(function(board){
      if(self.boardId === board.id){
          self.selectedBoard = board;
      }
  });

  this.switchTasks = function(models){
    for(var i=0; i<models.length; i++){
      switch(models[i].state) {
        case 'TODO':
          self.todoList.push(models[i]);
          break;
        case 'DOING':
          self.doingList.push(models[i]);
          break;
        case 'DONE':
          self.doneList.push(models[i]);
          break;
        default:
          console.log("Unknown state of todo" + data[i].state);
      }
    }
  };

  this.updateList = function(list, state){
    for(var i=0; i<list.length; i++){
       var task = list[i];
        if(task.state !== state){
            if(state==='DONE'){
              task.doneDate = Date.now();
            }
            task.state = state;
            self.update(task);
        }
    }
  };

  $scope.updateTodoList = function(event, ui){
      self.updateList(self.todoList, 'TODO');
  };


  $scope.updateDoingList = function(event, ui){
      self.updateList(self.doingList, 'DOING');
  };

  $scope.updateDoneList = function(event, ui){
      self.updateList(self.doneList, 'DONE');
  };

  $scope.filterAssignee = function(task){
      if($scope.search && $scope.search.assignee){
        return task.assignee.id === $scope.search.assignee.id;
      }else{
        return true;
      }
  };

  /* Show tasks from board or all tasks */
  this.synchronize = function(){
    self.todoList = [];
    self.doneList = [];
    self.doingList = [];
    if(params.boardId){
      taskService.getByBoard(params.boardId).then(function(data){
        self.switchTasks(data);
      });
    }else{
      taskService.getAll().then(function(data){
        self.switchTasks(data);
      });
    }
  };
  self.synchronize();

  /* Add a task to list, sync with api */
  this.add = function(task){
      taskService.add(task).then(function(data){
          self.synchronize();
      });
  };

  this.update = function(task){
      for(var i=0; i<task.attachments.length; i++){
          if(task.attachments[i] && task.attachments[i].toDelete === true){
              delete task.attachments[i];
          }
      }
      taskService.update(task).then(function(data){
          self.synchronize();
      });
  };

  /* Remove tasks from list */
  this.remove = function(task){
    /* Confirmation Dialog */
    var modalInstance = $uibModal.open({
      templateUrl: 'views/confirmationOverlay.html',
      controller: 'ModalConfirmationCtrl',
      size: 'sm',
      resolve: {
          model: function(){
            return task;
          },
          confirmation: function(){
            return {text: 'Task ' + task.description};
          }
        }
      });

      modalInstance.result.then(function(model) {
        taskService.remove(task).then(function(data){
          self.synchronize();
        });
      }, function () {
        // Dismissed
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
        assignees: function() {
            return self.assignees;
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
        self.update(model);
      }else{
        self.add(model);
      }
    }, function () {

    });
  };

  this.create = function(){
    openModal(true, {board: self.selectedBoard});
  };

  this.updateModal = function(model){
    openModal(false, model);
  };
};

taskController.$inject = ['TaskService', 'AttachmentService', '$stateParams', '$uibModal', '$scope'];

module.controller('TasksCtrl', taskController);
