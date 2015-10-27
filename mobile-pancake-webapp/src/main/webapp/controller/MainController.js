angular.module('mobilePancake')
.controller('MainController', function ($http, $scope) {
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