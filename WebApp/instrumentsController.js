angular.module('webApp', []).controller('instrumentsController', function($scope, $http) {
    $scope.newInstrumentName = "";
    $scope.newQRCode = 'https://chart.googleapis.com/chart?cht=qr&chl=kitchen%20balance&chs=180x180&choe=UTF-8&chld=L|2';
    $scope.instruments = [];
    //$scope.ingredients = [];
    $scope.serverURL = "http://10.52.4.119:8080"
    //$scope.serverURL = "http://10.52.4.100:8080"
    $scope.selectedQRCode;
    
    var config = {
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded; charset=UTF-8'
					}
				};
    
    $scope.registerInstrument = function() {
        $http.post($scope.serverURL + '/instrument/register', 'name=' + $scope.newInstrumentName, config)
                .then(
                    function(response) {
                        $scope.refreshInstruments();
                    },
                    function(response) {
                        alert("Error registering instrument");
                    })
                .finally(
                    function() {
                        $scope.newInstrumentName = "";
                    });
    };
    
    $scope.onInstrumentNameKeyPress = function(event)
    {
        if (event.charCode == 13) //if enter then hit the search button
            $scope.registerInstrument();
    }
    
    $scope.refreshInstruments = function() {
        $http.get($scope.serverURL + '/instrument/list', config)
                .then(
                    function(response) {
                        $scope.instruments = response.data;
                    },    
                    function(response) {
                        alert("Error :" + response);
                });
        
    }
    
    $scope.refreshIngredients = function() {    
        $http.get($scope.serverURL + '/ingredient/list', config)
                .then(
                    function(response) {
                        alert("hello" + response);
                    },    
                    function(response) {
                        alert("Error :" + response);
                });
    };
    
    $scope.showQRCode = function(qrCode) {
        var props = 'resizable= yes; status= no; scroll= no; help= no; center= yes;width=100;height=100;menubar=no;directories=no;location=no;modal=yes'
        var w = window.open("", "QRCode of Instrument", props);
        w.document.write("<img src='" + qrCode +"'></img>")
    }
    
    $scope.refreshInstruments();
});