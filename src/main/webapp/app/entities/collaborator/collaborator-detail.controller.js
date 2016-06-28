(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('CollaboratorDetailController', CollaboratorDetailController);

    CollaboratorDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Collaborator', 'KPI', 'Gamification'];

    function CollaboratorDetailController($scope, $rootScope, $stateParams, entity, Collaborator, KPI, Gamification) {
        var vm = this;
        vm.collaborator = entity;
        
        var unsubscribe = $rootScope.$on('skynetApp:collaboratorUpdate', function(event, result) {
            vm.collaborator = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
