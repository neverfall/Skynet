(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('GamificationDeleteController',GamificationDeleteController);

    GamificationDeleteController.$inject = ['$uibModalInstance', 'entity', 'Gamification'];

    function GamificationDeleteController($uibModalInstance, entity, Gamification) {
        var vm = this;
        vm.gamification = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Gamification.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
