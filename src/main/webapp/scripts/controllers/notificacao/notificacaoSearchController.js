angular.module('app').controller('NotificacaoSearchController', ['$scope', 'NgTableParams', 'NotificacaoResource', '$controller', '$usuarioLogadoService', '$notificacaoService', '$modal', function ($scope, NgTableParams, NotificacaoResource, $controller, $usuarioLogadoService, $notificacaoService, $modal) {

	$scope.filtro = {lida: 'false', page: 1, count: 10};
	var msgModal = $modal({scope: $scope, templateUrl: 'views/notificacao/viewMsgModal.html', show: false});
	$scope.tab = 1;
	
	$scope.carregarNotificacoesRecebidas = function () {
		$scope.tableNotificacoes = new NgTableParams({}, {
			counts: [],
			getData: function (params) {
				$scope.filtro.page = params.url().page;
				$scope.filtro.count = params.url().count;
				$scope.filtro.ativo = "true";

				$scope.filtro.usuario = $usuarioLogadoService.getUsuarioLogado();

				return NotificacaoResource.searchRecebidas($scope.filtro).$promise.then(function (data) {
					params.total(data.total);
					$scope.totalNotificacoes = data.total;
					return data.resultData;
				});
			}
		});
	};
	
	$scope.viewerOptions = { 
			selector:'textarea',
			force_br_newlines : true,
	        force_p_newlines : false,
	        height : 300,
	        forced_root_block : 'div',
			menubar:false,
			toolbar: false,
			statusbar: false,
			extended_valid_elements: 'a[href|target=_parent]',
			content_css : '/styles/tinymce.css'
	};
	
	$scope.carregarNotificacoesEnviadas = function () {
		$scope.tableNotificacoes = new NgTableParams({}, {
			counts: [],
			getData: function (params) {
				$scope.filtro.page = params.url().page;
				$scope.filtro.count = params.url().count;
				$scope.filtro.ativo = "true";

				$scope.filtro.usuario = $usuarioLogadoService.getUsuarioLogado();

				return NotificacaoResource.searchEnviadas($scope.filtro).$promise.then(function (data) {
					params.total(data.total);
					$scope.totalNotificacoes = data.total;
					return data.resultData;
				});
			}
		});
	};

	$scope.marcarComoLida = function (notificacao) {
		NotificacaoResource.markAsRead(notificacao).$promise.then(function () {
			$notificacaoService.refreshNumeroDeNotificacoes();
			notificacao.notificacaoLida = true;
			notificacao.dataLeitura = moment(new Date());
		});
	}
	
	$scope.limparFiltros = function() {
		$scope.filtro = {};
		if($scope.tab == 1) {
			$scope.filtro.lida = 'false';
		}
		$scope.search();
	}
	
	$scope.showMsg = function(notificacaoUsuario) {
		$scope.tituloMensagem = notificacaoUsuario.notificacao.titulo ? notificacaoUsuario.notificacao.titulo : notificacaoUsuario.notificacao.descricaoTipo;
		$scope.dataHoraMensagem = notificacaoUsuario.notificacao.dataEnvio;
		$scope.mensagem = notificacaoUsuario.notificacao.mensagem;
		if(!notificacaoUsuario.dataLeitura && !notificacaoUsuario.notificacaoLida) {
			$scope.marcarComoLida(notificacaoUsuario);
		}
		msgModal.show();
	}
	
	$scope.setTab = function (numero) {
		$scope.tab = numero;
		if(numero == 1) {
			$scope.carregarNotificacoesRecebidas();
		} else {
			$scope.carregarNotificacoesEnviadas();
		}		
	};

    $scope.editorOptions = {
			selector:'textarea',
			menubar: false,
			language_url: 'scripts/vendor/pt_BR.js',
			plugins: 'textcolor noneditable',
		};
	
	$scope.search = function() {
		if($scope.tab == 1) {
			$scope.carregarNotificacoesRecebidas();
		} else {
			$scope.carregarNotificacoesEnviadas();
		}		
	};
	$scope.carregarNotificacoesRecebidas();

	$scope.$on("$locationChangeStart", function (event) {
		msgModal.hide();
	});
}]);