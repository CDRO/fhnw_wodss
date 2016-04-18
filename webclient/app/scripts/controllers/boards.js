'use strict';

/**
 * @ngdoc function
 * @name angularWebclientApp.controller:BoardsCtrl
 * @description
 * # BoardsCtrl
 * Controller of the angularWebclientApp
 */

var boardController = function(boardService) {
  var self = this;
  this.list = [];
  this.assignees = [];

  boardService.getAll(this.list).then(function(data){
      self.list = data;
  });

  this.add = function(){
    boardService.add({title: self.title}).then(function(data){
        self.list.push(data);
    });
  };

  this.update = function(board){
    boardService.update(board);
  };

  this.remove = function(board){
    boardService.remove(board);
  }
};

boardController.$inject = ['BoardService'];

angular.module('angularWebclientApp').controller('BoardsCtrl', boardController);

