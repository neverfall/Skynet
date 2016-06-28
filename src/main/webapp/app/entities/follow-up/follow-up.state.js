(function() {
    'use strict';

    angular
        .module('skynetApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('follow-up', {
            parent: 'entity',
            url: '/follow-up',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'FollowUps'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/follow-up/follow-ups.html',
                    controller: 'FollowUpController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('follow-up-detail', {
            parent: 'entity',
            url: '/follow-up/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'FollowUp'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/follow-up/follow-up-detail.html',
                    controller: 'FollowUpDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'FollowUp', function($stateParams, FollowUp) {
                    return FollowUp.get({id : $stateParams.id});
                }]
            }
        })
        .state('follow-up.new', {
            parent: 'follow-up',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/follow-up/follow-up-dialog.html',
                    controller: 'FollowUpDialogController',
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
                    $state.go('follow-up', null, { reload: true });
                }, function() {
                    $state.go('follow-up');
                });
            }]
        })
        .state('follow-up.edit', {
            parent: 'follow-up',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/follow-up/follow-up-dialog.html',
                    controller: 'FollowUpDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FollowUp', function(FollowUp) {
                            return FollowUp.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('follow-up', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('follow-up.delete', {
            parent: 'follow-up',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/follow-up/follow-up-delete-dialog.html',
                    controller: 'FollowUpDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FollowUp', function(FollowUp) {
                            return FollowUp.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('follow-up', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
