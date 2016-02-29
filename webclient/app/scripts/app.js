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
    'ngRoute',
    'ngSanitize',
    'ngTouch'
  ])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl',
        controllerAs: 'main'
      })
      .when('/login', {
        templateUrl: 'views/login.html',
        controller: 'LoginCtrl',
        controllerAs: 'login'
      })
      .when('/register', {
        templateUrl: 'views/register.html',
        controller: 'RegisterCtrl',
        controllerAs: 'register'
      })
      .when('/boards', {
        templateUrl: 'views/boards/boardOverview.html',
        controller: 'BoardsCtrl',
        controllerAs: 'boards'
      })
      .otherwise({
        redirectTo: '/'
      });
  });
