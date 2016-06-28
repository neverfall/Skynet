(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('CustomerSatisfactionNPSDetailController', CustomerSatisfactionNPSDetailController);

    CustomerSatisfactionNPSDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'CustomerSatisfactionNPS'];

    function CustomerSatisfactionNPSDetailController($scope, $rootScope, $stateParams, entity, CustomerSatisfactionNPS) {
        var vm = this;
        vm.customerSatisfactionNPS = entity;
        
        var unsubscribe = $rootScope.$on('skynetApp:customerSatisfactionNPSUpdate', function(event, result) {
            vm.customerSatisfactionNPS = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
