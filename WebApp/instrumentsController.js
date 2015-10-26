angular.module('webApp', []).controller('instrumentsController', function($scope) {
    $scope.newInstrumentName = "";
    $scope.newQRCode = 'https://chart.googleapis.com/chart?cht=qr&chl=kitchen%20balance&chs=180x180&choe=UTF-8&chld=L|2';
    $scope.instruments = [];
    $scope.ingredients = [{name:"flour",unit:"g",amount:"300"}];
    $scope.registerInstrument = function() {
            $scope.instruments.push({name:$scope.newInstrumentName,qrcode:$scope.newQRCode});    
            $scope.newInstrumentName = "";
    }
});