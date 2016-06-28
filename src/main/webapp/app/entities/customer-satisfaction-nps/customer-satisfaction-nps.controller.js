(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('CustomerSatisfactionNPSController', CustomerSatisfactionNPSController);

    CustomerSatisfactionNPSController.$inject = ['$scope', '$state', 'CustomerSatisfactionNPS'];

    function CustomerSatisfactionNPSController ($scope, $state, CustomerSatisfactionNPS) {
        var vm = this;
        vm.customerSatisfactionNPS = [];
        vm.loadAll = function() {
            CustomerSatisfactionNPS.query(function(result) {
                vm.customerSatisfactionNPS = result;
            });
        };

        vm.loadAll();
        
    }
})();
