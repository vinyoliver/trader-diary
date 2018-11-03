angular.module('app').controller('EmpresaEditController', ['$scope', '$stateParams', '$state', 'Alert', '$modal', 'EmpresaResource', 'UnidadeResource', 'Resources', '$http', function ($scope, $stateParams, $state, Alert, $modal, EmpresaResource, UnidadeResource, Resources, $http) {

	var self = this;
	$scope.estados = Resources.getEstadosUF();
	$scope.cidades = [];
	$scope.editUnidade = false;
	$scope.empresa = $scope.empresa || {};
	$scope.unidade = $scope.unidade || {};
	$scope.contatoUnidade = {};
	$scope.images = [];
	Alert.clear();

	var confirmModal = $modal({scope: $scope, templateUrl: 'views/generics/confirmModal.html', show: false});
	var unidadeModal = $modal({scope: $scope, templateUrl: 'views/empresa/unidadeModal.html', show: false});

	$scope.get = function () {
		EmpresaResource.get({empresaId: $stateParams.empresaId}, function (data) {
			self.original = data;
			$scope.empresa = new EmpresaResource(self.original);
			EmpresaResource.unidades({empresaId: $stateParams.empresaId}, function (data) {
				$scope.empresa.unidades = data;
			})
			
			if($scope.empresa.imagem){
				$http.get('rest/empresa/'+data.id+'/imagem/'+ $scope.empresa.imagem.id)
				.success(function(data) {
					$scope.images.push({id: data.id, src: 'data:image/png;base64,'+data.base64, active: 'item'});
					if($scope.images.length > 0) {
						$scope.images[0].active = 'item active';
					}
				});
			}
		});
	};

	$scope.adicionarUnidade = function () {
		$scope.editUnidade = true;
		$scope.unidade = {};
		unidadeModal.show();
	};

	$scope.editarUnidade = function (unidade) {
		$scope.editUnidade = true;
		$scope.unidade = unidade;
		$scope.loadEstados();
		unidadeModal.show();
	};

	$scope.visualizarUnidade = function (unidade) {
		$scope.editUnidade = false;
		$scope.unidade = unidade;
		$scope.loadEstados();
		unidadeModal.show();
	};

	$scope.save = function () {
		$scope.empresa.$update(function (data) {
            Alert.success('info.registro.salvo');
            $state.go('empresa');
		});
	};

	$scope.saveUnidade = function () {
		$scope.unidade.empresa = {}
		$scope.unidade.empresa.id = $scope.empresa.id;
		if ($scope.unidade.id) {
			UnidadeResource.update($scope.unidade, function () {
				$scope.empresa.unidades.push($scope.unidade);
				Alert.success('info.registro.alterado');
				unidadeModal.hide();
				$state.go($state.current, {}, {reload: true});
			});
		} else {
			UnidadeResource.save($scope.unidade, function (data, responseHeaders) {
				$scope.unidade.ativo=true;
				$scope.empresa.unidades.push($scope.unidade);
				Alert.success('info.registro.alterado');
				unidadeModal.hide();
				$state.go($state.current, {}, {reload: true});
			});
		}
	};

	$scope.changeEstado = function () {
		$scope.unidade.cidade = {};
		$scope.loadEstados();
	};

	$scope.loadEstados = function () {
		if ($scope.unidade.estado) {
			$http.get('rest/resources/cidades/' + $scope.unidade.estado.uf).success(function (data) {
				$scope.cidades = data;
			});
		}
	};

	$scope.isEditable = function () {
		return $state.is("empresa.edit");
	};

	$scope.ativarOuDesativarUnidade = function (unidade, operation) {
		$scope.unidade = unidade;
		$scope.operation = operation;
		if (operation == 'desativar') {
			$scope.msgModal = 'Deseja realmente desativar o registro?';
		} else {
			$scope.msgModal = 'Deseja realmente ativar o registro?';
		}
		confirmModal.show();
	};

	$scope.operationConfirmed = function () {
		if ($scope.operation == 'ativar') {
			UnidadeResource.active($scope.unidade, function () {
				$state.reload();
				Alert.success('info.modal.operacao');
			});
		} else if ($scope.operation == 'desativar') {
			UnidadeResource.inactive($scope.unidade, function () {
				$state.reload();
				Alert.success('info.modal.operacao');
			});
		}
	};


	$scope.adicionarContato = function () {
		$scope.unidade.contatos = $scope.unidade.contatos || [];
		if ($scope.indiceContatoUnidade != null) {
			$scope.unidade.contatos[$scope.indiceContatoUnidade] = $scope.contatoUnidade;
			$scope.indiceContatoUnidade = null;
		} else {
			$scope.unidade.contatos.push($scope.contatoUnidade);
		}
		$scope.contatoUnidade = {};
	};

	$scope.editarContato = function (index) {
		$scope.indiceContatoUnidade = index;
		$scope.contatoUnidade = angular.copy($scope.unidade.contatos[index]);
	};

	$scope.habilitarAdicionarContatoUnidade = function () {
		return $scope.contatoUnidade.nome && $scope.contatoUnidade.telefone && $scope.contatoUnidade.cargo;
	};

	$scope.removerContato = function (contatoUnidade) {
		$scope.unidade.contatos.splice($scope.unidade.contatos.indexOf(contatoUnidade), 1);
	};

	$http.get('rest/resources/segmentos-industriais').success(function(data) {

        $scope.segmentos = data;
    });

    $scope.get();
}]);
