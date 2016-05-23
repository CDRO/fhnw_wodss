/**
 * Checks if login error happened, and redirect to login page
 * Log problem and redirect to login
 */
(function (undefined) {
  'use strict';
  var module = angular.module('services');

  var ServerErrorInterceptor = function($q, $log, $injector){
    return {
      response: function(response){
            return response;
      },
      responseError: function error(response) {
          if(response.status === 401){
            $injector.get('$state').transitionTo('login');
            return $q.reject(response);
          }else {
            return $q.reject(response);
          }
      }
    };
  };

  ServerErrorInterceptor.$inject = ['$q', '$log', '$injector'];

  // Export
  module.service('serverErrorInterceptor', ServerErrorInterceptor);

  module.config(['$httpProvider', function($httpProvider){
    $httpProvider.interceptors.push('serverErrorInterceptor');
  }])
})();
