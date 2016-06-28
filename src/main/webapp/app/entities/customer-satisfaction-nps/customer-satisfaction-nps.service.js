(function() {
    'use strict';
    angular
        .module('skynetApp')
        .factory('CustomerSatisfactionNPS', CustomerSatisfactionNPS);

    CustomerSatisfactionNPS.$inject = ['$resource'];

    function CustomerSatisfactionNPS ($resource) {
        var resourceUrl =  'api/customer-satisfaction-nps/:id';

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
