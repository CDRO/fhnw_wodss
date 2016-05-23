/**
 * Checks if user is logged in (401 response) for this operation
 * Log problem and redirect to login
 */
(function (undefined) {
  'use strict';
  var module = angular.module('services');

  var NotAuthorizedInterceptor = function($q, $log, $injector){
    return {
      responseError: function(response) {
        if(response.status === 401){
            $injector.get('$state').transitionTo('login');
            return $q.reject(response);
        }
        else{
          return $q.reject(response);
        }
      }
    }
  };

  NotAuthorizedInterceptor.$inject = ['$q', '$log','$injector'];

  // Export
  module.service('notAuthorizedInterceptor', NotAuthorizedInterceptor);

  module.config(['$httpProvider', function($httpProvider){
    $httpProvider.interceptors.push('notAuthorizedInterceptor');
  }])
})();
