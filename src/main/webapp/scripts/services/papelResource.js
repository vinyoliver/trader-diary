angular.module('app').factory('PapelResource',
	function ($resource) {
		var resource = $resource('rest/papel/:papelId', {
				papelId: '@id'
			},
			{
			'unidades': {
				method: 'GET',
				url: 'rest/papel/:papelId/unidades',
				isArray: true
			},
			'search': {
				method: 'POST',
				url: 'rest/papel/search'
			},
			'update': {
				method: 'PUT'
			},
			'active': {
				method: 'PUT',
				url: 'rest/papel/active/:id',
				params: {id: '@id'}
			},
			'inactive': {
				method: 'PUT',
				url: 'rest/papel/inactive/:id',
				params: {id: '@id'}
			}

		});
		return resource;
	});

