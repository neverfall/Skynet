(function() {
    'use strict';

    angular
        .module('skynetApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('home', {
            parent: 'app',
            url: '/',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/home/home.html',
                    controller: 'HomeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('Gamification', {
            parent: 'app',
            url: '/gamification',
            data: {
                authorities: [],
                pageTitle: 'Gamification'
            },
            views: {
                'content@': {
                    templateUrl: 'app/gamification/gamification.html',
                    controller: 'GamificationController',
                    controllerAs: 'vm'
                }
            }
        });
    }
})();
