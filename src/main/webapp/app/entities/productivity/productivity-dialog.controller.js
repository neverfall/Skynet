(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('ProductivityDialogController', ProductivityDialogController);

    ProductivityDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Productivity'];

    function ProductivityDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Productivity) {
        var vm = this;
        vm.productivity = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('skynetApp:productivityUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.productivity.id !== null) {
                Productivity.update(vm.productivity, onSaveSuccess, onSaveError);
            } else {
                Productivity.save(vm.productivity, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
