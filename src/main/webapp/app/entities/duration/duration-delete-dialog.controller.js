(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('DurationDeleteController',DurationDeleteController);

    DurationDeleteController.$inject = ['$uibModalInstance', 'entity', 'Duration'];

    function DurationDeleteController($uibModalInstance, entity, Duration) {
        var vm = this;
        vm.duration = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Duration.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
