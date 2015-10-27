angular.module('instrumentsController', []);
angular.module('ingredientsCtrl', []);
angular.module('recipesCtrl', []);
angular.module('instrumentMeasuresCtrl', []);

angular.module('webApp', ['instrumentsController','ingredientsCtrl','recipesCtrl','instrumentMeasuresCtrl'])
.controller('TabsCtrl', ['$scope', function ($scope) {
    $scope.tabs = [{
            title: 'Instruments',
            url: 'one.tpl.html'
        }, {
            title: 'Recipes',
            url: 'two.tpl.html'
        }, {
            title: 'Ingredients',
            url: 'three.tpl.html'
        }, {
            title: 'Instruments Measures',
            url: 'four.tpl.html'
    }];

    $scope.currentTab = 'one.tpl.html';

    $scope.onClickTab = function (tab) {
        $scope.currentTab = tab.url;
    }
    
    $scope.isActiveTab = function(tabUrl) {
        return tabUrl == $scope.currentTab;
    }
}]);