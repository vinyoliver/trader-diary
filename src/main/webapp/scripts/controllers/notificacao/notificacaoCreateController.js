angular.module('app').controller('NotificacaoCreateController', ['$scope', 'Alert', '$state', 'NotificacaoResource', '$usuarioLogadoService', 'UsuarioResource', 'EmpresaResource', 'UnidadeResource', 'PerfilResource', '$modal', function ($scope, Alert, $state, NotificacaoResource, $usuarioLogadoService, UsuarioResource, EmpresaResource, UnidadeResource, PerfilResource, $modal) {

	var confirmModal = $modal({scope: $scope, templateUrl: 'views/generics/confirmModal.html', show: false});
	
	$scope.notificacao = {
			status: 'EM_PROGRESSO', 
			usuarioEnvio: $usuarioLogadoService.getUsuarioLogado(),
			dataEnvio: moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
			notificacaoUsuarios: []
	};
	$scope.usuarios = [];
	$scope.usuarioFilter = {unidade: null};
	$scope.notificacaoUsuarios = [];
	
	$scope.getEmpresas = function () {
		$scope.usuarioFilter.unidadeSelecionada = null;
		$scope.usuarioFilter.perfil = null;

        if ($usuarioLogadoService.isAuthorized(['ROLE_SYSTEM_ADMIN'])) {
			EmpresaResource.search({ativo:true}).$promise.then(function(response) {
				$scope.empresas = response.resultData;
				$scope.buscarUsuarios();
			});
		}
		else{
			$scope.unidades = $usuarioLogadoService.getUsuarioLogado().unidades;
			$scope.unidades.forEach(function (item) {
				if(!item.ativo) {
					$scope.unidades.splice($scope.unidades.indexOf(item), 1);
				}
			});
			$scope.buscarUsuarios();
		}
	};
	

	$scope.buscarUnidades = function(){
		$scope.usuarioFilter.unidadeSelecionada = null;
		$scope.usuarioFilter.perfil = null;
		if($scope.usuarioFilter.empresa && $scope.usuarioFilter.empresa.id){
			EmpresaResource.unidades({empresaId: $scope.usuarioFilter.empresa.id}, function (data) {
				$scope.unidades = data;
				$scope.buscarUsuarios();
			});
		} else {
			$scope.getEmpresas();
			$scope.unidades = [];
		}
	}
	
	PerfilResource.search({ativo: true},function (data) {
		$scope.perfis = data;
	});
	
	$scope.editorOptions = { 
		selector:'textarea',
		force_br_newlines : true,
        force_p_newlines : false,
        statusbar: false,
        forced_root_block : 'div',
		menubar: false,
		language_url: 'scripts/vendor/pt_BR.js',
		plugins: 'textcolor noneditable',
		content_css : '/styles/tinymce.css',
		toolbar: 'undo redo | styleselect | bold italic | forecolor backcolor | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | fontselect |  fontsizeselect',
	};

	$scope.isEditable = function () {
		return true;
	};
	
	$scope.enviar = function() {
		$scope.notificacao.notificacaoUsuarios = [];
		$scope.notificacaoUsuarios.forEach(function (usuarioSelecionado) {
			$scope.notificacao.notificacaoUsuarios.push({"usuario": usuarioSelecionado});
		});
		
		NotificacaoResource.save($scope.notificacao, function (data, responseHeaders) {
			Alert.success('info.modal.operacao');
			$state.go('notificacao');
		});
		
	}
	
	$scope.buscarUsuarios = function(){
		usuariosSelecionados = $scope.notificacaoUsuarios;
		UsuarioResource.buscarUsuariosParaNotificacao($scope.usuarioFilter).$promise.then(function (usuarios) {
			usuarios.forEach(function(item, index, object) {
				if(usuariosSelecionados.some(function(e){ return e.id == item.id})){
					object.splice(index, 1);
				}
			});
			$scope.usuarios = $.merge(usuarios, usuariosSelecionados);
			var usuariosDualList = $('.usuariosDualList').bootstrapDualListbox('refresh');
		});
	}
	
	$scope.confirmarEnvio = function () {
		if ($scope.notificacao.mensagem.length > 2000) {
			Alert.error('erro.mensagem.longa');
			return;
		}
		
		$scope.msgModal = 'Ao enviar a notificação, os usuários selecionados serão notificados. Esta operação não pode ser desfeita. Confirma o envio da notificação?';
		confirmModal.show();
	};
	
	$scope.operationConfirmed = function () {
		$scope.enviar();
	};
	
	$scope.getEmpresas();
}]);