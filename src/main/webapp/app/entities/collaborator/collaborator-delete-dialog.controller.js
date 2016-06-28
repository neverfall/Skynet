(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('CollaboratorDeleteController',CollaboratorDeleteController);

    CollaboratorDeleteController.$inject = ['$uibModalInstance', 'entity', 'Collaborator'];

    function CollaboratorDeleteController($uibModalInstance, entity, Collaborator) {
        var vm = this;
        vm.collaborator = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Collaborator.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
