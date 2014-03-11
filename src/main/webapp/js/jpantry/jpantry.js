var jPantry = angular.module('jPantry', ['ngResource', 'jpantryLookUpService']);

jPantry.controller('jPantryCntrl', ['$scope', 'jPantryService', function($scope, jPantryService) {
	
	$scope.upcCodeKeyPress = function(event) {
		if (event.which==13) {
    		angular.element(document.getElementById('quantityTextField')).focus();
    	}
	}
	
	$scope.quantityKeyPress = function(event) {
		if (event.which==13) {
			$scope.item = jPantryService.get({upcCode: $scope.upcCode});
		}
	}
			
}]);

var jpantryLookUpService = angular.module('jpantryLookUpService', ['ngResource']);

jpantryLookUpService.factory('jPantryService', ['$resource', function($resource) {
	return $resource('/JPantry/rest/item/find/:upcCode', {upcCode: '@upcCode'});
}]);