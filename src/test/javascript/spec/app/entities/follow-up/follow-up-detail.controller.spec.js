'use strict';

describe('Controller Tests', function() {

    describe('FollowUp Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockFollowUp;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockFollowUp = jasmine.createSpy('MockFollowUp');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'FollowUp': MockFollowUp
            };
            createController = function() {
                $injector.get('$controller')("FollowUpDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'skynetApp:followUpUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
