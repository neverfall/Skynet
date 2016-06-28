(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('GamificationController', GamificationController);

    GamificationController.$inject = ['$scope', '$state', 'Gamification'];

    function GamificationController ($scope, $state, Gamification) {
        var vm = this;
        vm.gamifications = [];
        vm.loadAll = function() {
            Gamification.query(function(result) {
                vm.gamifications = result;
            });
        };

        vm.loadAll();
        
    }
})();
