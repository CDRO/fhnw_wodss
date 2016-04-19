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
  this.members = [];

  boardService.getAll(this.list).then(function(data){
      self.list = data;
  });

  this.checkMember = function(member){
      return self.validateEmail(member.text);
  };

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
  };

  /* Regex from here http://www.w3resource.com/javascript/form/email-validation.php */
  this.validateEmail = function validateEmail(email) {
    var re = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
    return re.test(email);
  };
};

boardController.$inject = ['BoardService'];

angular.module('angularWebclientApp').controller('BoardsCtrl', boardController);

