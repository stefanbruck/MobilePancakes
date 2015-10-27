angular.module('mobilePancake')

.directive('instructions', function($http, $rootScope) {
	return {
		restrict: 'E',
		scope: {},
		templateUrl: 'instructions/instructions.html',
		link: function($scope) {
            var recipe = 'Pancake';
      $http.get('http://10.52.4.100:8080/recipe/'+recipe+'/load')
        .success(
          function(data, status, headers, config) {
            $scope.data = data.content;
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