(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('ActividadDetailController', ActividadDetailController);

    ActividadDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Actividad', 'Empleado'];

    function ActividadDetailController($scope, $rootScope, $stateParams, entity, Actividad, Empleado) {
        var vm = this;
        vm.actividad = entity;
        
        var unsubscribe = $rootScope.$on('skynetApp:actividadUpdate', function(event, result) {
            vm.actividad = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
