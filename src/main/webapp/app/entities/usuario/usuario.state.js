(function() {
    'use strict';

    angular
        .module('skynetApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('usuario', {
            parent: 'entity',
            url: '/usuario',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Usuarios'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/usuario/usuarios.html',
                    controller: 'UsuarioController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('usuario-detail', {
            parent: 'entity',
            url: '/usuario/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Usuario'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/usuario/usuario-detail.html',
                    controller: 'UsuarioDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Usuario', function($stateParams, Usuario) {
                    return Usuario.get({id : $stateParams.id});
                }]
            }
        })
        .state('usuario.new', {
            parent: 'usuario',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/usuario/usuario-dialog.html',
                    controller: 'UsuarioDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                email: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('usuario', null, { reload: true });
                }, function() {
                    $state.go('usuario');
                });
            }]
        })
        .state('usuario.edit', {
            parent: 'usuario',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/usuario/usuario-dialog.html',
                    controller: 'UsuarioDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Usuario', function(Usuario) {
                            return Usuario.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('usuario', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('usuario.delete', {
            parent: 'usuario',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/usuario/usuario-delete-dialog.html',
                    controller: 'UsuarioDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Usuario', function(Usuario) {
                            return Usuario.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('usuario', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
