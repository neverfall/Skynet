(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('GamificationDialogController', GamificationDialogController);

    GamificationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Gamification', 'Category'];

    function GamificationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Gamification, Category) {
        var vm = this;
        vm.gamification = entity;
        vm.categories = Category.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('skynetApp:gamificationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.gamification.id !== null) {
                Gamification.update(vm.gamification, onSaveSuccess, onSaveError);
            } else {
                Gamification.save(vm.gamification, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
