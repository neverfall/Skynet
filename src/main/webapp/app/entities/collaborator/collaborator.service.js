(function() {
    'use strict';
    angular
        .module('skynetApp')
        .factory('Collaborator', Collaborator);

    Collaborator.$inject = ['$resource'];

    function Collaborator ($resource) {
        var resourceUrl =  'api/collaborators/:id';

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
