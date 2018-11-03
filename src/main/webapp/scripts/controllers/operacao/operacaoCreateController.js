angular.module('app').controller('OperacaoCreateController', ['$scope', 'Alert', '$state', '$http', '$controller', '$modal', '$stateParams', 'OperacaoResource', 'PapelResource', function ($scope, Alert, $state, $http, $controller, $modal, $stateParams, OperacaoResource, PapelResource) {

	$controller('OperacaoBaseController', {$scope: $scope});

	$scope.model = $scope.model || {finalidade: 'COMPRA'};
	//$scope.tab = 2;

	$scope.save = function () {
		OperacaoResource.save($scope.model, function (data, responseHeaders) {
			$scope.model = new OperacaoResource(data);

			if($scope.imagensEntrada.length > 0) {
				var formDataImageInicio = new FormData();
				for(var i = 0 ; i < $scope.imagensEntrada.length; i++) {
					formDataImageInicio.append('file', $scope.imagensEntrada[i].file);
				}

				$http.post('rest/operacao/'+data.id+'/ENTRADA/upload', formDataImageInicio, {
					transformRequest: angular.identity,
					headers: {'Content-Type': undefined}
				}).success(function() {
                    Alert.success('info.registro.salvo');
					$state.go("operacao.edit", {operacaoId: $scope.model.id}, {reload: true});
				});
			}else{
                Alert.success('info.registro.salvo');
                $state.go("operacao.edit", {operacaoId: $scope.model.id}, {reload: true});
			}
		});
	};

}]);