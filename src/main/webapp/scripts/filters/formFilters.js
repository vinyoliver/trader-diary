var language = {
	delimiters: {
		thousands: '.',
		decimal: ','
	},
	abbreviations: {
		thousand: 'mil',
		million: 'milhões',
		billion: 'b',
		trillion: 't'
	},
	ordinal: function (number) {
		return 'º';
	},
	currency: {
		symbol: 'R$'
	}
};

numeral.language('pt-br', language);
numeral.language('pt-br');


angular.module('app').filter('data', function () {
	return function (date) {
		if (!date) return "";
		return moment(date).format("DD/MM/YYYY");
	}
});

angular.module('app').filter('durationToHours', function () {
	return function (interval) {
		if (!interval) return "---";
		return moment.duration(parseFloat(interval), "hours").format("HH:mm", {trim: false});

	}
});

angular.module('app').filter('objectEmpty', function () {
	return function (object) {
		if (!object) return "---";
		return object;

	}
});

angular.module('app').filter('durationToMinutes', function () {
	return function (interval) {
		if (!interval) return "---";
		return moment.duration(interval, "minutes").format("HH:mm");
	}
});


angular.module('app').filter('horaShort', function () {
	return function (date) {
		if (!date) return "";
		return moment(date).format("HH:mm");
	}
});

angular.module('app').filter('hora', function () {
	return function (date) {
		if (!date) return "";
		return moment(date).format("HH:mm:ss");
	}
})


angular.module('app').filter('dataHoraShort', function () {
	return function (date) {
		if (!date) return "---";
		return moment(date).format("DD/MM/YY HH:mm");
	}
});

angular.module('app').filter('dataHora', function () {
	return function (date) {
		if (!date) return "";
		return moment(date).format("DD/MM/YYYY HH:mm:ss");
	}
});

angular.module('app').filter('realbrasileiro', function () {
	return function (input) {
		if (!input || isNaN(input)) {
			return 'R$ 0,00';
		}
		return 'R$ ' + numeral(input).format('0,0.00');
	}
});

angular.module('app').filter('decimal', function () {
	return function (input) {
		if (!input || isNaN(input)) {
			return '0,00';
		}
		return numeral(input).format('0,0.00');
	}
});

angular.module('app').filter('percentual', function () {
	return function (input) {
		if (!input || isNaN(input)) {
			return '0,00 %';
		}
		return numeral(input).format('0,0.00') + ' %';
	}
});


angular.module('app').filter('boolYesNo', function () {
	return function (input) {
		if (input) return "Sim";
		return "Não";
	};
});

angular.module('app').filter('break12', function () {
	return function (input) {
		if (input && input.length > 12) {
			return input.substr(0, 12) + "...";
		}
		return input;
	};
});

angular.module('app').filter('telefone', ['MaskService', function (MaskService) {
	return function (input) {
		if (!input) return "";
		var maskService = MaskService.create();
		return getValueMasked(maskService, '(99) 9?9999-9999', input);
	};
}]);

angular.module('app').filter('cpf', ['MaskService', function (MaskService) {
	return function (input) {
		if (!input) return "";
		var maskService = MaskService.create();
		return getValueMasked(maskService, '999.999.999-99', input);
	};
}]);


angular.module('app').filter('cep', ['MaskService', function (MaskService) {
	return function (input) {
		if (!input) return "";
		var maskService = MaskService.create();
		return getValueMasked(maskService, '9999-999', input);
	};
}]);

angular.module('app').filter('mask', ['MaskService', function (MaskService) {
	return function(text, mask) {
		var maskService = MaskService.create();
		return getValueMasked(maskService, mask, text);
	};
}]);


angular.module('app').filter('decimalParaHora', function () {
	return function (decimal) {
		var sinal = decimal < 0 ? "-" : "";
		var minuto = Math.floor(Math.abs(decimal));
		var segundo = Math.floor((Math.abs(decimal)*60) % 60);
		
		return sinal + (minuto < 10 ? "0" : "") + minuto + ":" + (segundo < 10 ? "0" : "") + segundo;
	}
})

function getValueMasked(maskService, mask, text) {
	if (!angular.isObject(mask)) {
		mask = {mask: mask}
	}
	return maskService.getValueMasked(mask, text);
};
