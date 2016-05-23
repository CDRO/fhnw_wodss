'use strict';

/**
 * @ngdoc function
 * @name angularWebclientApp.controller:RegisterCtrl
 * @description
 * # RegisterCtrl
 * Controller of the angularWebclientApp
 */
var module = angular.module('angularWebclientApp');

var RegisterCtrl = function ($scope, authService, $state, alertService, $translate) {
  var self = this;
  this.model = {};

  this.register = function(){
      authService.registerAccount(self.email, self.password).then(function(response){
        alertService.addAlert('warning', 'register.validationRequired');
        $state.go('login');
      }, function(error){
          if(error.status == 409){
              self.errorMessage = 'register.alreadyRegistered';
          }else{
              self.errorMessage = 'register.errorHappened';
          }

      });
  }
};

RegisterCtrl.$inject = ['$scope', 'AuthService', '$state', 'AlertService', '$translate'];

module.controller('RegisterCtrl', RegisterCtrl);



