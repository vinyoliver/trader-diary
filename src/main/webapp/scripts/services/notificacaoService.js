angular.module('app').factory('$notificacaoService', ['$http', '$location', 'Alert', '$cookies', '$rootScope', '$usuarioLogadoService', function ($http, $location, Alert, $cookies, $rootScope, $usuarioLogadoService) {

	var notificacaoService = {};
	notificacaoService.numeroDeNotificacoes = 0;

	notificacaoService.refreshNumeroDeNotificacoes = function () {
		var usuarioLogado = $usuarioLogadoService.getUsuarioLogado();
		
		if(usuarioLogado.id) {
			$http.get('rest/notificacao/usuario/' + usuarioLogado.id + '/number-notifications').then(function (response) {
				notificacaoService.numeroDeNotificacoes = response.data;
			});
		}
	}

	notificacaoService.getNumeroDeNotificacoes = function () {
		return notificacaoService.numeroDeNotificacoes;
	}

	return notificacaoService;
}]);
