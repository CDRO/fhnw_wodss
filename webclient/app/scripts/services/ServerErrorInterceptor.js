/**
 * Checks if error happens
 * Log problem and redirect to login
 */
(function (undefined) {
  'use strict';
  var module = angular.module('services');

  var ServerErrorInterceptor = function($q, $log){
    return {
      response: function(response){
            return response;
      },
      responseError: function error(response) {
            if(response.status !== 401){
              //$state.go('error');
              $log.error("Response status is %s with text %s for url %s", response.status, response.statusText, response.config.url);
              return $q.reject(response);
            }else{
              $q.reject(response);
            }
      }
    };
  };

  ServerErrorInterceptor.$inject = ['$q', '$log'];

  // Export
  module.service('serverErrorInterceptor', ServerErrorInterceptor);

  module.config(['$httpProvider', function($httpProvider){
    $httpProvider.interceptors.push('serverErrorInterceptor');
  }])
})();
