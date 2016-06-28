(function() {
    'use strict';

    angular
        .module('skynetApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('k-pi', {
            parent: 'entity',
            url: '/k-pi',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'KPIS'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/k-pi/k-pis.html',
                    controller: 'KPIController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('k-pi-detail', {
            parent: 'entity',
            url: '/k-pi/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'KPI'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/k-pi/k-pi-detail.html',
                    controller: 'KPIDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'KPI', function($stateParams, KPI) {
                    return KPI.get({id : $stateParams.id});
                }]
            }
        })
        .state('k-pi.new', {
            parent: 'k-pi',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/k-pi/k-pi-dialog.html',
                    controller: 'KPIDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('k-pi', null, { reload: true });
                }, function() {
                    $state.go('k-pi');
                });
            }]
        })
        .state('k-pi.edit', {
            parent: 'k-pi',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/k-pi/k-pi-dialog.html',
                    controller: 'KPIDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['KPI', function(KPI) {
                            return KPI.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('k-pi', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('k-pi.delete', {
            parent: 'k-pi',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/k-pi/k-pi-delete-dialog.html',
                    controller: 'KPIDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['KPI', function(KPI) {
                            return KPI.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('k-pi', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
