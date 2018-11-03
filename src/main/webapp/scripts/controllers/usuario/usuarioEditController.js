angular.module('app').controller('UsuarioEditController', ['$scope', '$stateParams', '$state', 'Alert', 'UsuarioResource', '$controller', function ($scope, $stateParams, $state, Alert, UsuarioResource, $controller) {
	
	$controller('UsuarioBaseController', {$scope: $scope});
	
	$scope.get = function () {
		UsuarioResource.get({usuarioId: $stateParams.usuarioId}, function (data) {
			$scope.usuario = new UsuarioResource(data);
			$scope.getUnidades();
			$scope.getPerfis();
		});
	};

	$scope.save = function () {
		$scope.usuario.$update(function () {
			Alert.success('info.registro.alterado');
			$state.go('usuario');
		});
	};
	
	$scope.isEditable = function() {
		return $state.is("usuario.edit");
	};

	$scope.get();
}]);
