(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('CustomerSatisfactionNPSDialogController', CustomerSatisfactionNPSDialogController);

    CustomerSatisfactionNPSDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CustomerSatisfactionNPS'];

    function CustomerSatisfactionNPSDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CustomerSatisfactionNPS) {
        var vm = this;
        vm.customerSatisfactionNPS = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('skynetApp:customerSatisfactionNPSUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.customerSatisfactionNPS.id !== null) {
                CustomerSatisfactionNPS.update(vm.customerSatisfactionNPS, onSaveSuccess, onSaveError);
            } else {
                CustomerSatisfactionNPS.save(vm.customerSatisfactionNPS, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
