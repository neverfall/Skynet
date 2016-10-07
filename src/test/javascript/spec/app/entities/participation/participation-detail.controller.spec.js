'use strict';

describe('Controller Tests', function() {

    describe('Participation Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockParticipation, MockActivity, MockParticipant;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockParticipation = jasmine.createSpy('MockParticipation');
            MockActivity = jasmine.createSpy('MockActivity');
            MockParticipant = jasmine.createSpy('MockParticipant');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Participation': MockParticipation,
                'Activity': MockActivity,
                'Participant': MockParticipant
            };
            createController = function() {
                $injector.get('$controller')("ParticipationDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'skynetApp:participationUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
