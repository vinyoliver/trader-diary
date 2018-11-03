angular.module('app').controller('DashBoardController', ['$scope', '$location', 'NgTableParams', 'OperacaoResource', '$controller', function ($scope, $location, NgTableParams, OperacaoResource, $controller) {

    $controller('BaseChartController', {$scope: $scope});

    $scope.periodos = [{label: "Ultímos 30 Dias", value: "MES"}, {
        label: "Ultímos 6 Meses",
        value: "SEMESTRE"
    }, {label: "Ultímo Ano", value: "ANO"}];

    $scope.anoResumo = (new Date()).getFullYear();
    $scope.periodo = $scope.periodos[0];


    $scope.filters = {"encerrada": false};
    $scope.searchResults = [];
    $scope.pageRange = [];
    $scope.totalResults = 0;
    $scope.tableParams = {};
    $scope.resetSearch = false;

    $scope.performSearch = function () {
        $scope.tableParams = new NgTableParams(
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
                    return OperacaoResource.search($scope.filters).$promise.then(function (data) {
                        params.total(data.total);
                        $scope.totalResults = data.total;
                        $scope.resetSearch = false;
                        return data.resultData;
                    });
                }
            });
    };

    $scope.montarGraficosOperacoesRing = function () {
        OperacaoResource.carregarGainLoss({periodo: $scope.periodo}).$promise.then(function (data) {
            $scope.gain30Dias = data.gain30Dias ? data.gain30Dias : 0;
            $scope.loss30Dias = data.loss30Dias ? data.loss30Dias : 0;
            $scope.valuesForRingChart = [[$scope.gain30Dias], [$scope.loss30Dias]];
            $scope.chartRingGainLoss = {
                "type": "ring",
                "legend": {
                    "border-width": "0",
                    "background-color": "transparent",
                    "layout": "4x1", //row x column
                    "x": "-7px",

                    "padding": "0",
                    "item": {
                        "font-size": "10px",
                        "padding": "0",
                        "width": "40px",
                    },
                    "height": "50px"
                },
                "background-color": "transparent",
                "title": {
                    "background-color": "none",
                    "color": "#333"
                },
                "plot": {
                    "slice": 25,
                    "shadow": 0,
                    "border-width": "1px",
                    "border-color": "#F2F2F0",
                    "value-box": {
                        "font-size": "15px",
                        "font-color": "rgba(250,250,250,0.7)"
                    },
                    "animation": {
                        "effect": "2",
                        "delay": "1000",
                        "speed": "500",
                        "method": "5",
                        "sequence": "1"
                    },

                },
                "tooltip": {
                    "fontSize": 15,
                    "align": "left",
                    "borderRadius": 3,
                    "borderWidth": 2,
                    "borderColor": "%color-1",
                    "backgroundColor": "#fff",
                    "alpha": 0.9,
                    "padding": 10,
                    "color": "#000",
                    "negation": "currency",
                    "thousandsSeparator": ".",
                    "shadow": 0,
                    "text": "$ <b style=\"color:%color\">%node-value</b>"
                },
                "series": [
                    {
                        "text": "GAIN",
                        "background-color": "#5eb480"
                    },
                    {
                        "text": "LOSS",
                        "background-color": "#de4c40"
                    },
                ]
            };
        });

    }


    $scope.montarGraficoOperacoesAnual = function () {
        OperacaoResource.carregarGainLossYear({ano: $scope.anoResumo}).$promise.then(function (data) {
            var gainArray = [];
            var lossArray = [];
            var index = data.length;
            for (i = 1; i <= 12; i++) {
                j = 0;
                var gain = null;
                var loss = null;
                while (j < data.length && (gain == null && loss == null)) {
                    if (data[j].mes == i) {
                        gain = data[j].gain30Dias;
                        loss = data[j].loss30Dias;
                    }
                    j++
                }
                gainArray.push(gain);
                lossArray.push(loss);
            }

            $scope.valueForResumeYear = [gainArray, lossArray];

            $scope.chartResumeYear = {
                "type": "bar",
                "plotarea": {
                    "adjust-layout": true
                },
                "scale-x": {
                    "label": {
                        "text": "Resumo de Operações",
                    },
                    "labels": ["Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"] /* Scale Labels */
                },
                "tooltip": {
                    "fontSize": 15,
                    "text": "$ <b>%node-value</b>"
                },
                "series": [
                    {
                        "text": "GAIN",
                        "background-color": "#5eb480"
                    },
                    {
                        "text": "LOSS",
                        "background-color": "#de4c40"
                    },
                ]
            };
        });
    };


    $scope.update = function (periodo) {
        $scope.periodo = periodo;
        $scope.montarGraficosOperacoesRing();
    };

    $scope.updateAnoResumo = function () {
        $scope.anoResumo = $("#ano_resumo").val();
        $scope.montarGraficoOperacoesAnual();
    };


    $scope.$on('$viewContentLoaded', function () {
        $scope.performSearch();
        $scope.montarGraficosOperacoesRing();
        $scope.montarGraficoOperacoesAnual();
    });

}]);