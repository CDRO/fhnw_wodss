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
    //this.imageBaseUrl = this.baseUrl + 'image/';

    // Represents the logged in user
    var currentUser = {
      email: "d.augsburger@gmx.ch",
      token: "d643053d-4759-44dc-b7d3-144f6b72a3d9"
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
