(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('KPIDetailController', KPIDetailController);

    KPIDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'KPI', 'Collaborator', 'Duration', 'Productivity', 'FollowUp', 'CustomerSatisfactionNPS', 'CustomerSatisfactionGrade'];

    function KPIDetailController($scope, $rootScope, $stateParams, entity, KPI, Collaborator, Duration, Productivity, FollowUp, CustomerSatisfactionNPS, CustomerSatisfactionGrade) {
        var vm = this;
        vm.kPI = entity;
        
        var unsubscribe = $rootScope.$on('skynetApp:kPIUpdate', function(event, result) {
            vm.kPI = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
