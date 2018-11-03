angular.module('app').controller('OperacaoBaseController', ['$scope', '$modal', '$http', 'OperacaoResource', 'PapelResource', 'Resources', 'Alert', '$state', '$window', '$confirm', '$location',
          function ($scope, $modal, $http, OperacaoResource, PapelResource, Resources, Alert, $state, $window, $confirm, $location) {

	$scope.imagensEntrada = [];
	$scope.imagensSaida = [];
              $scope.setups = Resources.getSetups();


	var finalizarModal = $modal({scope: $scope, templateUrl: 'views/operacao/finalizarOperacao.html', show: false, backdrop: 'static', keyboard: false});

	$scope.findPapeis = function(nome) {
		return PapelResource.search({nomeAtivo: nome, ativo: true, count: null}).$promise.then(function(response) {
			return response.resultData;
		});
	};

	$scope.formatarNomeAtivo = function (papel) {
		if(!papel){
			return "";
		}
		if (!papel.sigla || papel.sigla == null || papel.sigla == '---') {
		  return papel.nome;
		}
		return papel.sigla + ' - ' + papel.nome;
	}

	$scope.openModalFinalizar = function() {
		$scope.urlImagem = "img/image-default.png";
		finalizarModal.show();
	};

	$scope.hideModal = function() {
        finalizarModal.hide();
    }

	$window.scrollTo(0, 0);
	
}]);