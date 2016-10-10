(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('ActividadDeleteController',ActividadDeleteController);

    ActividadDeleteController.$inject = ['$uibModalInstance', 'entity', 'Actividad'];

    function ActividadDeleteController($uibModalInstance, entity, Actividad) {
        var vm = this;
        vm.actividad = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Actividad.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
