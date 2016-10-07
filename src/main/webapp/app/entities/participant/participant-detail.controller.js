(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('ParticipantDetailController', ParticipantDetailController);

    ParticipantDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Participant'];

    function ParticipantDetailController($scope, $rootScope, $stateParams, entity, Participant) {
        var vm = this;
        vm.participant = entity;
        
        var unsubscribe = $rootScope.$on('skynetApp:participantUpdate', function(event, result) {
            vm.participant = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
