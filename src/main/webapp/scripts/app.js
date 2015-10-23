'use strict';

angular.module('contactsApp',['ngRoute','ngResource'])
  .config(['$routeProvider', function($routeProvider) {
    $routeProvider
      .when('/',{templateUrl:'views/landing.html',controller:'LandingPageController'})
      .when('/Contacts',{templateUrl:'views/Contact/search.html',controller:'SearchContactController'})
      .when('/Contacts/new',{templateUrl:'views/Contact/detail.html',controller:'NewContactController'})
      .when('/Contacts/engine',{templateUrl:'views/Contact/engine.html',controller:'EngineContactController'})
      .when('/Contacts/edit/:ContactId',{templateUrl:'views/Contact/detail.html',controller:'EditContactController'})
      .otherwise({
        redirectTo: '/'
      });
  }])
  .controller('LandingPageController', function LandingPageController() {
  })
  .controller('NavController', function NavController($scope, $location) {
    $scope.matchesRoute = function(route) {
        var path = $location.path();
        return (path === ("/" + route) || path.indexOf("/" + route + "/") == 0);
    };
  });
