'use-strict';

angular.module('app').controller('LoggedController', ['$scope', '$usuarioLogadoService', function ($scope, $usuarioLogadoService) {

	$scope.usuarioLogado = $usuarioLogadoService.getUsuarioLogado();

	$scope.$watch(function () {
			return $usuarioLogadoService.getUsuarioLogado();
		},
		function (newVal, oldVal) {
			if (newVal) {
				$scope.usuarioLogado = newVal;
			}
		}, true);
}]);