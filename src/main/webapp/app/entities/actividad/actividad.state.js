(function() {
    'use strict';

    angular
        .module('skynetApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('actividad', {
            parent: 'entity',
            url: '/actividad',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Actividads'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/actividad/actividads.html',
                    controller: 'ActividadController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('actividad-detail', {
            parent: 'entity',
            url: '/actividad/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Actividad'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/actividad/actividad-detail.html',
                    controller: 'ActividadDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Actividad', function($stateParams, Actividad) {
                    return Actividad.get({id : $stateParams.id});
                }]
            }
        })
        .state('actividad.new', {
            parent: 'actividad',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/actividad/actividad-dialog.html',
                    controller: 'ActividadDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nombre: null,
                                puntos: null,
                                descripcion: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('actividad', null, { reload: true });
                }, function() {
                    $state.go('actividad');
                });
            }]
        })
        .state('actividad.edit', {
            parent: 'actividad',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/actividad/actividad-dialog.html',
                    controller: 'ActividadDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Actividad', function(Actividad) {
                            return Actividad.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('actividad', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('actividad.delete', {
            parent: 'actividad',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/actividad/actividad-delete-dialog.html',
                    controller: 'ActividadDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Actividad', function(Actividad) {
                            return Actividad.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('actividad', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
