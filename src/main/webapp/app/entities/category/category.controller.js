(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('CategoryController', CategoryController);

    CategoryController.$inject = ['$scope', '$state', 'Category'];

    function CategoryController ($scope, $state, Category) {
        var vm = this;
        vm.categories = [];
        vm.loadAll = function() {
            Category.query(function(result) {
                vm.categories = result;
            });
        };

        vm.loadAll();
        
    }
})();
