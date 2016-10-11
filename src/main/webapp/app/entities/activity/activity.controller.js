(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('ActivityController', ActivityController);

    ActivityController.$inject = ['$scope', '$state', 'Activity'];

    function ActivityController ($scope, $state, Activity) {
        var vm = this;
        vm.activities = [];
        vm.loadAll = function() {
            Activity.query(function(result) {
                vm.activities = result;
            });
        };

        vm.loadAll();
        
    }
})();
