(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('ParticipationController', ParticipationController);

    ParticipationController.$inject = ['$scope', '$state', 'User' ,'Participation'];

    function ParticipationController ($scope, $state, User, Participation) {
        var vm = this;
        vm.participations = [];
        vm.nonAdmins = [];

        vm.loadAll = function() {
            Participation.query(function(result) {
                vm.participations = result;
            });
        };

        vm.loadAll();

    }
})();
