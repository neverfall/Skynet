'use strict';

describe('Controller Tests', function() {

    describe('Participacion Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockParticipacion, MockActividad, MockEmpleado;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockParticipacion = jasmine.createSpy('MockParticipacion');
            MockActividad = jasmine.createSpy('MockActividad');
            MockEmpleado = jasmine.createSpy('MockEmpleado');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Participacion': MockParticipacion,
                'Actividad': MockActividad,
                'Empleado': MockEmpleado
            };
            createController = function() {
                $injector.get('$controller')("ParticipacionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'skynetApp:participacionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
