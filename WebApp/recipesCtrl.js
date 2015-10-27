angular.module('recipesCtrl', [])
.controller('recipesCtrl', function($scope, $http) {
    $scope.recipes = [];
    $scope.serverURL = "http://10.52.4.119:8080";
    
    var config = {
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded; charset=UTF-8'
					}
				};
    
    $scope.refreshRecipes = function() {    
        $http.get($scope.serverURL + '/recipe/list', config)
                .then(
                    function(response) {
                        $scope.recipes = response.data;
                    },    
                    function(response) {
                        alert("Error :" + response);
                });
    };
    
    $scope.showQRCode = function(qrCode) {
        var props = 'resizable= yes; status= no; scroll= no; help= no; center= yes;width=100;height=100;menubar=no;directories=no;location=no;modal=yes'
        var w = window.open("", "QRCode of Recipes", props);
        w.document.write("<img src='" + qrCode +"'></img>")
    }
    
    $scope.refreshRecipes();
});