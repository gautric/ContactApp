
angular.module('contactsApp').controller('NewContactController', function ($scope, $location, locationParser, flash, ContactResource ) {
    $scope.disabled = false;
    $scope.$location = $location;
    $scope.contact = $scope.contact || {};
    

    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            flash.setMessage({'type':'success','text':'The contact was created successfully.'});
            $location.path('/Contacts');
        };
        var errorCallback = function(response) {
            if(response && response.data && response.data.message) {
                flash.setMessage({'type': 'error', 'text': response.data.message}, true);
            } else {
                flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
            }
        };
        ContactResource.save($scope.contact, successCallback, errorCallback);
    }; 
    
    $scope.cancel = function() {
        $location.path("/Contacts");
    };
});