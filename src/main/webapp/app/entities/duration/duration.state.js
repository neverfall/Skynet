(function() {
    'use strict';

    angular
        .module('skynetApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('duration', {
            parent: 'entity',
            url: '/duration',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Durations'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/duration/durations.html',
                    controller: 'DurationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('duration-detail', {
            parent: 'entity',
            url: '/duration/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Duration'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/duration/duration-detail.html',
                    controller: 'DurationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Duration', function($stateParams, Duration) {
                    return Duration.get({id : $stateParams.id});
                }]
            }
        })
        .state('duration.new', {
            parent: 'duration',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/duration/duration-dialog.html',
                    controller: 'DurationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                value: null,
                                compliance: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('duration', null, { reload: true });
                }, function() {
                    $state.go('duration');
                });
            }]
        })
        .state('duration.edit', {
            parent: 'duration',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/duration/duration-dialog.html',
                    controller: 'DurationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Duration', function(Duration) {
                            return Duration.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('duration', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('duration.delete', {
            parent: 'duration',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/duration/duration-delete-dialog.html',
                    controller: 'DurationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Duration', function(Duration) {
                            return Duration.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('duration', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
