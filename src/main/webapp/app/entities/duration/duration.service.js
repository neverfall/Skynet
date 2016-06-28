(function() {
    'use strict';
    angular
        .module('skynetApp')
        .factory('Duration', Duration);

    Duration.$inject = ['$resource'];

    function Duration ($resource) {
        var resourceUrl =  'api/durations/:id';

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
