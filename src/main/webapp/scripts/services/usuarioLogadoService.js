angular.module('app').factory('$usuarioLogadoService', ['$http', '$location', 'Alert', '$cookies', '$rootScope', function ($http, $location, Alert, $cookies, $rootScope) {

	var usuarioLogadoService = {};
	usuarioLogadoService.usuarioLogado = null;

	usuarioLogadoService.isUsuarioLogado = function () {
		return this.usuarioLogado != null;
	}

	usuarioLogadoService.requestLogin = function (user) {
		return $http({
			method: 'POST',
			url: 'j_spring_security_check',
			data: $.param(user),
			headers: {'Content-Type': 'application/x-www-form-urlencoded'}
		}).success(function (usuarioLogado) {
			usuarioLogadoService.setUsuarioLogado(usuarioLogado);
		}).error(function () {
			Alert.error('erro.autenticacao');
		});
	}

	usuarioLogadoService.logout = function () {
		window.location.replace("/login.jsp");
		usuarioLogadoService.setUsuarioLogado(null);
		$http.post('/logout').success(function () {
		}).error(function (c) {
			console.info("error");
		});
	}

	usuarioLogadoService.addListener = function (callback) {
		if (usuarioLogadoService.usuarioLogado) {
			callback(usuarioLogadoService.usuarioLogado);
		} else {
			usuarioLogadoService.callbacks.push(callback);
		}
	};

	usuarioLogadoService.setUsuarioLogado = function (usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
		$rootScope.authenticated = true;
	}

	usuarioLogadoService.getUsuarioLogado = function () {
		return this.usuarioLogado;
	}

	usuarioLogadoService.isAuthorized = function (roles) {
		if (this.isUsuarioLogado()) {
			var userRoles = this.usuarioLogado.autoridades;
			for (var index = 0; index < roles.length; index++) {
				if (userRoles.indexOf(roles[index]) != -1) {
					return true;
				}
			}
		}
		return false;
	}
	
	usuarioLogadoService.isAuthenticated = function() {
		return $rootScope.authenticated;
	}

	return usuarioLogadoService;
}]);
