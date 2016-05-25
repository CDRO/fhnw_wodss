(function (undefined) {
  'use strict';

  var module = angular.module('services');

  /**
   * Service to retrieve and interact with Task related data
   * @param service
   * @constructor
   */
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
       * Updates the task
       * @param task
       */
    this.update = function(task){
      var files = null;
      if(task.files){
        files = task.files;
        delete task.files;
      }
      /*task.attachments = task.attachments.map(function(attachment){
        attachment['task'] = {id: task.id};
        return attachment;
      });*/

      return service.updateObject('task', task, files);
    };

    /**
     * Removes the Task
     * @param board task with id specified
     */
    this.remove = function (task) {
        service.deleteObject('task', task);
    };

  };

  // Inject Dependencies
  TaskService.$inject = ['ApiService'];

  // Export
  module.service('TaskService', TaskService);
})();
