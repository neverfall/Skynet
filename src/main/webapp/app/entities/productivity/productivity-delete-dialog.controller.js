(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('ProductivityDeleteController',ProductivityDeleteController);

    ProductivityDeleteController.$inject = ['$uibModalInstance', 'entity', 'Productivity'];

    function ProductivityDeleteController($uibModalInstance, entity, Productivity) {
        var vm = this;
        vm.productivity = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Productivity.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
