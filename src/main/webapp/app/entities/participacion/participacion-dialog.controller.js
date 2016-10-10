(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('ParticipacionDialogController', ParticipacionDialogController);

    ParticipacionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Participacion', 'Actividad', 'Empleado'];

    function ParticipacionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Participacion, Actividad, Empleado) {
        var vm = this;
        vm.participacion = entity;
        vm.actividads = Actividad.query();
        vm.empleados = Empleado.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('skynetApp:participacionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.participacion.id !== null) {
                Participacion.update(vm.participacion, onSaveSuccess, onSaveError);
            } else {
                Participacion.save(vm.participacion, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.fecha = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
