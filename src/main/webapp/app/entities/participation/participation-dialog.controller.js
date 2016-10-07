(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('ParticipationDialogController', ParticipationDialogController);

    ParticipationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Participation', 'Activity', 'Participant'];

    function ParticipationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Participation, Activity, Participant) {
        var vm = this;
        vm.participation = entity;
        vm.activities = Activity.query();
        vm.participants = Participant.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('skynetApp:participationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.participation.id !== null) {
                Participation.update(vm.participation, onSaveSuccess, onSaveError);
            } else {
                Participation.save(vm.participation, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.date = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();