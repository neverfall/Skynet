(function() {
    'use strict';
    angular
        .module('skynetApp')
        .factory('Gamification', Gamification);

    Gamification.$inject = ['$resource'];

    function Gamification ($resource) {
        var resourceUrl =  'api/gamifications/:id';

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
