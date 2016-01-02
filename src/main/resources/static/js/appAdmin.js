/**
 * Created by Igor on 20.09.2015.
 */
var app = angular.module('MagicMirrorAdmin', []);

app.controller('weatherController', function($scope, $http){

    function init() {
        $http.get('../../api/settings/all').then(function(response) {
            $scope.settings = response.data;
        });
    }

    $scope.submit = function() {
        var list = [];
        list.push({key:'forecastIoApiKey', value:$scope.settings.forecastIoApiKey});
        list.push({key:'forecastIoApiLongitude', value:$scope.settings.forecastIoApiLongitude});
        list.push({key:'forecastIoApiLatitude', value:$scope.settings.forecastIoApiLatitude});
        list.push({key:'forecastIoApiLanguage', value:$scope.settings.forecastIoApiLanguage});
        list.push({key:'forecastIoApiUnit', value:$scope.settings.forecastIoApiUnit});
        $http.post('../settings/set', list);
    };

    init();
});

app.controller('usersController', function ($scope, $http) {

});

app.controller('monitorController', function ($scope, $http) {
    $scope.powerOff = function() {
        $http.get('./off');
    };

    $scope.powerOn = function() {
        $http.get('./on');
    };
});

app.controller('imapController', function ($scope, $http) {
    $scope.untrustedHost = false;

    function init() {
        $http.get('accounts').success(function(data) {
            $scope.data = data;
        }).error(function() {
            $scope.data = [];
        })
    }

    $scope.submit = function() {
        $http.post('set', $scope.item).success(function() {init()}).error(function() {init()});
    };

    $scope.testConnection = function(connection, $event) {
        $($event.currentTarget).removeClass('btn-success btn-danger');
        $http.post('test', connection).success(function() {
            $($event.currentTarget).addClass('btn-success');
        }).error(function(message) {
            $($event.currentTarget).addClass('btn-danger');
            if (message.contains('unable to find valid certification path to requested target'))
                $scope.untrustedHost = true;
        });
    };

    $scope.trustHost = function(connection, $event) {
        $($event.currentTarget).removeClass('btn-success btn-danger');
        $http.post('trustHost', connection).success(function() {
            $($event.currentTarget).addClass('btn-success');
        }).error(function(message) {
            $($event.currentTarget).addClass('btn-danger');
        });
    };

    init();
});

app.controller('icalController', function ($scope, $http) {
    function init() {
        $http.get('accounts').success(function(data) {
            $scope.data = data;
        }).error(function() {
            $scope.data = [];
        })
    }

    $scope.submit = function($event) {
        $http.post('set', $scope.item).success(function() {
            $($event.currentTarget).addClass('btn-success');
            init();
            $scope.item = {};
        }).error(function() {
            $($event.currentTarget).addClass('btn-danger');
            init();
        });
    };

    $scope.testConnection = function(connection, $event) {
        $($event.currentTarget).removeClass('btn-success btn-danger');
        $http.post('test', connection).success(function() {
            $($event.currentTarget).addClass('btn-success');
        }).error(function() {
            $($event.currentTarget).addClass('btn-danger');
        });
    };

    init();
});

app.controller('cronController', function ($scope, $http) {
    $scope.submit = function($event) {
        $http.post('add', $scope.item).success(function() {
            init();
            $($event.currentTarget).addClass('btn-success');
            $($event.currentTarget).removeClass('btn-danger');
            $scope.item = {};
        }).error(function() {
            init();
            $($event.currentTarget).removeClass('btn-success');
            $($event.currentTarget).addClass('btn-danger');
        });
    };

    function init() {
        $http.get('actions').success(function(data) {
            $scope.data = data;
        }).error(function() {
            $scope.data = [];
        })
    }

    init();
});