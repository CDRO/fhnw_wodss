'use strict';

describe('Controller: RegisterCtrl', function () {

  // load the controller's module
  beforeEach(angular.mock.module('angularWebclientApp'));

  var RegisterCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope, AuthService, $q) {
    scope = $rootScope.$new();

    spyOn(AuthService, 'registerAccount').and.callFake(function() {
      var deferred = $q.defer();
      deferred.reject({status: 409});
      return deferred.promise;
    });

    RegisterCtrl = $controller('RegisterCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  /*it('should register Account', function () {
      scope.email = 'test@test.ch';
      scope.password = 'pass';
      RegisterCtrl.register();
      scope.$root.$digest();
      expect(RegisterCtrl.errorMessage).not.toBe(null);
  });*/
});
