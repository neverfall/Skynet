(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('ProductivityController', ProductivityController);

    ProductivityController.$inject = ['$scope', '$state', 'Productivity'];

    function ProductivityController ($scope, $state, Productivity) {
        var vm = this;
        vm.productivities = [];
        vm.loadAll = function() {
            Productivity.query(function(result) {
                vm.productivities = result;
            });
        };

        vm.loadAll();
        
    }
})();
