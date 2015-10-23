angular.module('contactsApp').factory('ContactResource', function($resource) {
	var resource = $resource('rest/contacts/:ContactId', {
		ContactId : '@id'
	}, {
		'queryAll' : {
			method : 'GET',
			isArray : true
		},
		'query' : {
			method : 'GET',
			isArray : false
		},
		'update' : {
			method : 'PUT'
		},
		'engine' : {
			method : 'GET',
			isArray : true,
			url : 'rest/contacts/search'
		}
	});
	return resource;
});