/*jslint plusplus: true, vars: true*/
/*global angular, window, console, btoa */
(function (undefined) {
  'use strict';

  var module = angular.module('services');

  var AttachmentService = function (service) {
    /**
     * Removes the Attachment
     * @param board board with id specified
     */
    this.remove = function (attachment) {
        service.deleteObject('task', attachment);
    };

    this.getPath = function(){
        return service.getPath() + 'attachment/';
    }

  };

  // Inject Dependencies
  AttachmentService.$inject = ['ApiService'];

  // Export
  module.service('AttachmentService', AttachmentService);
})();
