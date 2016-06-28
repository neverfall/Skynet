(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('FollowUpController', FollowUpController);

    FollowUpController.$inject = ['$scope', '$state', 'FollowUp'];

    function FollowUpController ($scope, $state, FollowUp) {
        var vm = this;
        vm.followUps = [];
        vm.loadAll = function() {
            FollowUp.query(function(result) {
                vm.followUps = result;
            });
        };

        vm.loadAll();
        
    }
})();
