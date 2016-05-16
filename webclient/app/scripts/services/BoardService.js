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
            // Object transformation for Tag Input
            response.data.map(function(board){
                var board = board;
                for(var i=0; i<board.users.length; i++){
                  board.users[i].text = board.users[i].email;
                }
                return board;
            });

            return response.data;
        });
    };

    /**
     * Save the specified Board
     */
    this.add = function(board){
      // Object transformation for Tag Input
      if(board.users){
        for(var i=0; i<board.users.length; i++){
          board.users[i].email = board.users[i].text;
          delete board.users[i].text;
        }
      }

      return service.createObject('board', board).then(function(response){
        return response.data;
      });
    };

      /**
       * Updates the board
       * @param board
       */
    this.update = function(board){
      // Object transformation for Tag Input
      if(board.users){
        for(var i=0; i<board.users.length; i++){
          board.users[i].email = board.users[i].text;
          delete board.users[i].text;
        }
      }

      return service.updateObject('board', board);
    };

    /**
     * Removes the Board
     * @param board board with id specified
     */
    this.remove = function (board) {
        return service.deleteObject('board', board);
    };


  };

  // Inject Dependencies
  BoardService.$inject = ['ApiService'];

  // Export
  module.service('BoardService', BoardService);
})();
