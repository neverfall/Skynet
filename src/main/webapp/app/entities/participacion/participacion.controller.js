(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('ParticipacionController', ParticipacionController);

    ParticipacionController.$inject = ['$scope', '$state', 'Participacion'];

    function ParticipacionController ($scope, $state, Participacion) {
        var vm = this;
        vm.participacions = [];
        vm.loadAll = function() {
            Participacion.query(function(result) {
                vm.participacions = result;
            });
        };

        vm.loadAll();
        
    }
})();
