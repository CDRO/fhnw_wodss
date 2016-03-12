/*jslint plusplus: true, vars: true*/
/*global angular, window, console, btoa */

(function (undefined) {
  'use strict';

  var module = angular.module('services');

  var ApiService = function (configService, $http) {

    /**
     * Receive some data from backend
     * @param collection/resource i. e. boards or tasks
     * @param parameters to specify collection
     * @returns list of collection
     */
    this.query = function (collection, parameters) {
      parameters = parameters || {};
      var uri = configService.baseUrl + collection;
      return $http(
        {method: "GET", url: uri, params: parameters, cache: false}
      );
    };

    /**
     * Query the specified object
     * @param collection i. e. board
     * @param id
     * @param parameters
     * @returns collection item
     */
    this.queryById = function (collection, id, parameters) {
      parameters = parameters || {};
      var uri = configService.baseUrl + collection + '/' + id;
      return $http({method: "GET", url: uri, params: parameters, cache: false});
    };

    /**
     * Create / Save Object
     * @param collection
     * @param object
     * @returns true
     */
    this.createObject = function (collection, object) {
      var uri = configService.baseUrl + collection;
      return $http({method: "POST", url: uri, data:
        JSON.stringify(object), cache: false});
    };

    /**
     * Update the Object
     * @param collection for example friends, products
     * @param object
     * @returns true/false
     */
    this.updateObject = function (collection, object) {
      var uri = configService.baseUrl + collection + "/" + object.id;
      return $http({method: "PUT", url: uri, data:
        JSON.stringify(object), cache: false});
    };

    /**
     * Delete the specified object
     * @param collection
     * @param object
     * @returns {*}
     */
    this.deleteObject = function (collection, object) {
      var uri = configService.baseUrl + collection + "/" +object.id;
      /*var headers =  {
        'Authorization': helpers.encodeBasicAuth(),
        'Content-Type': 'application/json'
      };*/
      return $http({method: "DELETE", url: uri, cache: false, /*headers: headers,*/ data: JSON.stringify(object)});
    };

    /**
     * Add Basic Authentication to the Header
     * @returns {string}
     */
    /*helpers.encodeBasicAuth = function () {
      return 'Basic ' + btoa(currentUser.email + ':' + currentUser.token);
    };*/
  };

  ApiService.$inject = ['ConfigService', '$http'];

  module.service('ApiService', ApiService);
})();
