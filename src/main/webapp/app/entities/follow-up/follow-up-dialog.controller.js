(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('FollowUpDialogController', FollowUpDialogController);

    FollowUpDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'FollowUp'];

    function FollowUpDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, FollowUp) {
        var vm = this;
        vm.followUp = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('skynetApp:followUpUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.followUp.id !== null) {
                FollowUp.update(vm.followUp, onSaveSuccess, onSaveError);
            } else {
                FollowUp.save(vm.followUp, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
