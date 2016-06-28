(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('CustomerSatisfactionGradeDeleteController',CustomerSatisfactionGradeDeleteController);

    CustomerSatisfactionGradeDeleteController.$inject = ['$uibModalInstance', 'entity', 'CustomerSatisfactionGrade'];

    function CustomerSatisfactionGradeDeleteController($uibModalInstance, entity, CustomerSatisfactionGrade) {
        var vm = this;
        vm.customerSatisfactionGrade = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            CustomerSatisfactionGrade.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
