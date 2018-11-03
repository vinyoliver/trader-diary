angular.module('app').controller('UsuarioSearchController', ['$scope', 'NgTableParams', 'Alert', 'UsuarioResource', 'PerfilResource', '$modal', '$controller', '$usuarioLogadoService', function ($scope, NgTableParams, Alert, UsuarioResource, PerfilResource, $modal, $controller, $usuarioLogadoService) {

	$controller('BaseSearchController', {$scope: $scope, resource: UsuarioResource, ngTableParams: NgTableParams});

	var alteraSenhaModal = $modal({scope: $scope, templateUrl: 'views/generics/alterarSenhaModal.html', show: false});
	
	$scope.getIdUsuarioLogado = function(){
		return $usuarioLogadoService.getUsuarioLogado().id;
	}

	$scope.alterarSenha = function (id) {
		$scope.usuario = {};
		$scope.usuario.admin = true;
		$scope.usuario.id = id;
		alteraSenhaModal.show();
	};
	
	$scope.salvarNovaSenha = function () {
		UsuarioResource.alteraSenha({id: $scope.usuario.id, 
									 senhaAtual: $scope.usuario.senhaAtual,
									 senhaNova: $scope.usuario.senhaNova }, function () {
			Alert.success('info.alteracao.senha');
		});
	};

	$scope.getPerfis = function () {
		PerfilResource.search(function (data) {
			$scope.perfis = data;
		});
	};

	$scope.getPerfis();
	$scope.performSearch();

}]);