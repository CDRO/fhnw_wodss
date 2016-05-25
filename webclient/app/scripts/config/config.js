(function (undefined) {
  'use strict';

  /**
   * Centralize the configuration of the applications
   * Store the authentication token
   */
  var module = angular.module('services', []);

  var Configuration = function($window){
    var self = this;
    this.loadStorage = 0;

    // Declare the backend, with an ending slash
    this.baseUrl = 'http://localhost:8080/';
    // this.baseUrl = 'https://www.cs.technik.fhnw.ch/wodss5/server/';

    // Represents the logged in user
    this.currentUser = {
      user: null,
      token: null
    };

    this.getCurrentUser = function() {
      return self.currentUser.user;
    };

    this.getCurrentToken = function(){
      return self.currentUser;
    };

    this.isLoggedIn = function(){
      // Try to load from local storage
      if(self.loadStorage===0){
           //self.loadFromStorage();
      }
      return (self.currentUser.user !== null && self.currentUser.token !== null);
    };

    this.loadFromStorage = function(){
        self.loadStorage = self.loadStorage+1;
        var user = JSON.parse($window.sessionStorage.user);
        if(user.token){
          self.setCurrentUser(user);
        }
    };

    this.saveToStorage = function(user){
      $window.sessionStorage.user = JSON.stringify(user);
    };

    this.setCurrentUser = function (newCurrentUser) {
        self.currentUser = angular.copy(newCurrentUser);
        self.saveToStorage(self.currentUser);
    };
  };

  Configuration.inject = ['$window'];

  module.service('ConfigService', Configuration);
})();
