/*jslint plusplus: true, vars: true*/
/*global angular, window, console, btoa */

(function (undefined) {
  'use strict';

  var module = angular.module('services');

  var ApiService = function (configService, $http) {

    // this user is logged in
    var currentUser = {
      email: null,
      token: null
    };

    var helpers = {};

    this.getCurrentUser = function() {
      return currentUser.email;
    };

    this.setCurrentUser = function (newCurrentUser) {
      currentUser = angular.copy(newCurrentUser);
    };

    /**
     * Receive some data from backend
     * @param collection/resource i. e. boards or tasks
     * @param parameters to specify collection
     * @returns list of collection
     */
    this.query = function (collection, parameters) {
      parameters = parameters || {};
      var headers = {'Authorization': helpers.encodeBasicAuth()};
      var uri = configService.baseUrl + collection;
      return $http(
        {method: "GET", url: uri, params: parameters, headers: headers,
          cache: false}
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
      var headers = {'Authorization': helpers.encodeBasicAuth()};
      var uri = configService.baseUrl + collection + '/' + id;
      return $http({method: "GET", url: uri, params: parameters, headers: headers,
        cache: false});
    };

    /**
     * Create / Save Object
     * @param collection
     * @param object
     * @returns true
     */
    this.createObject = function (collection, object) {
      var uri = configService.baseUrl + collection;
      var headers = {'Authorization': helpers.encodeBasicAuth()};
      return $http({method: "POST", url: uri, data:
        JSON.stringify(object), cache: false, headers: headers});
    };

    /**
     * Update the Object
     * @param collection for example friends, products
     * @param object
     * @returns true/false
     */
    this.updateObject = function (collection, object) {
      var uri = configService.baseUrl + collection + "/" + object.id;
      var headers = {'Authorization': helpers.encodeBasicAuth()};
      return $http({method: "PUT", url: uri, data:
        JSON.stringify(object), cache: false, headers: headers});
    };

    /**
     * Delete the specified object
     * @param collection
     * @param object
     * @returns {*}
     */
    this.deleteObject = function (collection, object) {
      var uri = configService.baseUrl + collection + "/" +object.id;
      var headers =  {
        'Authorization': helpers.encodeBasicAuth(),
        'Content-Type': 'application/json'
      };
      return $http({method: "DELETE", url: uri, cache: false, headers: headers, data: JSON.stringify(object)});
    };

    /**
     * Add Basic Authentication to the Header
     * @returns {string}
     */
    helpers.encodeBasicAuth = function () {

      return 'Basic ' + btoa(currentUser.email + ':' + currentUser.token);
    };

    /* service.createSessionByEMail = function (email, password) {
     return service.apiRequest('auth', 'POST', null, {
     'type': 'EMAIL',
     'email': email,
     'password': password
     });
     };*/
  };

  ApiService.$inject = ['ConfigService', '$http'];

  module.service('ApiService', ApiService);
})();
