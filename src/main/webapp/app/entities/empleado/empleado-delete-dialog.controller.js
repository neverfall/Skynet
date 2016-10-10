(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('EmpleadoDeleteController',EmpleadoDeleteController);

    EmpleadoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Empleado'];

    function EmpleadoDeleteController($uibModalInstance, entity, Empleado) {
        var vm = this;
        vm.empleado = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Empleado.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
