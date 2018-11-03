angular.module('app').service('Resources', ['$http', function ($http) {

	this.getMeses = function () {
		return ["JANEIRO", "FEVEREIRO", "MARÇO", "ABRIL", "MAIO", "JUNHO", "JULHO", "AGOSTO", "SETEMBRO", "OUTUBRO", "NOVEMBRO", "DEZEMBRO"];
	};

	this.getDiasDaSemana = function () {
		return ["SEGUNDA", "TERCA", "QUARTA", "QUINTA", "SEXTA", "SABADO", "DOMINGO"];
	};


    this.getSetups = function() {
		return {
			"CORRECAO_MM": "CORRECAO_MM",
            "CRUZAMENTO_MM": "CRUZAMENTO_MM",
			"OCO": "OCO",
			"OCO_INVERTIDO": "OCO_INVERTIDO",
            "ROMPIMENTO_SUPORTE": "ROMPIMENTO_SUPORTE",
			"ROMPIMENTO_RESISTENCIA": "ROMPIMENTO_RESISTENCIA",
			"ROMPIMENTO_TRIANGULO": "ROMPIMENTO_TRIANGULO",
			"TOPO_DUPLO": "TOPO_DUPLO",
			"TOPO_TRIPLO": "TOPO_TRIPLO",
			"FUNDO_DUPLO": "FUNDO_DUPLO",
			"FUNDO_TRIPLO": "FUNDO_TRIPLO",
		};
	};
	
	this.getPeriodicidades = function () {
		return {
			"HORAS": "HORAS",
			"DIAS": "DIAS",
			"SEMANAS": "SEMANAS",
			"MESES": "MESES"
		};
	};
	
	this.getMimeTypes = function() {
		var mime = "\'";
		mime += "application/pdf,"; //.pdf
		mime += "application/msword,"; //.doc
		mime += "application/vnd.ms-excel,"; //.xls
		mime += "image/png,image/jpeg,image/jpg,"; // images
		mime += "application/vnd.openxmlformats-officedocument.wordprocessingml.document,"; // .docx
		mime += "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet\'"; // .xlsx
		return mime;
	}
	
	this.getLabelsIndicadorMaquina = function() {
		return {
		    selectAll       : "Selecionar todos",
		    selectNone      : "Selecionar nenhum",
		    reset           : "Limpar",
		    search          : "Digite aqui para pesquisar...",
		    nothingSelected : "---"
		};
	}
	
	this.concatenaDataComHora = function(date){
		if(!date.date || !date.time){
			return null;
		}
		return date.date+' '+date.time+':00';
	};
	
	this.desconcatenaDataComHora = function(dateTime, values){
		if(dateTime){
			values.date = moment(dateTime).format('YYYY-MM-DD');
			values.time = moment(dateTime).format('HH:mm');
		}
	};

    return this;
}]);