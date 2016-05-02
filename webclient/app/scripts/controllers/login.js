'use strict';

/**
 * @ngdoc function
 * @name angularWebclientApp.controller:LoginCtrl
 * @description
 * # LoginCtrl
 * Controller of the angularWebclientApp
 */
var loginController = function(auth, $scope, $state) {
  var self = this;

  $scope.loginNow = function(){
    self.loginError = false;
    auth.login($scope.email, $scope.password).then(function(response){
        $state.go('boards');
        self.loginError = false;
    }, function(error){
        self.loginError = true;
    });

  };
};

loginController.$inject = ['AuthService', '$scope', '$state'];

angular.module('angularWebclientApp').controller('LoginCtrl', loginController);
