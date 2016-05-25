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
    'ngTouch',
    'pascalprecht.translate',
    'angularFileUpload',
    'ngFileUpload',
    'ngTagsInput',
    'ngDragDrop'
  ])
  .config(function ($stateProvider, $urlRouterProvider, $translateProvider, translations_DE) {
    // For any unmatched url, redirect to home
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
      .state('logout', {
        url: "/logout",
        controller: "LogoutCtrl"
      })
      .state('register', {
        url: "/register",
        templateUrl: 'views/register.html',
        controller: 'RegisterCtrl',
        controllerAs: 'register'
      })
      .state('boards', {
        url: "/boards",
        templateUrl: 'views/boardOverview.html',
        controller: 'BoardsCtrl',
        controllerAs: 'boards'
      })
      .state('tasksFromBoard', {
        url: "/tasks/board/:boardId",
        params: {
            boardId: null,
            tasks: null,
            boards: null,
            assignees: null
        },
        templateUrl: 'views/tasks.html',
        controller: 'TasksCtrl',
        controllerAs: 'tasks'
      })
      .state('tasks', {
        url: "/tasks",
        params: {
            boards: null
        },
        templateUrl: 'views/tasks.html',
        controller: 'TasksCtrl',
        controllerAs: 'tasks'
      })
      .state('validate', {
        url: "/validate/:id?validationCode",
        templateUrl: "views/validate.html",
        controller: "ValidationCtrl",
        controllerAs: "validation",
        params: {
          validationCode: null,
        }
      })
      .state('resetPassword', {
        url: "/reset/:id?resetCode",
        templateUrl: "views/resetPassword.html",
        controller: "ResetPasswordCtrl",
        controllerAs: "reset",
        params: {
          id: null,
          resetCode: null
        }
      })
      .state('user', {
        url: "/user",
        templateUrl: "views/editUser.html",
        controller: "UserCtrl",
        controllerAs: "user"
      })
      .state('error', {
        url: "/error",
        templateUrl: "views/error.html",
        controller: "ErrorCtrl",
        controllerAs: "error"
      });

      // Add Translations
      $translateProvider
        .translations('de', translations_DE)
        .preferredLanguage('de')
        .useSanitizeValueStrategy('sanitizeParameters');
  });
