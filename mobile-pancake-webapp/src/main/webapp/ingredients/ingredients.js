angular.module('mobilePancake')

.directive('ingredients', function($http, $rootScope) {
	return {
		restrict: 'E',
		scope: {},
		templateUrl: 'ingredients/ingredients.html',
		link: function($scope) {
      var selectedRecipe = 'Pancake';
      $http.get('http://10.52.4.100:8080/ingredient/list/' + selectedRecipe)
        .success(
          function(data, status, headers, config) {
            $scope.data = {
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
	};
});
