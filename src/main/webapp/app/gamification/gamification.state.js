(function() {
    'use strict';

    angular
        .module('skynetApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('gamification', {
            parent: 'app',
            url: '/gamification',
            data: {
                authorities: []
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
