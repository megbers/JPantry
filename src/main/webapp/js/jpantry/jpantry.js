var jPantry = angular.module('jPantry', ['ngResource', 'jPantryService']);

jPantry.controller('jPantryCntrl', ['$scope', 'jPantryLookUpService', 'jPantryItemIntake', 'jPantryItemOutbound', 
function($scope, jPantryLookUpService, jPantryItemIntake, jPantryItemOutbound) {
	
	var quantityTextField = angular.element('#quantityTextField');
	
	$scope.upcCodeKeyPress = function(event) {
		if (event.which==13) {
    		if($scope.scanType == 'lookup') {
				$scope.item = jPantryLookUpService.get({upcCode: $scope.upcCode});
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

jPantry.controller('jPantryInventoryListCntrl', ['$scope', 'jPantryInventoryListService',
function($scope, jPantryInventoryListService) {
	$scope.inventoryList = jPantryInventoryListService.query({});
}]);

jPantry.controller('jPantryShoppingListCntrl', ['$scope', 'jPantryShoppingListService',
function($scope, jPantryShoppingListService) {
	$scope.shoppingList = jPantryShoppingListService.query({});
}]);

jPantry.controller('jPantryAllListCntrl', ['$scope', 'jPantryAllListService',
function($scope, jPantryAllListService) {
	$scope.allList = jPantryAllListService.query({});
}]);

var jPantryService = angular.module('jPantryService', ['ngResource']);

jPantryService.factory('jPantryLookUpService', ['$resource', function($resource) {
	return $resource('/JPantry/rest/item/find/:upcCode', {upcCode: '@upcCode'});
}]);

jPantryService.factory('jPantryItemIntake', ['$resource', function($resource) {
	return $resource('/JPantry/rest/item/in/:upcCode/:quantity', {upcCode: '@upcCode', quantity: '@quantity'});
}]);

jPantryService.factory('jPantryItemOutbound', ['$resource', function($resource) {
	return $resource('/JPantry/rest/item/out/:upcCode/:quantity', {upcCode: '@upcCode', quantity: '@quantity'});
}]);

jPantryService.factory('jPantryShoppingListService', ['$resource', function($resource) {
	return $resource('/JPantry/rest/item/find/shopping', {});
}]);

jPantryService.factory('jPantryInventoryListService', ['$resource', function($resource) {
	return $resource('/JPantry/rest/item/find/inventory', {});
}]);

jPantryService.factory('jPantryAllListService', ['$resource', function($resource) {
	return $resource('/JPantry/rest/item/find/all', {});
}]);