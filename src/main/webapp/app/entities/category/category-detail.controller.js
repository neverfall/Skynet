(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('CategoryDetailController', CategoryDetailController);

    CategoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Category', 'Gamification'];

    function CategoryDetailController($scope, $rootScope, $stateParams, entity, Category, Gamification) {
        var vm = this;
        vm.category = entity;
        
        var unsubscribe = $rootScope.$on('skynetApp:categoryUpdate', function(event, result) {
            vm.category = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
