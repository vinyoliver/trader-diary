'use-strict';

angular.module('app').config(['$stateProvider', '$urlRouterProvider', function ($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.otherwise('/');

    $stateProvider

        .state('home', {
            url: '/',
            templateUrl: 'views/logged-content.html',
            controller: 'HomeController',
            resolve: {
                home: function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'home',
                        files: ['scripts/controllers/dashboardTecnico/dashBoardController.js',
                            'scripts/services/usuarioResource.js',
                            'scripts/services/empresaResource.js',
                            'scripts/services/operacaoResource.js',
                            'scripts/services/unidadeResource.js'
                        ]
                    });
                }
            }
        })

        .state('404', {
            url: 'not-found',
            templateUrl: 'views/404.html',
            requireLogin: true,
            parent: 'home'
        })

        .state('contato', {
            url: 'contato',
            templateUrl: 'views/contato.html',
            parent: 'home'
        })

        .state('usuario', {
            url: 'usuario',
            templateUrl: 'views/usuario/search.html',
            controller: 'UsuarioSearchController',
            abstract: false,
            parent: 'home',
            resolve: {
                usuario: function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'usuario',
                        files: ['scripts/controllers/usuario/usuarioSearchController.js',
                            'scripts/services/perfilResource.js']
                    });
                }
            }
        })

        .state('usuario.create', {
            url: 'usuario/create',
            templateUrl: 'views/usuario/detail.html',
            controller: 'UsuarioCreateController',
            requireLogin: true,
            parent: 'home',
            resolve: {
                usuarioCreate: function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'usuarioCreate',
                        files: ['scripts/controllers/usuario/usuarioCreateController.js',
                            'scripts/controllers/usuario/usuarioBaseController.js',
                            'scripts/services/perfilResource.js']
                    });
                }
            }
        })

        .state('usuario.edit', {
            url: 'usuario/edit/:usuarioId',
            templateUrl: 'views/usuario/detail.html',
            controller: 'UsuarioEditController',
            requireLogin: true,
            parent: 'home',
            resolve: {
                usuarioEdit: function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'usuarioEdit',
                        files: ['scripts/controllers/usuario/usuarioEditController.js',
                            'scripts/controllers/usuario/usuarioBaseController.js',
                            'scripts/services/perfilResource.js']
                    });
                }
            }
        })

        .state('usuario.view', {
            url: 'usuario/view/:usuarioId',
            templateUrl: 'views/usuario/detail.html',
            controller: 'UsuarioEditController',
            requireLogin: true,
            parent: 'home',
            resolve: {
                usuarioView: function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'usuarioView',
                        files: ['scripts/controllers/usuario/usuarioEditController.js',
                            'scripts/controllers/usuario/usuarioBaseController.js',
                            'scripts/services/perfilResource.js']
                    });
                }
            }
        })

        .state('empresa', {
            url: 'empresa',
            templateUrl: 'views/empresa/search.html',
            controller: 'EmpresaSearchController',
            abstract: false,
            parent: 'home',
            resolve: {
                empresa: function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'empresa',
                        files: ['scripts/controllers/empresa/empresaSearchController.js']
                    });
                }
            }
        })

        .state('empresa.create', {
            url: 'empresa/create',
            templateUrl: 'views/empresa/detail.html',
            controller: 'EmpresaCreateController',
            controllerUrl: 'EmpresaCreateController',
            abstract: false,
            parent: 'home',
            resolve: {
                empresaCreate: function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'empresaCreate',
                        files: ['scripts/controllers/empresa/empresaCreateController.js']
                    });
                }
            }
        })

        .state('empresa.edit', {
            url: 'empresa/edit/:empresaId',
            templateUrl: 'views/empresa/detail.html',
            controller: 'EmpresaEditController',
            controllerUrl: 'EmpresaEditController',
            abstract: false,
            parent: 'home',
            resolve: {
                empresaEdit: function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'empresaEdit',
                        files: ['scripts/controllers/empresa/empresaEditController.js']
                    });
                }
            }
        })

        .state('empresa.view', {
            url: 'empresa/view/:empresaId',
            templateUrl: 'views/empresa/detail.html',
            controller: 'EmpresaEditController',
            controllerUrl: 'EmpresaEditController',
            abstract: false,
            parent: 'home',
            resolve: {
                empresaView: function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'empresaView',
                        files: ['scripts/controllers/empresa/empresaEditController.js']
                    });
                }
            }
        })


        //		Notificações
        .state('notificacao', {
            url: 'notificacao',
            templateUrl: 'views/notificacao/search.html',
            controller: 'NotificacaoSearchController',
            requireLogin: true,
            parent: 'home',
            resolve: {
                notificacao: function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'notificacao',
                        files: ['scripts/controllers/notificacao/notificacaoSearchController.js',
                            'scripts/services/notificacaoResource.js']
                    });
                }
            }
        })

        .state('notificacao.create', {
            url: 'notificacao/create',
            templateUrl: 'views/notificacao/detail.html',
            controller: 'NotificacaoCreateController',
            requireLogin: true,
            parent: 'home',
            resolve: {
                notificacao: function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'notificacao',
                        files: ['scripts/controllers/notificacao/notificacaoCreateController.js',
                            'scripts/services/notificacaoResource.js',
                            'scripts/services/perfilResource.js']
                    });
                }
            }
        })

        .state('notificacao.view', {
            url: 'notificacao/view/:notificacaoId',
            templateUrl: 'views/notificacao/detail.html',
            controller: 'NotificacaoEditController',
            requireLogin: true,
            parent: 'home',
            resolve: {
                notificacao: function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'notificacao',
                        files: ['scripts/controllers/notificacao/notificacaoEditController.js',
                            'scripts/services/notificacaoResource.js']
                    });
                }
            }
        })


        .state('parametros', {
            url: 'parametros-sistema',
            templateUrl: 'views/parametros/detail.html',
            controller: 'ParametrosSistemaEditController',
            abstract: false,
            parent: 'home',
            resolve: {
                parametrosSistema: function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'parametrosSistema',
                        files: ['scripts/controllers/parametros/parametrosSistemaEditController.js',
                            'scripts/services/parametrosSistemaResource.js']
                    });
                }
            }
        })

        //		Feriado
        .state('feriado', {
            url: 'feriado',
            templateUrl: 'views/feriado/search.html',
            controller: 'FeriadoSearchController',
            abstract: false,
            parent: 'home',
            resolve: {
                cargo: function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'feriado',
                        files: ['scripts/controllers/feriado/feriadoSearchController.js',
                            'scripts/services/feriadoResource.js']
                    });
                }
            }
        })

        .state('feriado.create', {
            url: 'feriado/create',
            templateUrl: 'views/feriado/detail.html',
            controller: 'FeriadoCreateController',
            requireLogin: true,
            parent: 'home',
            resolve: {
                feriadoCreate: function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'feriadoCreate',
                        files: ['scripts/controllers/feriado/feriadoCreateController.js',
                            'scripts/controllers/feriado/feriadoBaseController.js',
                            'scripts/services/feriadoResource.js']
                    });
                }
            }
        })

        .state('feriado.edit', {
            url: 'feriado/edit/:id',
            templateUrl: 'views/feriado/detail.html',
            controller: 'FeriadoEditController',
            requireLogin: true,
            parent: 'home',
            resolve: {
                feriadoEdit: function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'feriadoEdit',
                        files: ['scripts/controllers/feriado/feriadoEditController.js',
                            'scripts/controllers/feriado/feriadoBaseController.js',
                            'scripts/services/feriadoResource.js']
                    });
                }
            }
        })

        .state('feriado.view', {
            url: 'feriado/view/:id',
            templateUrl: 'views/feriado/detail.html',
            controller: 'FeriadoEditController',
            requireLogin: true,
            parent: 'home',
            resolve: {
                feriadoView: function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'feriadoView',
                        files: ['scripts/controllers/feriado/feriadoEditController.js',
                            'scripts/controllers/feriado/feriadoBaseController.js',
                            'scripts/services/feriadoResource.js']
                    });
                }
            }
        })


        /*
        NEW FEATURES
         */
        .state('operacao', {
            url: 'operacao',
            templateUrl: 'views/operacao/search.html',
            controller: 'OperacaoSearchController',
            abstract: false,
            parent: 'home',
            resolve: {
                operacao: function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'operacao',
                        files: ['scripts/controllers/operacao/operacaoSearchController.js', 'scripts/services/operacaoResource.js']
                    });
                }
            }
        })

        .state('operacao.create', {
            url: 'operacao/create',
            templateUrl: 'views/operacao/detail.html',
            controller: 'OperacaoCreateController',
            controllerUrl: 'OperacaoCreateController',
            abstract: false,
            parent: 'home',
            resolve: {
                operacaoCreate: function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'operacaoCreate',
                        files: ['scripts/controllers/operacao/operacaoCreateController.js',
                            'scripts/controllers/operacao/operacaoBaseController.js',
                            'scripts/services/operacaoResource.js',
                            'scripts/services/papelResource.js']
                    });
                }
            }
        })

        .state('operacao.edit', {
            url: 'operacao/edit/:operacaoId',
            templateUrl: 'views/operacao/detail.html',
            controller: 'OperacaoEditController',
            controllerUrl: 'OperacaoEditController',
            abstract: false,
            parent: 'home',
            resolve: {
                operacaoEdit: function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'operacaoEdit',
                        files: ['scripts/controllers/operacao/operacaoEditController.js',
                            'scripts/controllers/operacao/operacaoBaseController.js',
                            'scripts/services/operacaoResource.js',
                            'scripts/services/papelResource.js']
                    });
                }
            }
        })

        .state('operacao.view', {
            url: 'operacao/view/:operacaoId',
            templateUrl: 'views/operacao/detail.html',
            controller: 'OperacaoEditController',
            controllerUrl: 'OperacaoEditController',
            abstract: false,
            parent: 'home',
            resolve: {
                operacaoView: function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'operacaoView',
                        files: ['scripts/controllers/operacao/operacaoEditController.js']
                    });
                }
            }
        })
}])


    .run(['$rootScope', '$location', '$usuarioLogadoService', '$http', '$notificacaoService', function ($rootScope, $location, $usuarioLogadoService, $http, $notificacaoService) {
        $rootScope.$on('$viewContentLoading', function (event, viewConfig) {
            if (!$usuarioLogadoService.isUsuarioLogado()) {
                $http.get('rest/usuario/current-user').success(function (data) {
                    $usuarioLogadoService.setUsuarioLogado(data);
                    $notificacaoService.refreshNumeroDeNotificacoes();
                }).error(function () {
                    window.location.replace("/login.jsp");
                });
            }
        });
    }])

	