'use strict';

/**
 * @ngdoc function
 * @name angularWebclientApp.controller:BoardsCtrl
 * @description
 * # BoardsCtrl
 * Controller of the angularWebclientApp
 */

var boardController = function(boardService, $uibModal) {
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
    var members = self.members.map(function(member){
        return {email: member.text, name: member.text};
    });
    boardService.add({title: self.title, users: members}).then(function(data){
        self.list.push(data);
    });
  };

  this.update = function(board){
    boardService.update(board);
  };

  /* Confirmation Dialog */
  this.confirm = function(model){
    var board = model;
    var modalInstance = $uibModal.open({
      templateUrl: 'views/confirmationOverlay.html',
      controller: 'ModalConfirmationCtrl',
      size: "sm",
      resolve: {
        model: function(){
          return model
        },
        confirmation: function(){
          return {text: "Board " + model.title};
        }
      }
    });

    modalInstance.result.then(function(model) {
        self.remove(model);
    }, function () {
        // Dismissed
    });

  };

  /* Removes the board */
  this.remove = function(board){
    boardService.remove(board);
  };

  /* Regex from here http://www.w3resource.com/javascript/form/email-validation.php */
  this.validateEmail = function validateEmail(email) {
    var re = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
    return re.test(email);
  };
};

boardController.$inject = ['BoardService', '$uibModal'];

angular.module('angularWebclientApp').controller('BoardsCtrl', boardController);

