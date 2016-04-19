'use strict';

/**
 * @ngdoc function
 * @name angularWebclientApp.controller:RegisterCtrl
 * @description Validate the given user id and validationCode with API
 * # ValidationCtrl
 * Controller of the angularWebclientApp
 */
var module = angular.module('angularWebclientApp');

var ValidationCtrl = function ($scope, authService, params, $state) {
  var self = this;
  this.model = {};
  this.validated = false;
  this.empty = false;
  this.error = false;

  var validate = function(id, validationCode){
    authService.validateAccount(id, validationCode).then(function(response){
        self.validated = true;
        self.error = false;
        // Login redirect
        $state.go('login');
    }, function(error){
        self.validated = false;
        self.error = true;
    })
  };
  var validationCode = params.validationCode;
  var id = params.id;
  if(validationCode){
      validate(id, validationCode);
  }else{
      self.empty = true;
  }

};

ValidationCtrl.$inject = ['$scope', 'AuthService', '$stateParams', '$state'];

module.controller('ValidationCtrl', ValidationCtrl);
