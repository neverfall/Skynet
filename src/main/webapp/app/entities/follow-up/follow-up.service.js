(function() {
    'use strict';
    angular
        .module('skynetApp')
        .factory('FollowUp', FollowUp);

    FollowUp.$inject = ['$resource'];

    function FollowUp ($resource) {
        var resourceUrl =  'api/follow-ups/:id';

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
