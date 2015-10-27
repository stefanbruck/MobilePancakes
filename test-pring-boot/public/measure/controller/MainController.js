angular.module('mobilePancake')
.controller('MainController', function ($http, $scope) {
	$scope.serverurl = SERVER_URL;
	$scope.selectedInstrument = "Balance";
    reset($scope);
    
    $scope.recipeChanged = function() {
    	resetStatus($scope);
    	$scope.selectedIngredient = null;
    	$scope.value = null;
    };
    
    $scope.ingredientChanged = function() {
    	resetStatus($scope);
    	$scope.value = null;
    };
    
    $scope.send = function() {
    	if ($scope.selectedRecipe != null && $scope.selectedIngredient != null && $scope.value != null && $scope.unit != null) {
	        var payload = {};
	        payload.instrumentName = $scope.selectedInstrument;
	        payload.ingredientName = $scope.selectedIngredient;
	        payload.value = $scope.value;
	        
	        var config = {
		            headers : {
		                'Content-Type': 'application/json;charset=UTF-8',
		            }
		        };
	        
	        $http.post($scope.serverurl + '/instrument/writeMeasure', payload, config)
	            .success(function(data, status, headers, config) {
	            	console.log('Saved');
	            	succeed($scope);
	            })
	            .error(function(data, status, headers, config) {
	          	  	failed($scope, "Failed to send the measures.");
	            	console.log('error');
	            });
    	} else {
    		failed($scope, 'Please fill in all fields');
    	}
    };
    

    $http.get($scope.serverurl + '/recipe/list')
        .success(
          function(data, status, headers, config) {
            $scope.selectedRecipe = null;
            $scope.recipesModel = {
              availableOptions: data
            };
          }
        )
        .error(
          function(data, status, headers, config) {
        	  failed($scope, "Failed to load the recipes.");
        	  console.log('Error ' + status);
          }
        );
  
    $scope.$watch('selectedRecipe', function(selectedRecipe) {
        if (selectedRecipe) {
          $http.get($scope.serverurl + '/ingredient/list/' + selectedRecipe)
            .success(
              function(data, status, headers, config) {
                $scope.ingredientsModel = {
                  ingredients: data
                };
              }
            )
            .error(
              function(data, status, headers, config) {
            	  failed($scope, "Failed to load the ingredients.");
            	  console.log('Error ingredients: ' + status);
              }
            );
        }
    });
  
    $scope.$watch('selectedRecipe', function(selectedRecipe) {
        if (selectedRecipe) {
          $http.get($scope.serverurl + '/recipe/' + selectedRecipe + '/load')
            .success(
              function(data, status, headers, config) {
                $scope.instructions = data.content;
              }
            )
            .error(
              function(data, status, headers, config) {
            	  failed($scope, "Failed to load the instructions.");
            	  console.log('Error loading the instructions' + status);
              }
            ); 
        }
    });
});

function reset(scope) {
	scope.selectedIngredient = null;
	scope.selectedRecipe = null;
	scope.value = null;
    scope.unit = 'g';
    resetStatus(scope);
}

function resetStatus(scope) {
    scope.failed = false;
    scope.success = false;
    scope.errorMsg = ' It did not work :(';
}

function failed(scope, msg) {
	scope.failed = true;
	scope.success = false;
	scope.errorMsg = msg;
}

function succeed(scope) {
	scope.failed = false;
	scope.success = true;
	scope.errorMsg = null;
}