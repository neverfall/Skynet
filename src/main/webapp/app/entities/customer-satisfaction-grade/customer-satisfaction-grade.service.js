(function() {
    'use strict';
    angular
        .module('skynetApp')
        .factory('CustomerSatisfactionGrade', CustomerSatisfactionGrade);

    CustomerSatisfactionGrade.$inject = ['$resource'];

    function CustomerSatisfactionGrade ($resource) {
        var resourceUrl =  'api/customer-satisfaction-grades/:id';

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
