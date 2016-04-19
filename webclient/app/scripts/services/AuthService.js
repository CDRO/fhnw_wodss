/*jslint plusplus: true, vars: true*/
/*global angular, window, console, btoa */
(function (undefined) {
  'use strict';

  var module = angular.module('services');

  var AuthService = function (service, config, $log) {

    /**
     * Login a user
     * @param email from account
     * @param password for account
     */
    this.login = function(email, password){
      return service.createObject('login', {
        'email': email,
        'password': password
      }).then(function(response){
        // Save token for future requests
        config.setCurrentUser({
            email: email,
            token: response.data.id,
            timeToLive: response.data.timeToLive
        });
      },
      function(error){
        config.setCurrentUser({email: null, token: null});
      });
    };

    /**
     * Logout the user
     */
    this.logout = function(){
      service.deleteObject('logout', service.getCurrentUser()).then(function(response){
        config.setCurrentUser({email: null, token: null});
      }, function(error){
        $log.error("Log out was not possible cause %s", response);
      });

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

    /**
     * Validate User
     * @param id of the user
     * @param validationCode from email
     */
    this.validateAccount = function(id, validationCode){
      return service.updateObject('user', {
        id: id,
        validationCode: validationCode
      });
    }
  };

  // Inject Dependencies
  AuthService.$inject = ['ApiService', 'ConfigService', '$log'];

  // Export
  module.service('AuthService', AuthService);
})();
