(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('CustomerSatisfactionNPSDeleteController',CustomerSatisfactionNPSDeleteController);

    CustomerSatisfactionNPSDeleteController.$inject = ['$uibModalInstance', 'entity', 'CustomerSatisfactionNPS'];

    function CustomerSatisfactionNPSDeleteController($uibModalInstance, entity, CustomerSatisfactionNPS) {
        var vm = this;
        vm.customerSatisfactionNPS = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            CustomerSatisfactionNPS.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
