(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('CustomerSatisfactionGradeDialogController', CustomerSatisfactionGradeDialogController);

    CustomerSatisfactionGradeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CustomerSatisfactionGrade'];

    function CustomerSatisfactionGradeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CustomerSatisfactionGrade) {
        var vm = this;
        vm.customerSatisfactionGrade = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('skynetApp:customerSatisfactionGradeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.customerSatisfactionGrade.id !== null) {
                CustomerSatisfactionGrade.update(vm.customerSatisfactionGrade, onSaveSuccess, onSaveError);
            } else {
                CustomerSatisfactionGrade.save(vm.customerSatisfactionGrade, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
