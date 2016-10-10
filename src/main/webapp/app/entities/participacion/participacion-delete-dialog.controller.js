(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('ParticipacionDeleteController',ParticipacionDeleteController);

    ParticipacionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Participacion'];

    function ParticipacionDeleteController($uibModalInstance, entity, Participacion) {
        var vm = this;
        vm.participacion = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Participacion.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
