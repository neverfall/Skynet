(function() {
    'use strict';

    angular
        .module('skynetApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('customer-satisfaction-nps', {
            parent: 'entity',
            url: '/customer-satisfaction-nps',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'CustomerSatisfactionNPS'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/customer-satisfaction-nps/customer-satisfaction-nps.html',
                    controller: 'CustomerSatisfactionNPSController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('customer-satisfaction-nps-detail', {
            parent: 'entity',
            url: '/customer-satisfaction-nps/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'CustomerSatisfactionNPS'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/customer-satisfaction-nps/customer-satisfaction-nps-detail.html',
                    controller: 'CustomerSatisfactionNPSDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'CustomerSatisfactionNPS', function($stateParams, CustomerSatisfactionNPS) {
                    return CustomerSatisfactionNPS.get({id : $stateParams.id});
                }]
            }
        })
        .state('customer-satisfaction-nps.new', {
            parent: 'customer-satisfaction-nps',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customer-satisfaction-nps/customer-satisfaction-nps-dialog.html',
                    controller: 'CustomerSatisfactionNPSDialogController',
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
                    $state.go('customer-satisfaction-nps', null, { reload: true });
                }, function() {
                    $state.go('customer-satisfaction-nps');
                });
            }]
        })
        .state('customer-satisfaction-nps.edit', {
            parent: 'customer-satisfaction-nps',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customer-satisfaction-nps/customer-satisfaction-nps-dialog.html',
                    controller: 'CustomerSatisfactionNPSDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CustomerSatisfactionNPS', function(CustomerSatisfactionNPS) {
                            return CustomerSatisfactionNPS.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('customer-satisfaction-nps', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('customer-satisfaction-nps.delete', {
            parent: 'customer-satisfaction-nps',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customer-satisfaction-nps/customer-satisfaction-nps-delete-dialog.html',
                    controller: 'CustomerSatisfactionNPSDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CustomerSatisfactionNPS', function(CustomerSatisfactionNPS) {
                            return CustomerSatisfactionNPS.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('customer-satisfaction-nps', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
