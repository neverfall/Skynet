(function() {
    'use strict';
    angular
        .module('skynetApp')
        .factory('Actividad', Actividad);

    Actividad.$inject = ['$resource'];

    function Actividad ($resource) {
        var resourceUrl =  'api/actividads/:id';

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
