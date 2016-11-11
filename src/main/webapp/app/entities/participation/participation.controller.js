(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('ParticipationController', ParticipationController);

    ParticipationController.$inject = ['$scope', '$state', 'Participation'];

    function ParticipationController ($scope, $state, Participation) {
        var vm = this;
        vm.participations = [];
        vm.loadAll = function() {
            Participation.query(function(result) {
                vm.participations = result;
            });
        };

        vm.loadAll();
        
    }
})();
