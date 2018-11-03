angular.module('app').controller('OperacaoSearchController', ['$scope', 'NgTableParams', 'OperacaoResource', '$controller', '$modal', 'Alert', '$rootScope', '$http', function ($scope, NgTableParams, OperacaoResource, $controller, $modal, Alert, $rootScope, $http) {

	$controller('BaseSearchController', {$scope: $scope, resource: OperacaoResource, ngTableParams: NgTableParams});


    $scope.getPercentual = function (operacao) {
        var value = 0.0;
        if (operacao.finalidade == 'COMPRA') {
            value = (operacao.saida - operacao.entrada) / operacao.entrada * 100;
        } else {
            value = (operacao.entrada - operacao.saida) / operacao.saida * 100;
        }
        if (!value || isNaN(value)) {
            return '0,00%';
        }
        return numeral(value).format('0,0.00') + '%';
    };

	$scope.performSearch();

}]);