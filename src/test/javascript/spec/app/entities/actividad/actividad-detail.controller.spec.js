'use strict';

describe('Controller Tests', function() {

    describe('Actividad Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockActividad, MockEmpleado;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockActividad = jasmine.createSpy('MockActividad');
            MockEmpleado = jasmine.createSpy('MockEmpleado');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Actividad': MockActividad,
                'Empleado': MockEmpleado
            };
            createController = function() {
                $injector.get('$controller')("ActividadDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'skynetApp:actividadUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
