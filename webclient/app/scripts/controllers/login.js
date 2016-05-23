'use strict';

/**
 * @ngdoc function
 * @name angularWebclientApp.controller:LoginCtrl
 * @description
 * # LoginCtrl
 * Controller of the angularWebclientApp
 */
var loginController = function(auth, $scope, $state, alertService) {
  var self = this;

  $scope.loginNow = function(){
    self.loginError = false;
    auth.login($scope.email, $scope.password).then(function(error){
        if(error.status){
          self.loginError = true;
        }else{
          alertService.flush();
          $state.go('boards');
          self.loginError = false;
        }

    });

  };
};

loginController.$inject = ['AuthService', '$scope', '$state', 'AlertService'];

angular.module('angularWebclientApp').controller('LoginCtrl', loginController);
