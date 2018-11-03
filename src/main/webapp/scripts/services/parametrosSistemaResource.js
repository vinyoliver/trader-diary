angular.module('app').factory('ParametrosSistemaResource', 
	function ($resource) {
		var resource = $resource('rest/parametros-sistema', {
		},
		{
			'update': {
				method: 'PUT',
				url: 'rest/parametros-sistema/update'
			}
		});
		return resource;
	});