(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('DurationController', DurationController);

    DurationController.$inject = ['$scope', '$state', 'Duration'];

    function DurationController ($scope, $state, Duration) {
        var vm = this;
        vm.durations = [];
        vm.loadAll = function() {
            Duration.query(function(result) {
                vm.durations = result;
            });
        };

        vm.loadAll();
        
    }
})();
