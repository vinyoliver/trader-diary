'use-strict';

angular.module('app').controller('HomeController', ['$scope', '$usuarioLogadoService', '$controller', function ($scope, $usuarioLogadoService, $controller) {

    $scope.usuarioLogado = $usuarioLogadoService.getUsuarioLogado();

    $scope.$watch(function () {
            return $usuarioLogadoService.getUsuarioLogado();
        },
        function (newVal, oldVal) {
            if (newVal) {
                $scope.usuarioLogado = newVal;
            }
        }, true);


    $scope.controller = function () {
        $controller('DashBoardController', {$scope: $scope});
    };

    $scope.controller();

}]);