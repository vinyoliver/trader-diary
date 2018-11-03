'use-strict';

angular.module('app').controller('LoginController', ['$timeout', '$rootScope', '$scope', '$http', '$state', '$usuarioLogadoService', '$templateCache', 'Alert', function ($timeout, $rootScope, $scope, $http, $state, $usuarioLogadoService, $templateCache, Alert) {

	$scope.user = {};

	$scope.login = function () {
		$usuarioLogadoService.requestLogin($scope.user)
			.success($scope.loginSuccess);
	}

	$scope.loginSuccess = function (usuarioLogado) {
		$state.go("home");
	}

}]);