(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('ParticipantDialogController', ParticipantDialogController);

    ParticipantDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Participant'];

    function ParticipantDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Participant) {
        var vm = this;
        vm.participant = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('skynetApp:participantUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.participant.id !== null) {
                Participant.update(vm.participant, onSaveSuccess, onSaveError);
            } else {
                Participant.save(vm.participant, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
