/**
 * Creates a Session Interceptor
 * Add a header field to http requests, when user is logged in
 */
(function (undefined) {
  'use strict';
  var module = angular.module('services');

  var SessionInterceptor = function(ConfigService){
      return {
        request: function(config){
            if(ConfigService.isLoggedIn()){
                config.headers['x-session-token'] = ConfigService.getCurrentUser().token;
            }
          return config;
        }
      }
  };

  SessionInterceptor.$inject = ['ConfigService'];

  // Export
  module.service('sessionInterceptor', SessionInterceptor);

  module.config(['$httpProvider', function($httpProvider){
    $httpProvider.interceptors.push('sessionInterceptor');
  }])
})();
