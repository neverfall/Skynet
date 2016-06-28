(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('FollowUpDeleteController',FollowUpDeleteController);

    FollowUpDeleteController.$inject = ['$uibModalInstance', 'entity', 'FollowUp'];

    function FollowUpDeleteController($uibModalInstance, entity, FollowUp) {
        var vm = this;
        vm.followUp = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            FollowUp.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
