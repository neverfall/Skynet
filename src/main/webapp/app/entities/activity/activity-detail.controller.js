(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('ActivityDetailController', ActivityDetailController);

    ActivityDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Activity', 'Participant'];

    function ActivityDetailController($scope, $rootScope, $stateParams, entity, Activity, Participant) {
        var vm = this;
        vm.activity = entity;
        
        var unsubscribe = $rootScope.$on('skynetApp:activityUpdate', function(event, result) {
            vm.activity = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
