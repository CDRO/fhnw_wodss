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
    //this.baseUrl = 'https://server1070.cs.technik.fhnw.ch/wodss5/';

    // Represents the logged in user
    var currentUser = {
      user: null,
      token: null
    };

    this.getCurrentUser = function() {
      return currentUser.user;
    };

    this.getCurrentToken = function(){
      return currentUser;
    };

    this.isLoggedIn = function(){
      return (currentUser.user !== null && currentUser.token !== null);
    };

    this.setCurrentUser = function (newCurrentUser) {
      currentUser = angular.copy(newCurrentUser);
    };
  };

  module.service('ConfigService', Configuration);
})();
