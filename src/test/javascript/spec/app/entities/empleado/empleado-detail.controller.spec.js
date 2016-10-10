'use strict';

describe('Controller Tests', function() {

    describe('Empleado Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockEmpleado;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockEmpleado = jasmine.createSpy('MockEmpleado');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Empleado': MockEmpleado
            };
            createController = function() {
                $injector.get('$controller')("EmpleadoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'skynetApp:empleadoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
