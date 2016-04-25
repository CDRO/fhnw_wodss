(function (undefined) {
  'use strict';

  /**
   * Centralize the configuration of the applications
   * Store the authentication token
   */
  var module = angular.module('services', []);

  var Configuration = function(){

    // Declare the backend, with an ending slash
    this.baseUrl = 'http://localhost:8080/';
    //this.baseUrl = 'http://server1070.cs.technik.fhnw.ch:8080/';

    // Represents the logged in user
    var currentUser = {
      email: null,
      token: null
    };

    this.getCurrentUser = function() {
      return currentUser;
    };

    this.isLoggedIn = function(){
      return (currentUser.email !== null && currentUser.token !== null);
    };

    this.setCurrentUser = function (newCurrentUser) {
      currentUser = angular.copy(newCurrentUser);
    };
  };

  module.service('ConfigService', Configuration);
})();
