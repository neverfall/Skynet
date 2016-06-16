(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('UsuarioController', UsuarioController);

    UsuarioController.$inject = ['$scope', '$state', 'Usuario'];

    function UsuarioController ($scope, $state, Usuario) {
        var vm = this;
        vm.usuarios = [];
        vm.loadAll = function() {
            Usuario.query(function(result) {
                vm.usuarios = result;
            });
        };

        vm.loadAll();
        
    }
})();
