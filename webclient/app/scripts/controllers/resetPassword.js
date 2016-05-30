'use strict';

/**
 * @ngdoc function
 * @name angularWebclientApp.controller:RegisterCtrl
 * @description Reset Password functionality
 * # ValidationCtrl
 * Controller of the angularWebclientApp
 */
var module = angular.module('angularWebclientApp');

var ResetPasswordCtrl = function ($scope, authService, params, $state, alertService) {
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
        alertService.flush();
        alertService.addAlert('success', 'reset.emailSent');
    }, function(error){
        alertService.flush();
        alertService.addAlert('warning', 'reset.notSent');
    });
  };

  this.resetPassword = function() {
      authService.resetPassword(self.model).then(function(response){
          $state.go('login');
      }, function(error){
          if(error.status === 410){
              self.errorGone = true;
          }else{
              self.error = true;
          }
      });
  };

};

ResetPasswordCtrl.$inject = ['$scope', 'AuthService', '$stateParams', '$state', 'AlertService'];

module.controller('ResetPasswordCtrl', ResetPasswordCtrl);
