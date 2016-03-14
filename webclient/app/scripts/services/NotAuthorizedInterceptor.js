/**
 * Checks if user is logged in (401 response) for this operation
 * Log problem and redirect to login
 */
(function (undefined) {
  'use strict';
  var module = angular.module('services');

  var NotAuthorizedInterceptor = function($q, $log){
    return {
      responseError: function(response) {
        if(response.status === 401){
          //$state.go('login');
          $log("Not logged in yet");
          return response;
        }
        else{
          return $q.reject(response);
        }
      }
    }
  };

  NotAuthorizedInterceptor.$inject = ['$q', '$log'];

  // Export
  module.service('notAuthorizedInterceptor', NotAuthorizedInterceptor);

  module.config(['$httpProvider', function($httpProvider){
    $httpProvider.interceptors.push('notAuthorizedInterceptor');
  }])
})();
