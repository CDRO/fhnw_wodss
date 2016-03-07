'use strict';

/**
 * @ngdoc function
 * @name angularWebclientApp.controller:TasksCtrl
 * @description
 * # TasksCtrl
 * Controller of the angularWebclientApp
 */
var module = angular.module('angularWebclientApp');

var taskController = function(taskService, params){
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


};

taskController.$inject = ['TaskService', '$stateParams'];

module.controller('TasksCtrl', taskController);
