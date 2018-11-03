angular.module('app').factory('Alert',
	function ($alert, $translate) {

	    return {
			internalError: function () {
                this.error('erro.internalError');
			},

			error: function (message, args) {
				this.show('danger', message, args);
	        },

	        success: function (message) {
				this.show('success', message);
	        },
	
	        info: function (message) {
				this.show('info', message);
	        },
	
	        warn: function (message) {
				this.show('warn', message);
	        },
	
	        handle: function (errorResponse) {
				if (errorResponse && errorResponse.status == 412 && errorResponse.data) {
	                return this.insertAlerts(errorResponse.data);
	            } else if (errorResponse.status == 500) {
					this.error("erro.internalError");
	                return true;
	            }
	        },
	
	        insertAlerts: function (messages) {
				//var types = ['error', 'warn', 'info'], hasAlerts = false;
				for (var index = 0; index < messages.length; index++) {
					var message = messages[index];
					this.error(message.key, message.args);
					hasAlerts = true;
				}
	            return hasAlerts;
	        },

			show: function (type, message, args) {
				this.clear();
	            type = type == "error" ? "danger" : type;
	            type = type == "warn" ? "warning" : type;

				var prepareArgs = {};
				if (args) {
					for (var x = 0; x < args.length; x++) {
						prepareArgs['$' + x] = args[x];
					}
				}
				$translate(message, prepareArgs).then(function (translations) {
					$alert({
						animation: 'am-fade-and-slide-top',
						content: translations,
						type: type,
						show: true,
						container: '.alert-message',
						duration: 6,
						dissmissable: true
					});
				});
	        },
	
	        clear: function () {
				$('.alert-message').empty();
	        }
	    };
	});