'use strict';

/**
 * @ngdoc function
 * @name angularWebclientApp.controller:RegisterCtrl
 * @description
 * # RegisterCtrl
 * Controller of the angularWebclientApp
 */
var module = angular.module('angularWebclientApp');

var RegisterCtrl = function ($scope, authService, $state) {
  var vm = this;
  this.model = {};

  this.register = function(){
      authService.registerAccount(vm.email, vm.password).then(function(response){
        $state.go('login');
      }, function(error){
          vm.errorMessage = error.message;
      });
  }
};

RegisterCtrl.$inject = ['$scope', 'AuthService', '$state'];

module.controller('RegisterCtrl', RegisterCtrl);



