(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('ParticipantController', ParticipantController);

    ParticipantController.$inject = ['$scope', '$state', 'Participant'];

    function ParticipantController ($scope, $state, Participant) {
        var vm = this;
        vm.participants = [];
        vm.loadAll = function() {
            Participant.query(function(result) {
                vm.participants = result;
            });
        };

        vm.loadAll();
        
    }
})();
