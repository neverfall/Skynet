(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('EmpleadoController', EmpleadoController);

    EmpleadoController.$inject = ['$scope', '$state', 'Empleado'];

    function EmpleadoController ($scope, $state, Empleado) {
        var vm = this;
        vm.empleados = [];
        vm.loadAll = function() {
            Empleado.query(function(result) {
                vm.empleados = result;
            });
        };

        vm.loadAll();
        
    }
})();
