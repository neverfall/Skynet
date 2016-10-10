(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('ParticipacionDetailController', ParticipacionDetailController);

    ParticipacionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Participacion', 'Actividad', 'Empleado'];

    function ParticipacionDetailController($scope, $rootScope, $stateParams, entity, Participacion, Actividad, Empleado) {
        var vm = this;
        vm.participacion = entity;
        
        var unsubscribe = $rootScope.$on('skynetApp:participacionUpdate', function(event, result) {
            vm.participacion = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
