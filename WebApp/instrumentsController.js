angular.module('webApp', []).controller('instrumentsController', function($scope, $http) {
    $scope.newInstrumentName = "";
    $scope.newQRCode = 'https://chart.googleapis.com/chart?cht=qr&chl=kitchen%20balance&chs=180x180&choe=UTF-8&chld=L|2';
    $scope.instruments = [];
    $scope.ingredients = [{name:"flour",unit:"g",amount:"300"}];
    $scope.serverURL = "http://10.52.4.100:8080"
    
    $scope.registerInstrument = function() {
        var config = {
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded; charset=UTF-8'
					}
				};
        
            $http.post($scope.serverURL + '/instrument/register', 'name=' + $scope.newInstrumentName, config)
					.success(
						function(data, status, headers, config) {
                            alert("something");
                            $scope.instruments.push({name:$scope.newInstrumentName,qrcode:data});  
        				})
					.error(
						function(data, status, headers, config) {
							if (status == 400) {
								alert(data.error_description);
							} else {
								alert("Error :" + status);
							}
						});

        
            $scope.newInstrumentName = "";
    }
});