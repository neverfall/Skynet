(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('DurationDialogController', DurationDialogController);

    DurationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Duration'];

    function DurationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Duration) {
        var vm = this;
        vm.duration = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('skynetApp:durationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.duration.id !== null) {
                Duration.update(vm.duration, onSaveSuccess, onSaveError);
            } else {
                Duration.save(vm.duration, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
