(function (undefined) {
  'use strict';

  /**
   * Centralize the configuration of the applications
   */
  var module = angular.module('services', []);

  var Configuration = function(){

    // Declare the backend, with an ending slash
    this.baseUrl = 'http://localhost:9000/';
    //this.imageBaseUrl = this.baseUrl + 'image/';

    // More config if needed

  };

  module.service('ConfigService', Configuration);

})();
