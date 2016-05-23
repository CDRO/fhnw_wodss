'use strict';

var UserCtrl = function ($scope, config, auth) {
  var self = this;
  this.profile = config.getCurrentUser();

  /* Update Profile information */
  this.update = function(){
    auth.updateProfile(self.profile).then(function(response){
        self.saved = 'user.saved';
    }, function(error){
        self.saved = '';
        self.errorMessage = 'user.error';
    });
  }

};

UserCtrl.$inject = ['$scope', 'ConfigService', 'AuthService'];

angular.module('angularWebclientApp').controller('UserCtrl', UserCtrl);
