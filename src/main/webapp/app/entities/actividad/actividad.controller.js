(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('ActividadController', ActividadController);

    ActividadController.$inject = ['$scope', '$state', 'Actividad'];

    function ActividadController ($scope, $state, Actividad) {
        var vm = this;
        vm.actividads = [];
        vm.loadAll = function() {
            Actividad.query(function(result) {
                vm.actividads = result;
            });
        };

        vm.loadAll();
        
    }
})();
