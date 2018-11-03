angular.module('app').controller('EmpresaCreateController', ['$scope', 'Alert', '$state', 'locationParser', '$modal', 'EmpresaResource', '$http', function ($scope, Alert, $state, locationParser, $modal, EmpresaResource, $http) {

	$scope.disabled = false;
	$scope.empresa = $scope.empresa || {};
	$scope.images = [];
	Alert.clear();

	var unidadeModal = $modal({scope: $scope, templateUrl: 'views/empresa/unidadeModal.html', show: false});

	$scope.adicionarUnidade = function () {
		unidadeModal.show();
	};

	$scope.save = function () {
		EmpresaResource.save($scope.empresa, function (data, responseHeaders) {
			var formDataFiles = new FormData();
			
			for(var i = 0 ; i < $scope.images.length; i++) {
				formDataFiles.append('file', $scope.images[i].file);
			}
			
			$http.post('rest/empresa/'+data.id+'/upload', formDataFiles, {
				transformRequest: angular.identity,
				headers: {'Content-Type': undefined}
			})
			.success(function() {		
				Alert.success('info.registro.salvo');
				$state.go("empresa.edit", {empresaId: data.id});
				
			});
		});
	};
	
	
	$scope.isEditable = function() {
		return true;
	};

}]);