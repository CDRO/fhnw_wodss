/*jslint plusplus: true, vars: true*/
/*global angular, window, console, btoa */
(function (undefined) {
  'use strict';

  var module = angular.module('services');

  var BoardService = function (service) {

    /**
     * Get the boards of the logged in user
     */
    this.getAll = function(){
        return service.query('boards').then(function(response){
            return response.data;
        });
    };

    /**
     * Save the specified Board
     */
    this.add = function(board){
      return service.createObject('board', board);
    };

      /**
       * Updates the board
       * @param board
       */
    this.update = function(board){
      return service.updateObject('board', board);
    };

    /**
     * Removes the Board
     * @param board board with id specified
     */
    this.remove = function (board) {
        service.deleteObject('board', board);
    };


  };

  // Inject Dependencies
  BoardService.$inject = ['ApiService'];

  // Export
  module.service('BoardService', BoardService);
})();
