(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('ParticipationDialogController', ParticipationDialogController);

    ParticipationDialogController.$inject = ['$q','Principal','$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Participation', 'Activity', 'User'];

    function ParticipationDialogController ($q, Principal, $timeout, $scope, $stateParams, $uibModalInstance, entity, Participation, Activity, User) {
        var vm = this;
        vm.participation = entity;
        vm.activities = Activity.query();
        vm.users = User.query();
        vm.participationWrapper = {};

        vm.load = function() {
            Activity.getByEmail(function(result) {
                vm.myActivities = result;
            });
        };
        vm.load();

        vm.currentAccount = null;

        Principal.identity().then(function(account) {
            vm.currentAccount = account;
        });


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
            vm.participationWrapper.participation = vm.participation;
            vm.participationWrapper.users = vm.pickedUsers;
            console.log(vm.participationWrapper);
            if (vm.participation.id !== null) {
                Participation.update(vm.participationWrapper, onSaveSuccess, onSaveError);
            } else {
                Participation.save(vm.participationWrapper, onSaveSuccess, onSaveError);
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
