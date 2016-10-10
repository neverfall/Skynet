(function() {
    'use strict';

    angular
        .module('skynetApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('empleado', {
            parent: 'entity',
            url: '/empleado',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Empleados'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/empleado/empleados.html',
                    controller: 'EmpleadoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('empleado-detail', {
            parent: 'entity',
            url: '/empleado/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Empleado'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/empleado/empleado-detail.html',
                    controller: 'EmpleadoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Empleado', function($stateParams, Empleado) {
                    return Empleado.get({id : $stateParams.id});
                }]
            }
        })
        .state('empleado.new', {
            parent: 'empleado',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/empleado/empleado-dialog.html',
                    controller: 'EmpleadoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nombre: null,
                                email: null,
                                rol: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('empleado', null, { reload: true });
                }, function() {
                    $state.go('empleado');
                });
            }]
        })
        .state('empleado.edit', {
            parent: 'empleado',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/empleado/empleado-dialog.html',
                    controller: 'EmpleadoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Empleado', function(Empleado) {
                            return Empleado.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('empleado', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('empleado.delete', {
            parent: 'empleado',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/empleado/empleado-delete-dialog.html',
                    controller: 'EmpleadoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Empleado', function(Empleado) {
                            return Empleado.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('empleado', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
