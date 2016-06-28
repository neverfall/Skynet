(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('CustomerSatisfactionGradeController', CustomerSatisfactionGradeController);

    CustomerSatisfactionGradeController.$inject = ['$scope', '$state', 'CustomerSatisfactionGrade'];

    function CustomerSatisfactionGradeController ($scope, $state, CustomerSatisfactionGrade) {
        var vm = this;
        vm.customerSatisfactionGrades = [];
        vm.loadAll = function() {
            CustomerSatisfactionGrade.query(function(result) {
                vm.customerSatisfactionGrades = result;
            });
        };

        vm.loadAll();
        
    }
})();
