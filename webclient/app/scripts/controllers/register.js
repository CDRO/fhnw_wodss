'use strict';

/**
 * @ngdoc function
 * @name angularWebclientApp.controller:RegisterCtrl
 * @description
 * # RegisterCtrl
 * Controller of the angularWebclientApp
 */

var RegisterCtrl = function ($scope, authService, $state, alertService) {
  var self = this;
  self.model = {};

  self.register = function(){
      authService.registerAccount(self.email, self.password).then(function(){
        alertService.addAlert('warning', 'register.validationRequired');
        $state.go('login');
      }, function(error){
          if(error.status === 409){
              self.errorMessage = 'register.alreadyRegistered';
          }else{
              self.errorMessage = 'register.errorHappened';
          }

      });
  };
};

RegisterCtrl.$inject = ['$scope', 'AuthService', '$state', 'AlertService'];

angular.module('angularWebclientApp').controller('RegisterCtrl', RegisterCtrl);
