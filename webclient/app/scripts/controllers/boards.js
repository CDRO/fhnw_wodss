'use strict';

/**
 * @ngdoc function
 * @name angularWebclientApp.controller:BoardsCtrl
 * @description
 * # BoardsCtrl
 * Controller of the angularWebclientApp
 */

var boardController = function($scope, boardService, configService, $uibModal) {
  var self = this;
  this.list = [];
  this.assignees = [];
  this.currentUser = configService.getCurrentUser();

  this.synchronize = function(){
    boardService.getAll(this.list).then(function(data){
      self.list = data;
    });
  };

  self.synchronize();

  this.add = function(model){
    boardService.add(model).then(function(){
        self.synchronize();
    });
  };

  this.update = function(model){
    boardService.update(model).then(function(){
        self.synchronize();
    });
  };

  /* Confirmation Dialog */
  this.confirm = function(model){
    var modalInstance = $uibModal.open({
      templateUrl: 'views/confirmationOverlay.html',
      controller: 'ModalConfirmationCtrl',
      size: 'sm',
      resolve: {
        model: function(){
          return model;
        },
        confirmation: function(){
          return {text: 'Board ' + model.title};
        }
      }
    });

    modalInstance.result.then(function(model) {
        self.remove(model);
    }, function () {
        // Dismissed
    });
  };

  /* Open Overlay to create/update the board */
  var openModal = function(isNew, model){
    if(!isNew){
      model.users = model.users.filter(function(user){
        if(user.id === model.owner.id){
          return false;
        }else{
          return true;
        }
      });
    }

    var modalInstance = $uibModal.open({
      templateUrl: 'views/boardOverlay.html',
      controller: 'ModalInstanceCtrl',
      size: 'lg',
      resolve: {
        isNew: isNew,
        assignees: function() {
          return [];
        },
        boards: function(){
          return [];
        },
        model: function(){
          return model;
        }
      }
    });

    modalInstance.result.then(function(model) {
      if(model.id){
        self.update(model);
      }else{
        self.add(model);
      }
    }, function () {

    });
  };

  this.createModal = function(){
    openModal(true, {});
  };

  this.updateModal = function(model){
    openModal(false, model);
  };

  /* Removes the board */
  this.remove = function(board){
    boardService.remove(board).then(function(){
        self.synchronize();
    });
  };


};

boardController.$inject = ['$scope', 'BoardService', 'ConfigService', '$uibModal'];

angular.module('angularWebclientApp').controller('BoardsCtrl', boardController);

