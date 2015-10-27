angular.module('ingredientsCtrl', [])
.controller('ingredientsCtrl', function($scope, $http) {
    $scope.ingredients = [];
    $scope.serverURL = "http://10.52.4.100:8080";
    
    var config = {
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded; charset=UTF-8'
					}
				};
    
    $scope.refreshIngredients = function() {    
        $http.get($scope.serverURL + '/ingredient/list', config)
                .then(
                    function(response) {
                        $scope.ingredients = response.data;
                    },    
                    function(response) {
                        alert("Error :" + response);
                });
    };
    
    $scope.showQRCode = function(qrCode) {
        var props = 'resizable= yes; status= no; scroll= no; help= no; center= yes;width=100;height=100;menubar=no;directories=no;location=no;modal=yes'
        var w = window.open("", "QRCode of Ingredients", props);
        w.document.write("<img src='" + qrCode +"'></img>")
    }
    
    $scope.refreshIngredients();
});
