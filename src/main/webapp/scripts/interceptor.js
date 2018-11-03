angular.module('app').factory('ajaxHttpInterceptor', ['$location', '$q', '$injector', '$rootScope', function ($location, $q, $injector, $rootScope) {
	var handlers = {};
	 var loadingCount = 0;
	handlers["401"] = function (response) {
		var loginURL = '/login';
		var search = $location.search();
		if (!search.target && $location.path() != '/login') {
			window.location.replace("/login.jsp");
		}
		window.location.replace("/login.jsp");
	}
	handlers["412"] = function (response) {
		var Alert = $injector.get('Alert');
		if (response.data) {
			Alert.handle(response);
		}
	}
	handlers["500"] = function (response) {
		var Alert = $injector.get('Alert');
		Alert.handle(response);
	}
	handlers["0"] = function (response) {
		//$.messages.clear().addError('error.servidorForaDoAr');
	}
	handlers["404"] = function (response) {
		$location.path("/not-found");
	}
	handlers["403"] = function (response) {
		$location.path("/403");
	}
	handlers["405"] = function (response) {
		window.location.replace("/login.jsp");
	}
	handlers.default = function (response) {
		//Alert.handle(response.data);
	}

	return {
		request: function (config) {
		    if(++loadingCount === 1)
		    	$rootScope.$broadcast('loading:progress');
		    return config || $q.when(config);
		},
		
		response: function (response) {
			if(typeof response.data === 'string' && response.data.indexOf('spring_security_check') != -1) {
				$rootScope.authenticated = false;
				window.location.replace("/login.jsp");
			}
		    if(--loadingCount === 0)
		    	$rootScope.$broadcast('loading:finish');
		    return response || $q.when(response);
		},
	        
		responseError: function (response) {
			if(--loadingCount === 0) 
				$rootScope.$broadcast('loading:finish');
			var handler = handlers[response.status];
			if (handler) {
				handler(response);
			} else {
				handlers.default(response);
			}
			return $q.reject(response);
		}
	};
}]);

angular.module('app').config(['$httpProvider', function ($httpProvider) {
	$httpProvider.interceptors.push('ajaxHttpInterceptor');
}]);