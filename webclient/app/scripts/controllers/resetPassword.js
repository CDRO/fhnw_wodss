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
  this.model = {};

  this.model.resetToken = params.resetToken;

  this.requestReset = function(email) {
    authService.requestResetToken(email).then(function (response) {
      // Activation Link

    });
  };

  this.resetPassword = function(model) {
      authService.resetPassword(model).then(function(response){
          $state.go('login');
      });
  };

};

ResetPasswordCtrl.$inject = ['$scope', 'AuthService', '$stateParams', '$state'];

module.controller('ResetPasswordCtrl', ResetPasswordCtrl);
