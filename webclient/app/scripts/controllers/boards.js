'use strict';

/**
 * @ngdoc function
 * @name angularWebclientApp.controller:BoardsCtrl
 * @description
 * # BoardsCtrl
 * Controller of the angularWebclientApp
 */

var boardController = function(boardService, $scope) {

  this.list = boardService.getBoards();

  this.addBoard = function(){
    boardService.addBoard({title: $scope.title});
  };

  this.updateBoard = function(board){
    boardService.updateBoard(board);
  };

  this.removeBoard = function(board){
    boardService.removeBoard(board);
  }
};

boardController.$inject = ['BoardService', '$scope'];

angular.module('angularWebclientApp').controller('BoardsCtrl', boardController);

