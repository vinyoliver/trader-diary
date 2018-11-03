angular.module('app').controller('OperacaoEditController', ['$scope', '$stateParams', '$state', 'Alert', '$http', 'OperacaoResource', '$controller', function ($scope, $stateParams, $state, Alert, $http, OperacaoResource, $controller) {

	$controller('OperacaoBaseController', {$scope: $scope});
	
	$scope.model = $scope.model || {};
	$scope.fornecedores = [];
	$scope.programacoes = [];
	$scope.tab = 1;
	
	$scope.get = function () {
		OperacaoResource.get({operacaoId: $stateParams.operacaoId}, function (data) {
			$scope.model = new OperacaoResource(data);
			$scope.model.stop = $scope.model.stopLoss[0].valor;
			$scope.model.target = $scope.model.stopGain[0].valor;

			$scope.imagensEntrada = $scope.model.imagensEntrada || [];
			$scope.imagensSaida = $scope.model.imagensSaida || [];

			prepareImages($scope.imagensEntrada);
			prepareImages($scope.imagensSaida);

		});
	};


	function prepareImages(images) {
		if(images && images.length > 0) {
			for(i in images) {
				images[i].active = 'item';
			}
			images[0].active = 'item active';
		}
	}

	$scope.save = function () {
        checkImagesBeforeSave();
		$scope.model.$update(function (data) {
			uploadImages();
		});
	};


	$scope.finalizarOperacao = function () {
        checkImagesBeforeSave();
		OperacaoResource.finalizar($scope.model, function (data) {
			$scope.model = new OperacaoResource(data);
			uploadImages();
		});
	};

    function checkImagesBeforeSave() {
        //tratamento de imagens removidas... So envia para o servidor aquilo permanece. O cadastro de novas imagens eh feito em outro momento.
        $scope.model.imagens = $scope.imagensEntrada.concat($scope.imagensSaida);
        var indicesToRemove = [];
        for (i in $scope.model.imagens) {
            if (!$scope.model.imagens[i].id) {
                indicesToRemove.push(i);
            }
        }
        for (i in indicesToRemove) {
            $scope.model.imagens.splice(indicesToRemove[i], 1);
        }
    }


	function uploadImages() {
		if ($scope.imagensEntrada.length > 0) {
			uploadImagesEntrada();
		}
		else if ($scope.imagensSaida.length > 0) {
			uploadImagesSaida();
		} else {
            encerrarAlteracao();
		}
	}

	function uploadImagesEntrada() {
		var formDataImageInicio = new FormData();
		var hasNewImage = false;
		for (var i = 0; i < $scope.imagensEntrada.length; i++) {
			if (!$scope.imagensEntrada[i].id) {
				formDataImageInicio.append('file', $scope.imagensEntrada[i].file);
                hasNewImage = true;
			}
		}
		if (hasNewImage = true) {
			$http.post('rest/operacao/' + $scope.model.id + '/ENTRADA/upload', formDataImageInicio, {
				transformRequest: angular.identity,
				headers: {'Content-Type': undefined}
			}).success(function () {
				uploadImagesSaida();
			});
		} else {
			uploadImagesSaida();
		}

	}

	function uploadImagesSaida() {
		var formDataImageSaida = new FormData();
        var hasNewImage = false;
		for (var i = 0; i < $scope.imagensSaida.length; i++) {
			if (!$scope.imagensSaida[i].id) {
                hasNewImage = true;
				formDataImageSaida.append('file', $scope.imagensSaida[i].file);
			}
		}
		if (hasNewImage) {
			$http.post('rest/operacao/' + $scope.model.id + '/SAIDA/upload', formDataImageSaida, {
				transformRequest: angular.identity,
				headers: {'Content-Type': undefined}
			}).success(function () {
                encerrarAlteracao();
			});
		} else {
            encerrarAlteracao();
		}
	}

	function encerrarAlteracao(){
        $scope.hideModal();
        $state.reload();
        Alert.success('info.registro.alterado');
	}

	$scope.isEditable = function() {
        return $state.is("operacao.edit");
	};
	
	$scope.removeAnexo = function(file) {
		$scope.files.splice($scope.files.indexOf(file), 1);
		$scope.model.anexos.splice($scope.model.anexos.indexOf(file), 1);
	};
	
	$scope.getPercentual = function() {
		var value = 0.0;
		if($scope.model.finalidade == 'COMPRA'){
			value = ($scope.model.precoSaida - $scope.model.precoEntrada) / $scope.model.precoEntrada * 100;
		}else{
			value = ($scope.model.precoEntrada - $scope.model.precoSaida) / $scope.model.precoSaida * 100;
		}
		if (!value || isNaN(value)) {
			return '0,00%';
		}
		return numeral(value).format('0,0.00') + '%';
	};


	$scope.getValorFinalOperacao = function() {
		if($scope.model.finalidade == 'COMPRA'){
			return ($scope.model.precoSaida - $scope.model.precoEntrada) * $scope.model.quantidade;
		}else{
			return ($scope.model.precoEntrada - $scope.model.precoSaida) * $scope.model.quantidade;
		}
	};

    $scope.getDifferenceDate = function() {
        return parseInt(moment.duration(moment($scope.model.dataTermino, "YYYY-MM-DD").diff(moment($scope.model.dataInicio, "YYYY-MM-DD"))).asDays());
    }

	$scope.nextOperation = function() {
        $http.get('rest/operacao/next/{0}'.format($stateParams.operacaoId)).success(function (nextId) {
            $state.go('operacao.edit', {'operacaoId': nextId});
        });
	}

    $scope.previousOperation = function() {
        $http.get('rest/operacao/previous/{0}'.format($stateParams.operacaoId)).success(function (previouId) {
            $state.go('operacao.edit', {'operacaoId': previouId});
        });
    }

    $scope.$on('ngRepeatFinished', function (ngRepeatFinishedEvent) {
        $(".lightgallery").lightGallery({
            selector: '.light-link',
            height: '100%',
            width: '100%',
            zoom: 'true',
            scale: 0.25
        });
    });


    $scope.get();
}]);