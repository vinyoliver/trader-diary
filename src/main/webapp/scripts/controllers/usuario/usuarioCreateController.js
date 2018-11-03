angular.module('app').controller('UsuarioCreateController', ['$scope', 'Alert', '$state', '$usuarioLogadoService', 'UsuarioResource', '$controller', function ($scope, Alert, $state, $usuarioLogadoService, UsuarioResource, $controller) {

	$controller('UsuarioBaseController', {$scope: $scope});
	
	$scope.usuario = $scope.usuario || {};
	$scope.usuario.unidades = $scope.usuario.unidades || [];
	$scope.usuario.perfis = $scope.usuario.perfis || [];

	$scope.save = function () {
		$scope.usuario.empresa = {id: $usuarioLogadoService.getUsuarioLogado().idEmpresa};
		
		UsuarioResource.save($scope.usuario, function (data, responseHeaders) {
			Alert.success('info.registro.salvo');
			$state.go('usuario');
		});
	};
	
	$scope.isEditable = function() {
		return true;
	};
	
	$scope.getUnidades();
	$scope.getPerfis();
}]);