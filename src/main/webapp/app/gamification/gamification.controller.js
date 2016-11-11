(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('GamificationController', GamificationController);

    GamificationController.$inject = ['$scope', 'Activity'];

    function GamificationController ($scope, Activity) {
        var vm = this;
        vm.scores = [];
        vm.load = function() {
            Activity.getScore(function(result) {
                vm.scores = result;
                console.log(vm.scores);
            });
        };
        vm.load();
    }
})();
