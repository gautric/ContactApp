

angular.module('contactsApp').controller('EngineContactController', function($scope, $http, $filter, ContactResource ) {

	  $scope.engine={};

      $scope.performEngine = function() {
     	
        $scope.searchResults = ContactResource.engine($scope.engine, function(){
        	$scope.filteredResults = $scope.searchResults;
            $scope.currentPage = 0;
        });
    };

});