angular.module('mobilePancake')
.controller('MainController', function ($http, $scope) {
    $scope.value = 123;
    $scope.unit = 'g';
    
    $scope.send = function(newValue) {
        var payload = 'recipeName=' + $scope.selectedRecipe + '&selectedIngredient=' + $scope.selectedIngredient + '&value='+ $scope.value + '&unit' + $scope.unit;
        
        var config = {
            headers : {
                'Content-Type': 'x-www-form-urlencoded; charset=UTF-8',
                'Access-Control-Allow-Origin' : '*'
            }
        };
        
      $http.post('http://10.52.4.100:8080/measures/save', payload, config)
        .success(function(data, status, headers, config) {
          alert('Saved');
        })
        .error(function(data, status, headers, config) {
          alert('error');
        });
    };
    

    $http.get('http://10.52.4.100:8080/recipe/list')
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
          $http.get('http://10.52.4.100:8080/ingredient/list/' + selectedRecipe)
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
          $http.get('http://10.52.4.100:8080/recipe/' + selectedRecipe + '/load')
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