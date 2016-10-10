(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('ActividadDialogController', ActividadDialogController);

    ActividadDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Actividad', 'Empleado'];

    function ActividadDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Actividad, Empleado) {
        var vm = this;
        vm.actividad = entity;
        vm.empleados = Empleado.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('skynetApp:actividadUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.actividad.id !== null) {
                Actividad.update(vm.actividad, onSaveSuccess, onSaveError);
            } else {
                Actividad.save(vm.actividad, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
