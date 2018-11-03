angular.module('app').controller('UsuarioBaseController', ['$scope', '$state', '$usuarioLogadoService', 'EmpresaResource', 'PerfilResource', '$window', function ($scope, $state, $usuarioLogadoService, EmpresaResource, PerfilResource, $window) {
	
	$scope.getUnidades = function () {
		EmpresaResource.unidades({empresaId: $usuarioLogadoService.getUsuarioLogado().idEmpresa}, function (data) {
			$scope.unidades = data;	
		});
	};

	$scope.getPerfis = function () { 
		PerfilResource.search(function (data) {
			$scope.perfis = data;
		});
	};

	$scope.hasUnidade = function (unidadeId) {
		if($scope.usuario) {
			for (var i = 0; i < $scope.usuario.unidades.length; i++) {
				if ($scope.usuario.unidades[i].id == unidadeId)
					return 'state-icon glyphicon glyphicon-check';
			}
		}
		return 'state-icon glyphicon glyphicon-unchecked';
	};

	$scope.checkUnidade = function (event, unidadeId, action) {
		if($scope.isEditable() && (action == "click" || (action == "keyup" && event.keyCode == 32))) {
			// verifica se o usuario tem a unidade e a remove
			for (var i = 0; i < $scope.usuario.unidades.length; i++) {
				if ($scope.usuario.unidades[i].id == unidadeId) {
					$('#' + event.target.id).attr('ng-class', 'state-icon glyphicon glyphicon-unchecked');
					$scope.usuario.unidades.splice(i, 1);
					return;
				}
			}
			// adiciona a unidade ao usuario
			for (var i = 0; i < $scope.unidades.length; i++) {
				if ($scope.unidades[i].id == unidadeId) {
					$scope.usuario.unidades.push($scope.unidades[i]);
					$('#' + event.target.id).attr('ng-class', 'state-icon glyphicon glyphicon-check');
					return;
				}
			}
		}
	};

	$scope.hasPerfil = function (perfilId) {
		for (var i = 0; i < $scope.usuario.perfis.length; i++) {
			if ($scope.usuario.perfis[i].id == perfilId)
				return 'state-icon glyphicon glyphicon-check';
		}
		return 'state-icon glyphicon glyphicon-unchecked';
	};

	$scope.checkPerfil = function (event, perfilId) {
		if($scope.isEditable()) {
			// verifica se o usuario tem o perfil e o remove
			for (var i = 0; i < $scope.usuario.perfis.length; i++) {
				if ($scope.usuario.perfis[i].id == perfilId) {
					$('#' + event.target.id).attr('ng-class', 'state-icon glyphicon glyphicon-unchecked');
					$scope.usuario.perfis.splice(i, 1);
					return;
				}
			}
			// adiciona o perfil ao usuario
			for (var i = 0; i < $scope.perfis.length; i++) {
				if ($scope.perfis[i].id == perfilId) {
					$scope.usuario.perfis.push($scope.perfis[i]);
					$('#' + event.target.id).attr('ng-class', 'state-icon glyphicon glyphicon-check');
					return;
				}
			}
		}
	};	
	
	$scope.campoInvalido = function() {
		return $scope.usuario.senha != $scope.senhaConfirmada;
	}
	
	$scope.desabilitaSalvar = function() {
		if ($scope.usuario.unidades.length == 0) return true;
		if (!$scope.usuario.perfil) return true;
		
		if ($state.is("usuario.create") && $scope.usuario.senha != $scope.usuario.senhaConfirmada) return true;
			
		return false;
	}
	
	$window.scrollTo(0, 0);
	
}]);