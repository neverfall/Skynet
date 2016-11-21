(function() {
    'use strict';
    angular
        .module('skynetApp')
        .factory('Participation', Participation);

    Participation.$inject = ['$resource', 'DateUtils', '$log'];

    function Participation ($resource, DateUtils, $log) {
        var resourceUrl =  'api/participations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.date = DateUtils.convertLocalDateFromServer(data.date);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.date = DateUtils.convertLocalDateToServer(data.date);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.date = DateUtils.convertLocalDateToServer(data.date);
                    $log.log(data);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
