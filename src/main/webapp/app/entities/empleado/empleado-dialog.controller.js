(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('EmpleadoDialogController', EmpleadoDialogController);

    EmpleadoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Empleado'];

    function EmpleadoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Empleado) {
        var vm = this;
        vm.empleado = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('skynetApp:empleadoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.empleado.id !== null) {
                Empleado.update(vm.empleado, onSaveSuccess, onSaveError);
            } else {
                Empleado.save(vm.empleado, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
