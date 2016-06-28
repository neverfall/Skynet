(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('CollaboratorDialogController', CollaboratorDialogController);

    CollaboratorDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Collaborator', 'KPI', 'Gamification'];

    function CollaboratorDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Collaborator, KPI, Gamification) {
        var vm = this;
        vm.collaborator = entity;
        vm.kpis = KPI.query();
        vm.gamifications = Gamification.query({filter: 'collaborator-is-null'});
        $q.all([vm.collaborator.$promise, vm.gamifications.$promise]).then(function() {
            if (!vm.collaborator.gamification || !vm.collaborator.gamification.id) {
                return $q.reject();
            }
            return Gamification.get({id : vm.collaborator.gamification.id}).$promise;
        }).then(function(gamification) {
            vm.gamifications.push(gamification);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('skynetApp:collaboratorUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.collaborator.id !== null) {
                Collaborator.update(vm.collaborator, onSaveSuccess, onSaveError);
            } else {
                Collaborator.save(vm.collaborator, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
