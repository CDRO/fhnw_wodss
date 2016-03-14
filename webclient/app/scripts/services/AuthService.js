/*jslint plusplus: true, vars: true*/
/*global angular, window, console, btoa */
(function (undefined) {
  'use strict';

  var module = angular.module('services');

  var AuthService = function (service, config) {

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
        config.setCurrentUser(response.data);
      });
    };

    /**
     * Logout the user
     */
    this.logout = function(){
      service.deleteObject('token', service.getCurrentUser());
      config.setCurrentUser({email: null,
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
      // Need to be verified first
    };
  };

  // Inject Dependencies
  AuthService.$inject = ['ApiService', 'ConfigService'];

  // Export
  module.service('AuthService', AuthService);
})();
