/*jslint plusplus: true, vars: true*/
/*global angular, window, console, btoa */
(function (undefined) {
  'use strict';

  var module = angular.module('services');

  var TaskService = function (service) {

    var dummyData = [{
      id: 23,
      title: "Mein Task",
      state: "Erledigt"
    },{
      id: 25,
      title: "Zweiter Task",
      state: "Offen"
    },
    {
      id: 27,
      title: "Task",
      state: "In Bearbeitung"
    }

    ];

    /**
     * Get the tasks of the logged in user
     */
    this.getAll = function(){
      return service.query('tasks');
    };

    this.getByBoard = function(boardId){
        return service.query('tasks', {boardId: boardId});
    };

    /**
     * Save the specified Board
     */
    this.add = function(board){
      //return service.createObject('task', board);
      dummyData.push(board);
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
