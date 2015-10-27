angular.module('mobilePancake')
.controller('MainController', function ($http, $scope) {
	$scope.serverurl = 'http://10.52.4.100:8080';
	$scope.selectedInstrument = "Balance";
    $scope.value = 123;
    $scope.unit = 'g';
    
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
	            })
	            .error(function(data, status, headers, config) {
	            	console.log('error');
	            });
    	} else {
    		alert('Please fill in all fields');
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
            alert('Error ' + status);
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
                alert('Error ingredients: ' + status);
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
                alert('Error ' + status);
              }
            ); 
        }
    });
});