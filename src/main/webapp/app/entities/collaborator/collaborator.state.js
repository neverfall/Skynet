(function() {
    'use strict';

    angular
        .module('skynetApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('collaborator', {
            parent: 'entity',
            url: '/collaborator',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Collaborators'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/collaborator/collaborators.html',
                    controller: 'CollaboratorController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('collaborator-detail', {
            parent: 'entity',
            url: '/collaborator/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Collaborator'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/collaborator/collaborator-detail.html',
                    controller: 'CollaboratorDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Collaborator', function($stateParams, Collaborator) {
                    return Collaborator.get({id : $stateParams.id});
                }]
            }
        })
        .state('collaborator.new', {
            parent: 'collaborator',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/collaborator/collaborator-dialog.html',
                    controller: 'CollaboratorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                lastName: null,
                                email: null,
                                apprentice: false,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('collaborator', null, { reload: true });
                }, function() {
                    $state.go('collaborator');
                });
            }]
        })
        .state('collaborator.edit', {
            parent: 'collaborator',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/collaborator/collaborator-dialog.html',
                    controller: 'CollaboratorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Collaborator', function(Collaborator) {
                            return Collaborator.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('collaborator', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('collaborator.delete', {
            parent: 'collaborator',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/collaborator/collaborator-delete-dialog.html',
                    controller: 'CollaboratorDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Collaborator', function(Collaborator) {
                            return Collaborator.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('collaborator', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
