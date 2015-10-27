angular.module('instrumentsController', []);
angular.module('ingredientsCtrl', []);

angular.module('webApp', ['instrumentsController','ingredientsCtrl'])
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
    }];

    $scope.currentTab = 'one.tpl.html';

    $scope.onClickTab = function (tab) {
        $scope.currentTab = tab.url;
    }
    
    $scope.isActiveTab = function(tabUrl) {
        return tabUrl == $scope.currentTab;
    }
}]);