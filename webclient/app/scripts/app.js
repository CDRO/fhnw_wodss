'use strict';

/**
 * @ngdoc overview
 * @name angularWebclientApp
 * @description
 * # angularWebclientApp
 *
 * Main module of the application.
 */
angular
  .module('angularWebclientApp', [
    'services',
    'ngAnimate',
    'ngCookies',
    'ngResource',
    'ui.router',
    'ui.bootstrap',
    'ngSanitize',
    'ngTouch'
  ])
  .config(function ($stateProvider, $urlRouterProvider) {
    // For any unmatched url, redirect to /state1
    $urlRouterProvider.otherwise("/home");

    $stateProvider
      .state('home', {
        url: '',
        templateUrl: 'views/main.html',
        controller: 'MainCtrl',
        controllerAs: 'main'
      })
      .state('login', {
        url: "/login",
        templateUrl: 'views/login.html',
        controller: 'LoginCtrl',
        controllerAs: 'login'
      })
      .state('register', {
        url: "/register",
        templateUrl: 'views/register.html',
        controller: 'RegisterCtrl',
        controllerAs: 'register'
      })
      .state('boards', {
        url: "/boards",
        templateUrl: 'views/boards/boardOverview.html',
        controller: 'BoardsCtrl',
        controllerAs: 'boards'
      })
      .state('tasksFromBoard', {
        url: "/tasks/board/:boardId",
        templateUrl: 'views/boards/boardOverview.html',
        controller: 'TasksCtrl',
        controllerAs: 'tasks'
      })
      .state('tasks', {
        url: "/tasks",
        templateUrl: 'views/tasks.html',
        controller: 'TasksCtrl',
        controllerAs: 'tasks'
      });
  }).controller('HeaderCtrl', function ($scope, $location) {
    $scope.isActive = function (viewLocation) {
      return $location.path().indexOf(viewLocation) == 0;
    };
});
