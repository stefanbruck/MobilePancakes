angular.module('instrumentsController', []);

angular.module('webApp', ['instrumentsController'])
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