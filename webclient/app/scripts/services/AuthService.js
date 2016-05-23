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
      return service.createObject('token', {
        'email': email,
        'password': password
      }).then(function(response){
        // Save token for future requests
        config.setCurrentUser({
            user: response.data.user,
            id: response.data.id,
            token: response.data.id,
            email: response.data.user.email,
            timeToLive: response.data.timeToLive
        });
        return true;
      },
      function(error){
          config.setCurrentUser({user: null, token: null});
          return error;
      });
    };

    /**
     * Logout the user
     */
    this.logout = function(){
      return service.deleteObject('token', config.getCurrentToken()).then(function(response){
        config.setCurrentUser({user: null, id: null, token: null, email: null, timeToLive: null});
      }, function(error){
        $log.error("Log out was not possible cause %o", error);
      });
    };

      /**
       * Request an email for password reset
       * @param model
       */
    this.resetPasswordRequest = function(model){
      return service.createObject('resetcode', model);
    };

    /**
     * Resets the Password
     * @param model
     */
    this.resetPassword = function(model){
      return service.updateObject('user', model, null, 'logindata');
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
     * Update existing user
     * @param user User Object
     */
    this.updateProfile = function (user) {
      return service.updateObject('user', user);
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
      }, null, 'logindata');
    }
  };

  // Inject Dependencies
  AuthService.$inject = ['ApiService', 'ConfigService', '$log'];

  // Export
  module.service('AuthService', AuthService);
})();
