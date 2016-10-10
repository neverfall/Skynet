(function() {
    'use strict';
    angular
        .module('skynetApp')
        .factory('Empleado', Empleado);

    Empleado.$inject = ['$resource'];

    function Empleado ($resource) {
        var resourceUrl =  'api/empleados/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
