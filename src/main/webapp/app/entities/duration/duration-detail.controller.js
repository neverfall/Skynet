(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('DurationDetailController', DurationDetailController);

    DurationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Duration'];

    function DurationDetailController($scope, $rootScope, $stateParams, entity, Duration) {
        var vm = this;
        vm.duration = entity;
        
        var unsubscribe = $rootScope.$on('skynetApp:durationUpdate', function(event, result) {
            vm.duration = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
