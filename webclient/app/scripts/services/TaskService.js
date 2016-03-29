/*jslint plusplus: true, vars: true*/
/*global angular, window, console, btoa */
(function (undefined) {
  'use strict';

  var module = angular.module('services');

  var TaskService = function (service) {

    /**
     * Get the tasks of the logged in user
     */
    this.getAll = function(){
      return service.query('tasks').then(function(response){
        return response.data;
      });
    };

    /**
     * Get the tasks from specified Board
     * @param boardId
     * @returns Tasks
       */
    this.getByBoard = function(boardId){
        return service.query('tasks', {boardId: boardId}).then(function(response){
          return response.data;
        });
    };

    /**
     * Save the specified Task
     */
    this.add = function(task){
      var files = null;
      if(task.files){
        files = task.files;
        delete task.files;
      }
      return service.createObject('task', task, files).then(function(response){
          return response.data;
      });
    };

      /**
       * Updates the board
       * @param board
       */
    this.update = function(board){
      return service.updateObject('task', board);
    };

    /**
     * Removes the Board
     * @param board board with id specified
     */
    this.remove = function (board) {
        service.deleteObject('task', board);
    };

  };

  // Inject Dependencies
  TaskService.$inject = ['ApiService'];

  // Export
  module.service('TaskService', TaskService);
})();
