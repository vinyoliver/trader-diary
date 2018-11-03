angular.module('app').factory('PerfilResource',
	function ($resource) {
		var resource = $resource('rest/perfil/:PerfilId', {
			PerfilId: '@id'
		}, {
			'search': {
				method: 'GET',
				url: 'rest/perfil/search',
				isArray: true
			},
			'update': {
				method: 'PUT'
			}

		});
		return resource;
	});

