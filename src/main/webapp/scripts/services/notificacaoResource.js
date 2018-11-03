angular.module('app').factory('NotificacaoResource',
	function ($resource) {
		var resource = $resource('rest/notificacao/:notificacaoId', {
			notificacaoId: '@id'
		}, {
			'searchRecebidas': {
				method: 'POST',
				url: 'rest/notificacao/searchRecebidas'
			},
			'searchEnviadas': {
				method: 'POST',
				url: 'rest/notificacao/searchEnviadas'
			},
			'markAsRead': {
				method: 'PUT',
				url: 'rest/notificacao/markAsRead/:id',
				params: {id: '@id'}
			},
			'usuarios': {
				method: 'POST',
				url: 'rest/notificacao/usuarios'
			},
			'finalizar': {
				method: 'PUT',
				url: 'rest/notificacao/finalizar'
			}
		});
		return resource;
	});