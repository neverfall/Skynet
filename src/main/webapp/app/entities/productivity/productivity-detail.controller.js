(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('ProductivityDetailController', ProductivityDetailController);

    ProductivityDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Productivity'];

    function ProductivityDetailController($scope, $rootScope, $stateParams, entity, Productivity) {
        var vm = this;
        vm.productivity = entity;
        
        var unsubscribe = $rootScope.$on('skynetApp:productivityUpdate', function(event, result) {
            vm.productivity = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
