angular.module('app').factory('UnidadeResource',
	function ($resource) {
		var resource = $resource('rest/unidade/:unidadeId', {
			unidadeId: '@id'
		}, {
			'update': {
				method: 'PUT',
				url: 'rest/unidade/update'
			},
			'active': {
				method: 'PUT',
				url: 'rest/unidade/active'
			},
			'inactive': {
				method: 'PUT',
				url: 'rest/unidade/inactive'
			},
			'search': {
				method: 'POST',
				url: 'rest/unidade/search'
			},
		});
		return resource;
	});

