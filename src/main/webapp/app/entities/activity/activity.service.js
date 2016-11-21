(function() {
    'use strict';
    angular
        .module('skynetApp')
        .factory('Activity', Activity);

    Activity.$inject = ['$resource'];

    function Activity ($resource) {
        var resourceUrl =  'api/activities/:id';

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
            'getByEmail': {
                 url: 'api/activities/getByEmail' ,
                 method: 'GET',
                 transformResponse: function (data) {
                     if (data) {
                         data = angular.fromJson(data);
                     }
                     return data;
                 },
                 isArray: true
             },
            'getScore': {
                 url: 'api/gamification' ,
                 method: 'GET',
                 transformResponse: function (data) {
                     if (data) {
                         data = angular.fromJson(data);
                     }
                     return data;
                 },
                 isArray: true
             },
            'update': { method:'PUT' }
        });
    }
})();
