(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('UsuarioDeleteController',UsuarioDeleteController);

    UsuarioDeleteController.$inject = ['$uibModalInstance', 'entity', 'Usuario'];

    function UsuarioDeleteController($uibModalInstance, entity, Usuario) {
        var vm = this;
        vm.usuario = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Usuario.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
