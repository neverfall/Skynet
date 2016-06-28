(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('KPIDeleteController',KPIDeleteController);

    KPIDeleteController.$inject = ['$uibModalInstance', 'entity', 'KPI'];

    function KPIDeleteController($uibModalInstance, entity, KPI) {
        var vm = this;
        vm.kPI = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            KPI.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
