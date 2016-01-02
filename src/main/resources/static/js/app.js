/**
 * Created by Igor on 16.09.2015.
 */

var templateUrl='../angularTemplate/';
//var templateUrl='../../static/angularTemplate/';

$(document).foundation();

var app = angular.module('MagicMirror', []);

app.factory('WebsocketService', ['$q', '$timeout', function($q, $timeout) {
    var service = {},
        listener = $q.defer(),
        socket = {
            client: null,
            stomp: null
        };

    service.RECONNECT_TIMEOUT = 30000;
    service.SOCKET_URL = "/stomp";
    service.CHAT_TOPIC = "/websocket";
    service.CHAT_BROKER = "/app";

    service.receive = function() {
        return listener.promise;
    };

    service.send = function(message, id) {
        socket.stomp.send(service.CHAT_BROKER, {
            priority: 9
        }, JSON.stringify({
            message: message,
            id: id
        }));
    };

    service.subscribe = function(callback) {
        socket.client = new SockJS(service.SOCKET_URL);
        socket.stomp = Stomp.over(socket.client);
        socket.stomp.debug = null;
        socket.stomp.onclose = reconnect;
        socket.stomp.connect({}, function () {
            socket.stomp.subscribe(service.CHAT_TOPIC, function(data) {
                listener.notify(JSON.parse(data.body));
            });
            if (callback) callback();
        });
    };

    var reconnect = function() {
        $timeout(function() {
            initialize();
        }, this.RECONNECT_TIMEOUT);
    };

    return service;
}]);

app.directive('userContent', function($compile, $http, $q) {

    var linker = function(scope, element, attrs) {
        $http.get(templateUrl+scope.userData.templateUrl).success(function(data) {
            element.html(data);
            $compile(element.contents())(scope);
        });
    };

    return {
        restrict: 'AE',
        replace: 'true',
        link:linker
    }
});

app.controller('front', function($scope, $http, $timeout, $interval, WebsocketService) {

    $scope.date = new Date();
    var icons = new Skycons({"color": "white"});
    $scope.settings = {};
    $scope.data = [];

    WebsocketService.receive().then(null, null, function(message) {
        console.log(message);
        updateDisplayedData(message);
        $timeout( function(){ $(window).trigger('resize'); }, 1000);
    });

    $scope.refreshClock = function() {
        $scope.date = new Date();
    };

    $scope.refreshWeather = function() {
        var url = forecastIoUrlBuilder();
        if (url) {
            $http.jsonp(url)
                .then(function(response) {
                    // Success
                    $scope.weatherData = response.data;
                    icons.set("weatherIcon", $scope.weatherData.currently.icon);
                    icons.play();
                }, function(response) {
                    // Failed

                });
        }
    };

    $scope.hourlyForecastIndexes=function(){
        if ($scope.weatherData)
            return [2,5,8,11];
    };

    $scope.setForecastIcon = function(elementId, icon) {
        icons.set(elementId, icon);
        icons.play();
    };

    $scope.dataColumnWidth = function() {
        if ($scope.data.length==1)
            return 'medium-12';
        if ($scope.data.length==2)
            return 'medium-6';
        if ($scope.data.length==3)
            return 'medium-4';
        return '';

    };

    function getSettings() {
        $http.get('../api/settings/all').then(function(response) {
            $scope.settings = response.data;
            if ($scope.settings.forecastIoApiUnit == 'si')
                $scope.settings.temperatureUnit = '°C';
            else
                $scope.settings.temperatureUnit = '°F';
            $scope.refreshWeather();
        });
    }

    function forecastIoUrlBuilder() {
        if (!$scope.settings.forecastIoApiKey || !$scope.settings.forecastIoApiLatitude || !$scope.settings.forecastIoApiLongitude)
            return false;
        var url = 'https://api.forecast.io/forecast/' + $scope.settings.forecastIoApiKey
            + '/' + $scope.settings.forecastIoApiLatitude + ',' + $scope.settings.forecastIoApiLongitude + '?callback=JSON_CALLBACK';
        if ($scope.settings.forecastIoApiLanguage)
            url += '&lang=' + $scope.settings.forecastIoApiLanguage;
        if ($scope.settings.forecastIoApiUnit)
            url += '&units=' + $scope.settings.forecastIoApiUnit;
        return url;
    }

    function updateDisplayedData(newData) {
        var user = newData.user.username;
        // Find the user in current dataset
        var userFound = false;
        for (var i = 0; i < $scope.data.length; i++) {
            if ($scope.data[i].user.username == user) {
                userFound = true;
                // Go through each object for the user
                for (var j = 0; j < newData.content.length; j++) {
                    var objectFound = false;
                    for (var k = 0; k < $scope.data[i].content.length; k++) {
                        if ($scope.data[i].content[k].templateUrl == newData.content[j].templateUrl) {
                            objectFound = true;
                            $scope.data[i].content[k] = newData.content[j];
                        }
                    }
                    if (!objectFound) {
                        $scope.data[i].content.push(newData.content[j]);
                    }
                }
            }
        }
        if (!userFound) {
            $scope.data.push(newData);
        }
    }

    $scope.refreshClock();
    getSettings();
    WebsocketService.subscribe(null);

    $interval( function(){ $scope.refreshClock(); }, 1000);
    $interval( function(){ $scope.refreshWeather(); }, 1000 * 60 * 30);

    $scope.parseJavaDate = function(data) {
        return new Date(data.year, data.monthValue, data.dayOfMonth, data.hour, data.minute, data.second, 0);
    };
});