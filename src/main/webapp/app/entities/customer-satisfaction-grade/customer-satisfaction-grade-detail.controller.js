(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('CustomerSatisfactionGradeDetailController', CustomerSatisfactionGradeDetailController);

    CustomerSatisfactionGradeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'CustomerSatisfactionGrade'];

    function CustomerSatisfactionGradeDetailController($scope, $rootScope, $stateParams, entity, CustomerSatisfactionGrade) {
        var vm = this;
        vm.customerSatisfactionGrade = entity;
        
        var unsubscribe = $rootScope.$on('skynetApp:customerSatisfactionGradeUpdate', function(event, result) {
            vm.customerSatisfactionGrade = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
