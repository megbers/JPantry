var jPantry = angular.module('jPantry', ['ngResource', 'jpantryLookUpService']);

jPantry.controller('jPantryCntrl', ['$scope', 'jPantryService', 'jPantryItemIntake', 'jPantryItemOutbound', 
function($scope, jPantryService, jPantryItemIntake, jPantryItemOutbound) {
	
	var quantityTextField = angular.element('#quantityTextField');
	
	$scope.upcCodeKeyPress = function(event) {
		if (event.which==13) {
    		if($scope.scanType == 'lookup') {
				$scope.item = jPantryService.get({upcCode: $scope.upcCode});
			} else {
				quantityTextField.focus();
			}
    	}
	}
	
	$scope.quantityKeyPress = function(event) {
		if (event.which==13) {
			if($scope.scanType == 'in') {
				$scope.item = jPantryItemIntake.get({upcCode: $scope.upcCode, quantity: $scope.quantity});
			} else if($scope.scanType == 'out') {
				$scope.item = jPantryItemOutbound.get({upcCode: $scope.upcCode, quantity: $scope.quantity});
			}
		}
	}
	
	$scope.$watch('scanType', function(scanType) {
       if(scanType === 'lookup') {
			quantityTextField.parent().hide();
	   } else {
			quantityTextField.parent().show();
	   }
	});
			
}]);

var jpantryLookUpService = angular.module('jpantryLookUpService', ['ngResource']);

jpantryLookUpService.factory('jPantryService', ['$resource', function($resource) {
	return $resource('/JPantry/rest/item/find/:upcCode', {upcCode: '@upcCode'});
}]);

jpantryLookUpService.factory('jPantryItemIntake', ['$resource', function($resource) {
	return $resource('/JPantry/rest/item/in/:upcCode/:quantity', {upcCode: '@upcCode', quantity: '@quantity'});
}]);

jpantryLookUpService.factory('jPantryItemOutbound', ['$resource', function($resource) {
	return $resource('/JPantry/rest/item/out/:upcCode/:quantity', {upcCode: '@upcCode', quantity: '@quantity'});
}]);