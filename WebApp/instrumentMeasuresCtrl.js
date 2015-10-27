angular.module('instrumentMeasuresCtrl', [])
.controller('instrumentMeasuresCtrl', function($scope, $http) {
    $scope.instrumentMeasures = [];
    $scope.serverURL = "http://10.52.4.100:8080";
    
    var config = {
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded; charset=UTF-8'
					}
				};
    
    $scope.refreshInstrumentMeasures = function() {    
        $http.get($scope.serverURL + '/instrument/listMeasures', config)
                .then(
                    function(response) {
                        $scope.instrumentMeasures = response.data;
                    },    
                    function(response) {
                        alert("Error :" + response);
                });
    };

    $scope.refreshInstrumentMeasures();
});
