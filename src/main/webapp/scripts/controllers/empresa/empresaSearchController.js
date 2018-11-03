angular.module('app').controller('EmpresaSearchController', ['$scope', 'NgTableParams', 'EmpresaResource', '$controller', function ($scope, NgTableParams, EmpresaResource, $controller) {

	$controller('BaseSearchController', {$scope: $scope, resource: EmpresaResource, ngTableParams: NgTableParams});
	$scope.performSearch();
	
}]);