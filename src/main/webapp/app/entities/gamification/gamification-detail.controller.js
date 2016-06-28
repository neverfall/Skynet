(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('GamificationDetailController', GamificationDetailController);

    GamificationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Gamification', 'Category'];

    function GamificationDetailController($scope, $rootScope, $stateParams, entity, Gamification, Category) {
        var vm = this;
        vm.gamification = entity;
        
        var unsubscribe = $rootScope.$on('skynetApp:gamificationUpdate', function(event, result) {
            vm.gamification = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
