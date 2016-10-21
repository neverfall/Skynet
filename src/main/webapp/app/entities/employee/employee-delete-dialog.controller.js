(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('EmployeeDeleteController',EmployeeDeleteController);

    EmployeeDeleteController.$inject = ['$uibModalInstance', 'entity', 'Employee'];

    function EmployeeDeleteController($uibModalInstance, entity, Employee) {
        var vm = this;
        vm.employee = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        var onDeleteSuccess = function () {
            $uibModalInstance.close(true);
        };

        vm.confirmDelete = function (id) {
            vm.employee.active = false;
            Employee.update(vm.employee, onDeleteSuccess);
        };
    }
})();
