(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('KPIDialogController', KPIDialogController);

    KPIDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'KPI', 'Collaborator', 'Duration', 'Productivity', 'FollowUp', 'CustomerSatisfactionNPS', 'CustomerSatisfactionGrade'];

    function KPIDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, KPI, Collaborator, Duration, Productivity, FollowUp, CustomerSatisfactionNPS, CustomerSatisfactionGrade) {
        var vm = this;
        vm.kPI = entity;
        vm.collaborators = Collaborator.query();
        vm.durations = Duration.query({filter: 'kpi-is-null'});
        $q.all([vm.kPI.$promise, vm.durations.$promise]).then(function() {
            if (!vm.kPI.duration || !vm.kPI.duration.id) {
                return $q.reject();
            }
            return Duration.get({id : vm.kPI.duration.id}).$promise;
        }).then(function(duration) {
            vm.durations.push(duration);
        });
        vm.productivitys = Productivity.query({filter: 'kpi-is-null'});
        $q.all([vm.kPI.$promise, vm.productivitys.$promise]).then(function() {
            if (!vm.kPI.productivity || !vm.kPI.productivity.id) {
                return $q.reject();
            }
            return Productivity.get({id : vm.kPI.productivity.id}).$promise;
        }).then(function(productivity) {
            vm.productivities.push(productivity);
        });
        vm.followups = FollowUp.query({filter: 'kpi-is-null'});
        $q.all([vm.kPI.$promise, vm.followups.$promise]).then(function() {
            if (!vm.kPI.followUp || !vm.kPI.followUp.id) {
                return $q.reject();
            }
            return FollowUp.get({id : vm.kPI.followUp.id}).$promise;
        }).then(function(followUp) {
            vm.followups.push(followUp);
        });
        vm.customersatisfactionnpss = CustomerSatisfactionNPS.query({filter: 'kpi-is-null'});
        $q.all([vm.kPI.$promise, vm.customersatisfactionnpss.$promise]).then(function() {
            if (!vm.kPI.customerSatisfactionNPS || !vm.kPI.customerSatisfactionNPS.id) {
                return $q.reject();
            }
            return CustomerSatisfactionNPS.get({id : vm.kPI.customerSatisfactionNPS.id}).$promise;
        }).then(function(customerSatisfactionNPS) {
            vm.customersatisfactionnps.push(customerSatisfactionNPS);
        });
        vm.customersatisfactiongrades = CustomerSatisfactionGrade.query({filter: 'kpi-is-null'});
        $q.all([vm.kPI.$promise, vm.customersatisfactiongrades.$promise]).then(function() {
            if (!vm.kPI.customerSatisfactionGrade || !vm.kPI.customerSatisfactionGrade.id) {
                return $q.reject();
            }
            return CustomerSatisfactionGrade.get({id : vm.kPI.customerSatisfactionGrade.id}).$promise;
        }).then(function(customerSatisfactionGrade) {
            vm.customersatisfactiongrades.push(customerSatisfactionGrade);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('skynetApp:kPIUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.kPI.id !== null) {
                KPI.update(vm.kPI, onSaveSuccess, onSaveError);
            } else {
                KPI.save(vm.kPI, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
