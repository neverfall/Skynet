(function() {
    'use strict';

    angular
        .module('skynetApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('customer-satisfaction-grade', {
            parent: 'entity',
            url: '/customer-satisfaction-grade',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'CustomerSatisfactionGrades'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/customer-satisfaction-grade/customer-satisfaction-grades.html',
                    controller: 'CustomerSatisfactionGradeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('customer-satisfaction-grade-detail', {
            parent: 'entity',
            url: '/customer-satisfaction-grade/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'CustomerSatisfactionGrade'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/customer-satisfaction-grade/customer-satisfaction-grade-detail.html',
                    controller: 'CustomerSatisfactionGradeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'CustomerSatisfactionGrade', function($stateParams, CustomerSatisfactionGrade) {
                    return CustomerSatisfactionGrade.get({id : $stateParams.id});
                }]
            }
        })
        .state('customer-satisfaction-grade.new', {
            parent: 'customer-satisfaction-grade',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customer-satisfaction-grade/customer-satisfaction-grade-dialog.html',
                    controller: 'CustomerSatisfactionGradeDialogController',
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
                    $state.go('customer-satisfaction-grade', null, { reload: true });
                }, function() {
                    $state.go('customer-satisfaction-grade');
                });
            }]
        })
        .state('customer-satisfaction-grade.edit', {
            parent: 'customer-satisfaction-grade',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customer-satisfaction-grade/customer-satisfaction-grade-dialog.html',
                    controller: 'CustomerSatisfactionGradeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CustomerSatisfactionGrade', function(CustomerSatisfactionGrade) {
                            return CustomerSatisfactionGrade.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('customer-satisfaction-grade', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('customer-satisfaction-grade.delete', {
            parent: 'customer-satisfaction-grade',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customer-satisfaction-grade/customer-satisfaction-grade-delete-dialog.html',
                    controller: 'CustomerSatisfactionGradeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CustomerSatisfactionGrade', function(CustomerSatisfactionGrade) {
                            return CustomerSatisfactionGrade.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('customer-satisfaction-grade', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
