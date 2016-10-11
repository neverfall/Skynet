(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('EmployeeDetailController', EmployeeDetailController);

    EmployeeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Employee'];

    function EmployeeDetailController($scope, $rootScope, $stateParams, entity, Employee) {
        var vm = this;
        vm.employee = entity;
        
        var unsubscribe = $rootScope.$on('skynetApp:employeeUpdate', function(event, result) {
            vm.employee = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
