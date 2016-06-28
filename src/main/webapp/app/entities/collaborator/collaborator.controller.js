(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('CollaboratorController', CollaboratorController);

    CollaboratorController.$inject = ['$scope', '$state', 'Collaborator'];

    function CollaboratorController ($scope, $state, Collaborator) {
        var vm = this;
        vm.collaborators = [];
        vm.loadAll = function() {
            Collaborator.query(function(result) {
                vm.collaborators = result;
            });
        };

        vm.loadAll();
        
    }
})();
