'use strict';

describe('Controller Tests', function() {

    describe('CustomerSatisfactionNPS Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCustomerSatisfactionNPS;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCustomerSatisfactionNPS = jasmine.createSpy('MockCustomerSatisfactionNPS');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'CustomerSatisfactionNPS': MockCustomerSatisfactionNPS
            };
            createController = function() {
                $injector.get('$controller')("CustomerSatisfactionNPSDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'skynetApp:customerSatisfactionNPSUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
