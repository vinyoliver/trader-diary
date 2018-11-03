angular.module('app').factory('UsuarioResource',
	function ($resource) {
		var resource = $resource('rest/usuario/:usuarioId', {
			usuarioId: '@id'
		}, {
			'search': {
				method: 'POST',
				url: 'rest/usuario/search'
			},
			'update': {
				method: 'PUT',
				url: 'rest/usuario/update'
			},
			'active': {
				method: 'PUT',
				url: 'rest/usuario/active/:id',
				params: {id: '@id'}
			},
			'inactive': {
				method: 'PUT',
				url: 'rest/usuario/inactive/:id',
				params: {id: '@id'}
			},
			'alteraSenha': {
				method: 'PUT',
				url: 'rest/usuario/altera-senha'
			},
			'trocaUnidade': {
				method: 'PUT',
				url: 'rest/usuario/troca-unidade'
			},
			'unidadeSelecionada': {
				method: 'GET',
				url: 'rest/usuario/unidade-selecionada'
			},
			'buscarUsuariosParaNotificacao': {
				method: 'POST',
				isArray: true,
				url:'rest/usuario/buscar-usuarios-para-notificacao'
			}
		});
		return resource;
	});

