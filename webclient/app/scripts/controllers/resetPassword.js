'use strict';

/**
 * @ngdoc function
 * @name angularWebclientApp.controller:RegisterCtrl
 * @description Reset Password functionality
 * # ValidationCtrl
 * Controller of the angularWebclientApp
 */
var module = angular.module('angularWebclientApp');

var ResetPasswordCtrl = function ($scope, authService, params, $state) {
  var self = this;

  // Handle Password Reset
  this.resetCode = params.resetCode;

  this.model = {
    resetCode: params.resetCode,
    id: params.id
  };


  this.requestReset = function() {
    authService.resetPasswordRequest(self.model).then(function (response) {
        // Activation Link sent
    });
  };

  this.resetPassword = function() {
      authService.resetPassword(self.model).then(function(response){
          $state.go('login');
      }, function(error){
          if(error.status === 410){
              self.errorGone = true;
          }else{
            reset.error = true;
          }
      });
  };

};

ResetPasswordCtrl.$inject = ['$scope', 'AuthService', '$stateParams', '$state'];

module.controller('ResetPasswordCtrl', ResetPasswordCtrl);
