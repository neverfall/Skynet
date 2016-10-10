(function() {
    'use strict';
    angular
        .module('skynetApp')
        .factory('Participacion', Participacion);

    Participacion.$inject = ['$resource', 'DateUtils'];

    function Participacion ($resource, DateUtils) {
        var resourceUrl =  'api/participacions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.fecha = DateUtils.convertLocalDateFromServer(data.fecha);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.fecha = DateUtils.convertLocalDateToServer(data.fecha);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.fecha = DateUtils.convertLocalDateToServer(data.fecha);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
