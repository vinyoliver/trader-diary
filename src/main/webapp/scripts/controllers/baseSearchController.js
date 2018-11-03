angular.module('app').controller('BaseSearchController', ['$scope', 'resource', 'ngTableParams', '$modal', 'Alert', '$window', '$location', 'FiltersService', function ($scope, resource, ngTableParams, $modal, Alert, $window, $location, FiltersService) {

	var confirmModal = $modal({scope: $scope, templateUrl: 'views/generics/confirmModal.html', show: false});
	
	$scope.filters = jQuery.extend(true, {}, FiltersService.getLocationSearch(), FiltersService.getFilters()); // concatencação dos parâmetros via url com os paramentros de array(filtersService.js). Motivo: Url não está armazenando arrays.
	$scope.searchResults = [];
	$scope.pageRange = [];
	$scope.totalResults = 0;
	$scope.tableParams = {};
	$scope.resetSearch = false;
	
	$scope.performSearch = function () {
		$scope.tableParams = new ngTableParams(
		 angular.extend({
		        page: 1,
		        count: 10
		 }, $location.search()), {
	        getData: function (params) {
	        	if ($scope.resetSearch) {
	        		$scope.filters.page = 1;
	        	} else {
	        		$scope.filters.page = params.url().page;
	        	}
        		
				$scope.filters.count = params.url().count;
	        	FiltersService.setFilters($scope.filters);
	        	$location.search($scope.filters);
	        	jQuery(function ($) {
	        		$scope.filters = $.extend(true, {}, FiltersService.getLocationSearch(), FiltersService.getFilters()); 
	        	});
	        	
				return resource.search($scope.filters).$promise.then(function (data) {
	            	params.total(data.total);
					$scope.totalResults = data.total;
					$scope.resetSearch = false;
					return data.resultData;
	            });
	        }
	    });
	};
	
	$scope.translateMultiSelect = {
		    selectAll       : "Marcar tudo",
		    selectNone      : "Desmarcar tudo",
		    reset           : "Limpar",
		    search          : "Digite aqui para pesquisar...",
		    nothingSelected : "---"
	};

	$scope.clearFilters = function () {
		$scope.filters = {};
		$scope.filters.page = 1;
		$scope.filters.count = 10;
		$scope.performSearch();
	};
	
	$scope.ativar = function (entidade) {
		$scope.entidade = entidade;
		$scope.operation = 'ativar';
		$scope.msgModal = 'Deseja realmente ativar o registro?';
		confirmModal.show();
	};
	
	$scope.desativar = function (entidade) {
		$scope.entidade = entidade;
		$scope.operation = 'desativar';
		$scope.msgModal = 'Deseja realmente desativar o registro?';
		confirmModal.show();
	};


	$scope.remover = function (entidade) {
		$scope.entidade = entidade;
		$scope.operation = 'remover';
		$scope.msgModal = 'Deseja realmente remover o registro?';
		confirmModal.show();
	};
	
	$scope.operationConfirmed = function () {
		if($scope.operation == 'ativar') {
			resource.active({id: $scope.entidade.id}, function () {
				Alert.success('info.modal.operacao');
				$scope.performSearch();
			});
		} else if($scope.operation == 'desativar') {
			resource.inactive({id: $scope.entidade.id}, function () {
				Alert.success('info.modal.operacao');
				$scope.performSearch();
			});
		} else if ($scope.operation == 'remover') {
			resource.remove({id: $scope.entidade.id}, function () {
				Alert.success('info.modal.operacao');
				$scope.performSearch();
			});
		}

	};
	
	$scope.pesquisar = function() {
		$scope.resetSearch = true;
		$scope.performSearch();
	}
	
	$window.scrollTo(0, 0);
	
}]);
