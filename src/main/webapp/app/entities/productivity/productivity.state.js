(function() {
    'use strict';

    angular
        .module('skynetApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('productivity', {
            parent: 'entity',
            url: '/productivity',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Productivities'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/productivity/productivities.html',
                    controller: 'ProductivityController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('productivity-detail', {
            parent: 'entity',
            url: '/productivity/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Productivity'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/productivity/productivity-detail.html',
                    controller: 'ProductivityDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Productivity', function($stateParams, Productivity) {
                    return Productivity.get({id : $stateParams.id});
                }]
            }
        })
        .state('productivity.new', {
            parent: 'productivity',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/productivity/productivity-dialog.html',
                    controller: 'ProductivityDialogController',
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
                    $state.go('productivity', null, { reload: true });
                }, function() {
                    $state.go('productivity');
                });
            }]
        })
        .state('productivity.edit', {
            parent: 'productivity',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/productivity/productivity-dialog.html',
                    controller: 'ProductivityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Productivity', function(Productivity) {
                            return Productivity.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('productivity', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('productivity.delete', {
            parent: 'productivity',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/productivity/productivity-delete-dialog.html',
                    controller: 'ProductivityDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Productivity', function(Productivity) {
                            return Productivity.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('productivity', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
