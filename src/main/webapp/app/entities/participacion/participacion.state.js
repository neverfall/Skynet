(function() {
    'use strict';

    angular
        .module('skynetApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('participacion', {
            parent: 'entity',
            url: '/participacion',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Participacions'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/participacion/participacions.html',
                    controller: 'ParticipacionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('participacion-detail', {
            parent: 'entity',
            url: '/participacion/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Participacion'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/participacion/participacion-detail.html',
                    controller: 'ParticipacionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Participacion', function($stateParams, Participacion) {
                    return Participacion.get({id : $stateParams.id});
                }]
            }
        })
        .state('participacion.new', {
            parent: 'participacion',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/participacion/participacion-dialog.html',
                    controller: 'ParticipacionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                fecha: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('participacion', null, { reload: true });
                }, function() {
                    $state.go('participacion');
                });
            }]
        })
        .state('participacion.edit', {
            parent: 'participacion',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/participacion/participacion-dialog.html',
                    controller: 'ParticipacionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Participacion', function(Participacion) {
                            return Participacion.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('participacion', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('participacion.delete', {
            parent: 'participacion',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/participacion/participacion-delete-dialog.html',
                    controller: 'ParticipacionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Participacion', function(Participacion) {
                            return Participacion.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('participacion', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
