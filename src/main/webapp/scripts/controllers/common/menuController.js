'use-strict';

angular.module('app').controller('MenuController', ['$scope', '$state', '$http', '$sce', '$usuarioLogadoService', '$modal', 'Alert', 'UsuarioResource', 'EmpresaResource', 'UnidadeResource', '$notificacaoService', '$controller', function ($scope, $state, $http, $sce, $usuarioLogadoService, $modal, Alert, UsuarioResource, EmpresaResource, UnidadeResource, $notificacaoService, $controller) {

	var alteraSenhaModal = $modal({scope: $scope, templateUrl: 'views/generics/alterarSenhaModal.html', show: false});
	var tutorialVideoModal = $modal({scope: $scope, templateUrl: 'views/generics/tutorialVideoModal.html', show: false});
	var relatarErroModal = $modal({scope: $scope, templateUrl: 'views/generics/relatarErroModal.html', show: false});
	
    $controller('DashBoardController', {$scope: $scope});

	$scope.dados = {};
	$scope.empresas = [];
	$scope.unidades = [];
	
	$scope.logout = function () {
		$usuarioLogadoService.logout();
	};

	$scope.isUsuarioLogado = function () {
		return $usuarioLogadoService.isUsuarioLogado();
	};
	
	$scope.alterarSenha = function () {
		$scope.usuario = {};
		$scope.usuario.admin = false;
		$scope.usuario.id = $usuarioLogadoService.getUsuarioLogado().id;
		alteraSenhaModal.show();
	};
	
	$scope.salvarNovaSenha = function () {
		UsuarioResource.alteraSenha({id: $scope.usuario.id, 
									 senhaAtual: $scope.usuario.senhaAtual,
									 senhaNova: $scope.usuario.senhaNova }, function () {
			alteraSenhaModal.hide();
			Alert.success('info.alteracao.senha');
		});
	};

	$scope.verificaUnidade = function (trocarUnidade) {
		// se trocarUnidade é true então o usuário está tentando trocar a unidade pelo menu
		// se trocarUnidade é false então o usuário está escolhendo a unidade depois de logar
		// recupera unidade selecionada da sessão
		UsuarioResource.unidadeSelecionada(function(data) {
			// Guarda os principais dados da unidade
			$scope.dados.idUnidade = data.id;
			$scope.dados.nomeUnidade = data.nome;
			
			if (data.empresa) {
				$scope.dados.nomeEmpresa = data.empresa.nome;
			}

			if(!$scope.dados.idUnidade || trocarUnidade) {
				$http.get('rest/usuario/current-user').success(function (usuarioLogado) {
					$usuarioLogadoService.setUsuarioLogado(usuarioLogado);
						$scope.unidades = $usuarioLogadoService.getUsuarioLogado().unidades;

						$scope.unidades.forEach(function (item) {
							if(!item.ativo) {
								$scope.unidades.splice($scope.unidades.indexOf(item), 1);
							}
						});

					/* Quando o usuário possui uma única empresa/carteira */
						if($scope.isAuthenticated() && $usuarioLogadoService.getUsuarioLogado().unidades.length == 1) {
							$scope.dados.nomeUnidade = $scope.unidades[0].nome;
							$scope.dados.idUnidade = $scope.unidades[0].id;
							$scope.selecionarUnidadeAtual($scope.unidades[0]);
							$state.go("home");
						} else {
							// Usuário possui mais de uma unidade
							$scope.escolheUnidadeModal = $modal({
								scope: $scope,
								templateUrl: 'views/generics/escolheUnidadeModal.html',
								show: true,
								backdrop: 'static',
								keyboard: false						
							});
						}
                    // }
				});
			} else {
				$scope.unidadeSelecionada = true;
			}
		});
	};
	
	$scope.selecionarUnidade = function () {
		if($scope.dados.idUnidade) {
			// busca unidade no banco
			UnidadeResource.get({unidadeId: $scope.dados.idUnidade}, function (data) {
				// atualiza unidade selecionada na sessão
				$scope.selecionarUnidadeAtual(data);
				// guarda nome da unidade p/ mostrar no menubar
				$scope.dados.nomeUnidade = data.nome;
				$scope.dados.nomeEmpresa = data.empresa.nome;
				
				$scope.escolheUnidadeModal.hide();
				$state.go("home");
			});
		}
	};
	
	$scope.changeEmpresa = function (item) {
		if(item) {
			$scope.dados.idUnidade = null;
			UnidadeResource.search({empresaId: item.id, ativo: true, count:null}, function (data) {
				$scope.unidades = data.resultData;
			});
		}
	};
	
	$scope.isHome = function () {
		if($state.is("home"))
			return true;
		return false;
	};
	
	$scope.getNomeUsuarioLogado = function () {
		if($usuarioLogadoService.getUsuarioLogado() == null)
			return '';
		return $usuarioLogadoService.getUsuarioLogado().nome;
	};
	
	$scope.$watch(function () {
		if($notificacaoService.getNumeroDeNotificacoes() > 0) {
			if(!$('.badge').is(':animated')) {
				$('.badge').blink({backgroundColor:'#d2352c'}, {backgroundColor:'#ffd041'}, 1000);
			}
		} else {
			$('.badge').stop();
			$('.badge').css("background-color","#d2352c");
		}
		return $notificacaoService.getNumeroDeNotificacoes();
	},
	function (newVal, oldVal) {
		$scope.numberNotifications = newVal;		
	}, true);
	
	$scope.isAuthenticated = function() {
		return $usuarioLogadoService.isAuthenticated();
	};

	$scope.$watch('[dados.idEmpresa, dados.idUnidade]', function(newVal) {
		if(newVal[0]){
			$usuarioLogadoService.getUsuarioLogado().idEmpresa = newVal[0];
		}
		if(newVal[1]){
			$usuarioLogadoService.getUsuarioLogado().idUnidade = newVal[1];
		}
	});
	
	$scope.selecionarUnidadeAtual = function (unidade) {
		$scope.unidadeSelecionada = true;
		UsuarioResource.trocaUnidade(unidade, function () {
			$notificacaoService.refreshNumeroDeNotificacoes();
			$scope.numberNotifications = $notificacaoService.getNumeroDeNotificacoes();
		});
	};
	
	$scope.podeTrocarUnidade = function() {
        return $scope.isAuthenticated() && $usuarioLogadoService.getUsuarioLogado().unidades && ($usuarioLogadoService.getUsuarioLogado().unidades.length > 1 || $usuarioLogadoService.isAuthorized(['ROLE_SYSTEM_ADMIN']));
	}
	
	$scope.verificaUnidade(false);

	
	$scope.$on('$viewContentLoaded', function() {
		

		jQuery(function ($) {

			$('#wrapper').on('mouseenter', function (e) {
			    $('[data-toggle=popover]').each(function () {
			        if (!$(this).is(e.target) && $(this).has(e.target).length === 0 && $('.popover').has(e.target).length === 0) {
			            $(this).popover("destroy");
			        }
			    });
			});
			
			$('[data-toggle="tooltip"]').tooltip();
				
		});
	 });

	 $scope.openLeftMenu = function() {
		    $mdSidenav('left').toggle();
	  };
		  
	$scope.openModalTutorialVideo = function (url_video) {
		$scope.url_video = $sce.trustAsResourceUrl(url_video);
		tutorialVideoModal.show();
	};

	  
	$scope.reportarErroModalForm = function () {
		relatarErroModal.show();
	};	  

}]);