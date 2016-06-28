(function() {
    'use strict';
    angular
        .module('skynetApp')
        .factory('KPI', KPI);

    KPI.$inject = ['$resource'];

    function KPI ($resource) {
        var resourceUrl =  'api/k-pis/:id';

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
