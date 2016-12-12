(function() {
    'use strict';

    angular
        .module('skynetApp')
        .controller('ParticipationController', ParticipationController);

    ParticipationController.$inject = ['$scope', '$state', 'User' ,'Participation'];

    function ParticipationController ($scope, $state, User, Participation) {
        var vm = this;
        vm.edit = 0;
        vm.disableDeleteButton = 1;
        vm.checkboxes = [];
        vm.checkboxIds = [];
        vm.participations = [];
        vm.deleteSome = function(){
            vm.edit = 0;
        }
        vm.addId = function(id) {
            var index = vm.checkboxIds.indexOf(id)
            if (index < 0 ) {
                vm.checkboxIds.push(id);
            } else {
                vm.checkboxIds.splice(index,1);
            }
            if(vm.checkboxIds.length == 0){
                vm.disableDeleteButton = 1;
            }else{
                vm.disableDeleteButton = 0;
            }
        }
        vm.loadAll = function() {
            Participation.query(function(result) {
                vm.participations = result;
            });
        };
        vm.loadAll();
    }
})();
