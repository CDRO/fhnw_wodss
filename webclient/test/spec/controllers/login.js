'use strict';

describe('Controller: LoginCtrl', function () {

  // load the controller's module
  beforeEach(angular.mock.module('angularWebclientApp'));

  var LoginCtrl,
    scope, AuthService;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope, _AuthService_, $q) {
    AuthService = _AuthService_;
    spyOn(AuthService, 'login').and.callFake(function(email, password){
      var deferred = $q.defer();
      if(email === 'test@test.ch' && password === 'pass'){
        deferred.resolve(true);
      }else{
        deferred.resolve({status: 403});
      }
      return deferred.promise;
    });

    scope = $rootScope.$new();

    LoginCtrl = $controller('LoginCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should initialize without login error', function () {
      expect(LoginCtrl.loginError).toBe(false);
  });

  /*  it('should create login error if password is wrong', function () {
      scope.email = 'test@test.ch';
      scope.password = 'wrongPass';
      LoginCtrl.loginNow();
      expect(AuthService.login).toHaveBeenCalled();
      scope.$root.$digest();
      //scope.$root.$apply();
      expect(LoginCtrl.loginError).toBe(true);
  });

  it('should create login error if user is wrong', function () {
    scope.email = 'wrongUser@test.ch';
    scope.password = 'pass';
    LoginCtrl.loginNow();
    root.$digest();
    expect(LoginCtrl.loginError).toBe(true);
  });

  it('should create no login error if login is correct', function () {
    scope.email = 'test@test.ch';
    scope.password = 'pass';
    LoginCtrl.loginNow();
    root.$digest();
    expect(LoginCtrl.loginError).toBe(false);
  });*/

});
