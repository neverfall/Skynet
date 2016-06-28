'use strict';

describe('Controller Tests', function() {

    describe('Collaborator Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCollaborator, MockKPI, MockGamification;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCollaborator = jasmine.createSpy('MockCollaborator');
            MockKPI = jasmine.createSpy('MockKPI');
            MockGamification = jasmine.createSpy('MockGamification');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Collaborator': MockCollaborator,
                'KPI': MockKPI,
                'Gamification': MockGamification
            };
            createController = function() {
                $injector.get('$controller')("CollaboratorDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'skynetApp:collaboratorUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
