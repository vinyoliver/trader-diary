angular.module('app').factory('EmpresaResource',
	function ($resource) {
		var resource = $resource('rest/empresa/:empresaId', {
				empresaId: '@id'
			},
			{
			'unidades': {
				method: 'GET',
				url: 'rest/empresa/:empresaId/unidades',
				isArray: true
			},
			'search': {
				method: 'POST',
				url: 'rest/empresa/search'
			},
			'update': {
				method: 'PUT'
			},
			'active': {
				method: 'PUT',
				url: 'rest/empresa/active/:id',
				params: {id: '@id'}
			},
			'inactive': {
				method: 'PUT',
				url: 'rest/empresa/inactive/:id',
				params: {id: '@id'}
			}

		});
		return resource;
	});

