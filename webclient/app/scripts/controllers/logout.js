'use strict';

/**
 * @ngdoc function
 * @name angularWebclientApp.controller:LogoutCtrl
 * @description
 * # LogoutCtrl
 * Controller of the angularWebclientApp
 */
var logoutController = function(auth, $state) {

  auth.logout();

  $state.go('login');

};

logoutController.$inject = ['AuthService', '$state'];

angular.module('angularWebclientApp').controller('LogoutCtrl', logoutController);
