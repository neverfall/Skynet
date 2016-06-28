'use strict';

describe('Controller Tests', function() {

    describe('KPI Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockKPI, MockCollaborator, MockDuration, MockProductivity, MockFollowUp, MockCustomerSatisfactionNPS, MockCustomerSatisfactionGrade;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockKPI = jasmine.createSpy('MockKPI');
            MockCollaborator = jasmine.createSpy('MockCollaborator');
            MockDuration = jasmine.createSpy('MockDuration');
            MockProductivity = jasmine.createSpy('MockProductivity');
            MockFollowUp = jasmine.createSpy('MockFollowUp');
            MockCustomerSatisfactionNPS = jasmine.createSpy('MockCustomerSatisfactionNPS');
            MockCustomerSatisfactionGrade = jasmine.createSpy('MockCustomerSatisfactionGrade');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'KPI': MockKPI,
                'Collaborator': MockCollaborator,
                'Duration': MockDuration,
                'Productivity': MockProductivity,
                'FollowUp': MockFollowUp,
                'CustomerSatisfactionNPS': MockCustomerSatisfactionNPS,
                'CustomerSatisfactionGrade': MockCustomerSatisfactionGrade
            };
            createController = function() {
                $injector.get('$controller')("KPIDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'skynetApp:kPIUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
