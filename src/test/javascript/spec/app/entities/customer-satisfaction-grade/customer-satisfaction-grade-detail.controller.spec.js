'use strict';

describe('Controller Tests', function() {

    describe('CustomerSatisfactionGrade Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCustomerSatisfactionGrade;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCustomerSatisfactionGrade = jasmine.createSpy('MockCustomerSatisfactionGrade');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'CustomerSatisfactionGrade': MockCustomerSatisfactionGrade
            };
            createController = function() {
                $injector.get('$controller')("CustomerSatisfactionGradeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'skynetApp:customerSatisfactionGradeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
