angular.module('mobilePancake')

.directive('recipes', function($http, $rootScope) {
	return {
		restrict: 'E',
		scope: {},
		templateUrl: 'recipes/recipes.html',
		link: function($scope) {
      $http.get('http://localhost:8080/recipe/list')
        .success(
          function(data, status, headers, config) {
            $scope.data = {
              recipes: null,
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
