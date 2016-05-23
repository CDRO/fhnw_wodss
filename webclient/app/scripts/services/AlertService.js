(function (undefined) {
  'use strict';

  var module = angular.module('services');

  var AlertService = function($rootScope) {

    var service = {};
    /**
     * Saves the bootstrap alerts
     * @type {Array} of Messageb objects {type: ..., msg: ...}
       */
    $rootScope.alerts = [];

    /**
     * Removes all alerts
     */
    service.flush = function(){
        $rootScope.alerts = [];
    };

    /**
     * Add alert to rootscope
     * @param type
     * @param msg
       */
    service.addAlert = function (type, msg) {
      $rootScope.alerts.push({'type': type, 'msg': msg});
    };

    /**
     * Remove alert from rootscope
     */
    service.closeAlert = function (index) {
      $rootScope.alerts.splice(index, 1);
    };

    return service;
  };

  AlertService.inject = ['$rootScope'];

  // Export
  module.factory('AlertService', AlertService);

})();
