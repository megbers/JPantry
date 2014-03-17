var jPantry = angular.module('jPantry', ['ngResource', 'jPantryService']);

jPantry.controller('jPantryCntrl', ['$scope', '$rootScope', 'jPantryLookUpService', 'jPantryItemIntake', 'jPantryItemOutbound', 'jPantryItemUpdate',
function($scope, $rootScope, jPantryLookUpService, jPantryItemIntake, jPantryItemOutbound, jPantryItemUpdate) {
	var upcCodeTextField = angular.element('#upcCodeTextField');
	var quantityTextField = angular.element('#quantityTextField');
	var onListCheckbox = angular.element('#onListCheckbox');
	var adminUpdateFields = angular.element('#jPantryAdminUpdateFields');

    var allList = angular.element('#jPantryAllListCntrl');
    var shoppingList = angular.element('#jPantryShoppingListCntrl');
    var inventoryList = angular.element('#jPantryInventoryListCntrl');

	$scope.scanType = 'lookup';

    function resetFields() {
        $scope.upcCode = '';
        $scope.quantity = '';
    }

	$scope.upcCodeKeyPress = function(event) {
		if (event.which == 13) {
    		if($scope.scanType == 'lookup') {
				$scope.item = jPantryLookUpService.get({upcCode: $scope.upcCode}, function() {
                    $rootScope.$broadcast('refreshAll', {});
                });
                resetFields();
			} else {
				quantityTextField.focus();
			}
    	}
	}
	
	$scope.quantityKeyPress = function(event) {
		if (event.which == 13) {
			if($scope.scanType == 'in') {
				$scope.item = jPantryItemIntake.get({upcCode: $scope.upcCode, quantity: $scope.quantity}, function() {
                    $rootScope.$broadcast('refreshInventory', {});
                    $rootScope.$broadcast('refreshAll', {});
                });
			} else if($scope.scanType == 'out') {
                var onList = $scope.onList ? $scope.onList : false;
				$scope.item = jPantryItemOutbound.get({upcCode: $scope.upcCode, quantity: $scope.quantity, onList: onList}, function() {
                    $rootScope.$broadcast('refreshShopping', {});
                    $rootScope.$broadcast('refreshAll', {});
                });
			}
            resetFields();
			upcCodeTextField.focus();
		}
	}
	
	$scope.updateItem = function() {
		jPantryItemUpdate.update($scope.item, function(data) {
			$rootScope.$broadcast('refreshAll', {});
		});
	}

    $scope.$watch('scanType', function(scanType) {
       allList.hide();
       shoppingList.hide();
       inventoryList.hide();

       if(scanType === 'lookup') {
           quantityTextField.parent().hide();
           allList.show();
		   adminUpdateFields.show();
	   } else {
		   adminUpdateFields.hide();
           quantityTextField.parent().show();
	   }
	   
	   if(scanType === 'out') {
	       onListCheckbox.parent().show();
           shoppingList.show();
	   } else {
	   		onListCheckbox.parent().hide();
	   }

       if(scanType === 'in') {
           inventoryList.show();
       }
	});
	
	$scope.$on('updateItemFromList', function(event, item) {
        $scope.item = item;
    });
			
}]);




jPantry.controller('jPantryInventoryListCntrl', ['$scope', 'jPantryInventoryListService', 'jPantryItemIntake', 
function($scope, jPantryInventoryListService, jPantryItemIntake) {

    $scope.$on('refreshInventory', function(event, data) {
        refreshInventory();
    });

    function refreshInventory() {
        $scope.inventoryList = jPantryInventoryListService.query({});
    }

    refreshInventory();
}]);

jPantry.controller('jPantryShoppingListCntrl', ['$scope', 'jPantryShoppingListService',
function($scope, jPantryShoppingListService) {
    $scope.$on('refreshShopping', function(event, data) {
        refreshShopping();
    });

    function refreshShopping() {
        $scope.shoppingList = jPantryShoppingListService.query({});
    }

    refreshShopping()
}]);

jPantry.controller('jPantryAllListCntrl', ['$scope', '$rootScope', 'jPantryAllListService',
function($scope, $rootScope, jPantryAllListService) {
	// Make these controllers children of a common parent and inherit this method
	$scope.itemSelected = function (item) {
		$rootScope.$broadcast('updateItemFromList', item);
	}

    $scope.$on('refreshAll', function(event, data) {
        refreshAll();
    });

    function refreshAll() {
        $scope.allList = jPantryAllListService.query({});
    }

    refreshAll();
}]);




var jPantryService = angular.module('jPantryService', ['ngResource']);

jPantryService.factory('jPantryItemUpdate', ['$resource', function($resource) {
	return $resource('/JPantry/rest/item/update', {}, {update: {method: 'PUT', data: {}, isArray: false}});
}]);

jPantryService.factory('jPantryLookUpService', ['$resource', function($resource) {
	return $resource('/JPantry/rest/item/find/:upcCode', {upcCode: '@upcCode'});
}]);

jPantryService.factory('jPantryItemIntake', ['$resource', function($resource) {
	return $resource('/JPantry/rest/item/in/:upcCode/:quantity', {upcCode: '@upcCode', quantity: '@quantity'});
}]);

jPantryService.factory('jPantryItemOutbound', ['$resource', function($resource) {
	return $resource('/JPantry/rest/item/out/:upcCode/:quantity/:onList', {upcCode: '@upcCode', quantity: '@quantity', onList: '@onList'});
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