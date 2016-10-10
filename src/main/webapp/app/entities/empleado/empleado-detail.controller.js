(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('EmpleadoDetailController', EmpleadoDetailController);

    EmpleadoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Empleado'];

    function EmpleadoDetailController($scope, $rootScope, $stateParams, entity, Empleado) {
        var vm = this;
        vm.empleado = entity;
        
        var unsubscribe = $rootScope.$on('skynetApp:empleadoUpdate', function(event, result) {
            vm.empleado = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
