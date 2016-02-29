/*jslint plusplus: true, vars: true*/
/*global angular, window, console, btoa */
(function (undefined) {
  'use strict';

  var module = angular.module('services');

  var AuthService = function (service) {
    /**
     * Login a user
     * @param email from account
     * @param password for account
     */
    this.login = function(email, password){
      return service.createObject('token', {
        'type': 'EMAIL',
        'email': email,
        'password': password}
      ).then(function(response){
        // Save token for future requests
        service.setCurrentUser(response.data);
      });
    };

    /**
     * Logout the user
     */
    this.logout = function(){
      service.deleteObject('token', service.getCurrentUser());
      service.setCurrentUser({email: null,
        token: null});
    };

    /**
     * Register new user
     * @param email
     * @param password
     */
    this.registerAccount = function (email, password) {
      return service.createObject('user', {
        'email': email,
        'password': password
      });
    };
  };

  // Inject Dependencies
  AuthService.$inject = ['ApiService'];

  // Export
  module.service('AuthService', AuthService);
})();
