(function() {
    'use strict';

    angular
        .module('skynetApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('activity', {
            parent: 'entity',
            url: '/activity',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'Activities'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/activity/activities.html',
                    controller: 'ActivityController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('activity-detail', {
            parent: 'entity',
            url: '/activity/{id}',
            data: {
                authorities: ['ROLE_ADMIN', 'ROLE_COORDINATOR'],
                pageTitle: 'Activity'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/activity/activity-detail.html',
                    controller: 'ActivityDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Activity', function($stateParams, Activity) {
                    return Activity.get({id : $stateParams.id});
                }]
            }
        })
        .state('activity.new', {
            parent: 'activity',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/activity/activity-dialog.html',
                    controller: 'ActivityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                points: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('activity', null, { reload: true });
                }, function() {
                    $state.go('activity');
                });
            }]
        })
        .state('activity.edit', {
            parent: 'activity',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/activity/activity-dialog.html',
                    controller: 'ActivityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Activity', function(Activity) {
                            return Activity.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('activity', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('activity.delete', {
            parent: 'activity',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/activity/activity-delete-dialog.html',
                    controller: 'ActivityDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Activity', function(Activity) {
                            return Activity.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('activity', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
