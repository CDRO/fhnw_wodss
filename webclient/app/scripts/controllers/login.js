'use strict';

/**
 * @ngdoc function
 * @name angularWebclientApp.controller:LoginCtrl
 * @description
 * # LoginCtrl
 * Controller of the angularWebclientApp
 */
var loginController = function(auth, $scope) {
  var vm = this;
  vm.dataLoading = false;

  $scope.loginNow = function(){
    auth.login($scope.email, $scope.password);

    // Pseudo Implementation, put a User Service here
    vm.dataLoading = true;
    //console.log("Going to login");
    /*setTimeout(function(){
     vm.dataLoading(false);
     }, 3000);*/
  };
};

loginController.$inject = ['AuthService', '$scope'];

angular.module('angularWebclientApp').controller('LoginCtrl', loginController);
