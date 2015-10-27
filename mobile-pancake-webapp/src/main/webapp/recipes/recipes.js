angular.module('mobilePancake')

.directive('recipes', function($http, $rootScope) {
	return {
		restrict: 'E',
		scope: {},
		templateUrl: 'recipes/recipes.html',
		link: function($scope) {
      
      $scope.recipeSelected = function(recipeName) {
        $rootScope.selectedRecipe = recipeName;
      };
      
      $http.get('http://10.52.4.100:8080/recipe/list')
        .success(
          function(data, status, headers, config) {
            $scope.data = {
              recipe: null,
              availableOptions: data
            };
          }
        )
        .error(
          function(data, status, headers, config) {
            alert('Error ' + status);
          }
        ); 
		}
	};
});
