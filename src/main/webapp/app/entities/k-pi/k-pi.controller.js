(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('KPIController', KPIController);

    KPIController.$inject = ['$scope', '$state', 'KPI'];

    function KPIController ($scope, $state, KPI) {
        var vm = this;
        vm.kPIS = [];
        vm.loadAll = function() {
            KPI.query(function(result) {
                vm.kPIS = result;
            });
        };

        vm.loadAll();
        
    }
})();
