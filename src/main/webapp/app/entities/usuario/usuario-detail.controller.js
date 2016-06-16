(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('UsuarioDetailController', UsuarioDetailController);

    UsuarioDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Usuario'];

    function UsuarioDetailController($scope, $rootScope, $stateParams, entity, Usuario) {
        var vm = this;
        vm.usuario = entity;
        
        var unsubscribe = $rootScope.$on('skynetApp:usuarioUpdate', function(event, result) {
            vm.usuario = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
