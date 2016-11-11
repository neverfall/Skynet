(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('ParticipationDetailController', ParticipationDetailController);

    ParticipationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Participation'];

    function ParticipationDetailController($scope, $rootScope, $stateParams, entity, Participation) {
        var vm = this;
        vm.participation = entity;
        var unsubscribe = $rootScope.$on('skynetApp:participationUpdate', function(event, result) {
            vm.participation = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
