(function() {
    'use strict';

    angular
        .module('skynetApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('gamification', {
            parent: 'entity',
            url: '/gamification',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Gamifications'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/gamification/gamifications.html',
                    controller: 'GamificationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('gamification-detail', {
            parent: 'entity',
            url: '/gamification/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Gamification'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/gamification/gamification-detail.html',
                    controller: 'GamificationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Gamification', function($stateParams, Gamification) {
                    return Gamification.get({id : $stateParams.id});
                }]
            }
        })
        .state('gamification.new', {
            parent: 'gamification',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/gamification/gamification-dialog.html',
                    controller: 'GamificationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                yearlyScore: null,
                                periodScore: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('gamification', null, { reload: true });
                }, function() {
                    $state.go('gamification');
                });
            }]
        })
        .state('gamification.edit', {
            parent: 'gamification',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/gamification/gamification-dialog.html',
                    controller: 'GamificationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Gamification', function(Gamification) {
                            return Gamification.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('gamification', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('gamification.delete', {
            parent: 'gamification',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/gamification/gamification-delete-dialog.html',
                    controller: 'GamificationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Gamification', function(Gamification) {
                            return Gamification.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('gamification', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
