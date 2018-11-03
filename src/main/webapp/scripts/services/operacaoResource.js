angular.module('app').factory('OperacaoResource',
	function ($resource) {
		var resource = $resource('rest/operacao/:operacaoId', {
			operacaoId : '@id'
		}, {
			'search': {
				method: 'POST',
				url: 'rest/operacao/search'
			},
			'update': {
				method: 'PUT',
				url: 'rest/operacao/update'
			},
			'active': {
				method: 'POST',
				url: 'rest/operacao/active'
			},
			'remove': {
				method: 'DELETE',
				url: 'rest/operacao/remove/:id',
				params: {id: '@id'}
			},
			'searchAll': {
				method: 'POST',
				url: 'rest/operacao/searchAllObject'
			},
			'findAll': {
				method: 'GET',
				isArray: true,
				url: 'rest/operacao/findAll'
			},
			'print': {
				method: 'POST',
				headers: {
					accept: 'application/pdf'
				},
				responseType: "arraybuffer",
				url: 'rest/operacao/print',
				transformResponse: function (data) {
			        var pdf;
			        if (data) {
			            pdf = new Blob([data], {
			                type: 'application/pdf'
			            });
			        }
			        return {
			            response: pdf
			        };
			    }
			},
			'finalizar': {
                method: 'POST',
				url: 'rest/operacao/finalizar'
			},
            'carregarGainLoss': {
                method: 'GET',
                url: 'rest/operacao/carregarGainLoss/:periodo',
                params: {periodo: '@periodo'}
            },
            'carregarGainLossYear': {
                method: 'GET',
                isArray: true,
                url: 'rest/operacao/carregarGainLossYear/:ano',
                params: {ano: '@ano'}
            },
            'carregarGainLossYear': {
                method: 'GET',
                isArray: true,
                url: 'rest/operacao/carregarGainLossYear/:ano',
                params: {ano: '@ano'}
            },
		});
		return resource;
	});