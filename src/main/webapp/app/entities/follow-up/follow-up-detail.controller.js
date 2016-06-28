(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('FollowUpDetailController', FollowUpDetailController);

    FollowUpDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'FollowUp'];

    function FollowUpDetailController($scope, $rootScope, $stateParams, entity, FollowUp) {
        var vm = this;
        vm.followUp = entity;
        
        var unsubscribe = $rootScope.$on('skynetApp:followUpUpdate', function(event, result) {
            vm.followUp = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
