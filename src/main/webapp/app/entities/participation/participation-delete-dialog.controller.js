(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('ParticipationDeleteController',ParticipationDeleteController);

    ParticipationDeleteController.$inject = ['$uibModalInstance', 'entity', 'Participation'];

    function ParticipationDeleteController($uibModalInstance, entity, Participation) {
        var vm = this;
        vm.participationId = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Participation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
