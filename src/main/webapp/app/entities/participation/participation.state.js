(function() {
    'use strict';

    angular
        .module('skynetApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('participation', {
            parent: 'entity',
            url: '/participation',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Participations'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/participation/participations.html',
                    controller: 'ParticipationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('participation-detail', {
            parent: 'entity',
            url: '/participation/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Participation'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/participation/participation-detail.html',
                    controller: 'ParticipationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Participation', function($stateParams, Participation) {
                    return Participation.get({id : $stateParams.id});
                }]
            }
        })
        .state('participation.new', {
            parent: 'participation',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/participation/participation-dialog.html',
                    controller: 'ParticipationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('participation', null, { reload: true });
                }, function() {
                    $state.go('participation');
                });
            }]
        })
        .state('participation.edit', {
            parent: 'participation',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/participation/participation-dialog.html',
                    controller: 'ParticipationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Participation', function(Participation) {
                            return Participation.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('participation', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('participation.delete', {
            parent: 'participation',
            url: '/?id/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/participation/participation-delete-dialog.html',
                    templateUrl: 'app/entities/participation/participation-delete-dialog.html',
                    controller: 'ParticipationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Participation', function(Participation) {
                            console.log($stateParams.id);
                            return $stateParams.id;
                        }]
                    }
                }).result.then(function() {
                    $state.go('participation', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
