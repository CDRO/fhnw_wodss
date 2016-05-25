'use strict';

describe('Controller: BoardsCtrl', function () {

  // load the controller's module and fake response
  beforeEach(angular.mock.module('angularWebclientApp'));

  var BoardsCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope, BoardService, $q){
    scope = $rootScope.$new();

    spyOn(BoardService, 'getAll').and.callFake(function() {
      var deferred = $q.defer();
      deferred.resolve([{title: 'Board1'}, {title: 'Board2'}, {title: 'Board3'}]);
      return deferred.promise;
    });

    BoardsCtrl = $controller('BoardsCtrl', {
        $scope: scope
        // place here mocked dependencies
    });
  }));

  it('Should attach the boards to the list scope', function(){
      // To resolve the promise
      scope.$root.$digest();
      // Check result
      expect(BoardsCtrl.list).not.toBe(null);
      expect(BoardsCtrl.list.length).toBe(3);
  });

});
