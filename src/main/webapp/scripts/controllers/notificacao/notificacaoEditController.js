angular.module('app').controller('NotificacaoEditController', ['$scope', '$stateParams', '$state', 'Alert', 'NotificacaoResource', 'NgTableParams', function ($scope, $stateParams, $state, Alert, NotificacaoResource, NgTableParams) {

	$scope.notificacao = $scope.notificacao || {};
	$scope.filtro = {page: 1, count: 10};

	$scope.get = function () {
		NotificacaoResource.get({notificacaoId: $stateParams.notificacaoId}, function (data) {
			$scope.notificacao = new NotificacaoResource(data);
			$scope.carregarUsuarios();
		});
	};
	
	$scope.editorOptions = { 
			selector:'textarea',
			menubar: false,
			force_br_newlines : true,
	        force_p_newlines : false,
	        forced_root_block : '<br>',
			statusbar: false,
			language_url: 'scripts/vendor/pt_BR.js',
			plugins: 'textcolor noneditable',
			toolbar: 'undo redo | styleselect | bold italic | forecolor backcolor | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent',
			init_instance_callback : function(editor) {
				if ($state.is('notificacao.edit')) {
					editor.getBody().setAttribute('contenteditable', true);
				} else {
					editor.getBody().setAttribute('contenteditable', false);
				}
			}
		};

	$scope.isEditable = function () {
		return $state.is("notificacao.edit");
	};
	
	$scope.carregarUsuarios = function () {
		$scope.tableNotificacoes = new NgTableParams({}, {
			counts: [],
			getData: function (params) {
				$scope.filtro.page = params.url().page;
				$scope.filtro.count = params.url().count;
				$scope.filtro.ativo = "true";
				$scope.filtro.notificacaoId = $scope.notificacao.id;

				return NotificacaoResource.usuarios($scope.filtro).$promise.then(function (data) {
					params.total(data.total);
					$scope.totalNotificacoes = data.total;
					if($scope.notificacao.descricaoStatus != "Finalizada" && ($scope.filtro.lida == "" || $scope.filtro.lida == undefined)) {
						$scope.finalizada = true;
						for(var i = 0 ; i < data.resultData.length; i++) {
							if(data.resultData[i].dataLeitura == null) {
								$scope.finalizada = false;
								break;
							}
						}
						if($scope.finalizada) {
							NotificacaoResource.finalizar($scope.notificacao, function () {
								$scope.notificacao.descricaoStatus = "Finalizada";
							});
						}
					}
					
					return data.resultData;
				});
			}
		});
	};

	$scope.viewerOptions = {
		selector: 'textarea',
		force_br_newlines: true,
		force_p_newlines: false,
		height: 300,
		forced_root_block: 'div',
		menubar: false,
		toolbar: false,
		statusbar: false,
		extended_valid_elements: 'a[href|target=_parent]',
		content_css: '/styles/tinymce.css'
	};

	$scope.get();
}]);
