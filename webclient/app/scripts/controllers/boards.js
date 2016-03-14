'use strict';

/**
 * @ngdoc function
 * @name angularWebclientApp.controller:BoardsCtrl
 * @description
 * # BoardsCtrl
 * Controller of the angularWebclientApp
 */

var boardController = function(boardService, $scope) {
  var self = this;
  this.list = [];

  boardService.getAll(this.list).then(function(data){
      self.list = data;
  });

  this.add = function(){
    boardService.add({title: $scope.title});
  };

  this.update = function(board){
    boardService.update(board);
  };

  this.remove = function(board){
    boardService.remove(board);
  }
};

boardController.$inject = ['BoardService', '$scope'];

angular.module('angularWebclientApp').controller('BoardsCtrl', boardController);

