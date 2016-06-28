(function() {
    'use strict';
    angular
        .module('skynetApp')
        .factory('Productivity', Productivity);

    Productivity.$inject = ['$resource'];

    function Productivity ($resource) {
        var resourceUrl =  'api/productivities/:id';

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
