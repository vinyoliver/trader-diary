'use strict';

angular.module('app', ['ngSanitize', 'ngCookies', 'ui.router', 'ui.mask', 'ui.select', 'ngResource', 'mgcrea.ngStrap', 'pascalprecht.translate', 'ngTable', 'ngMask', 'ngFileUpload', 'angucomplete-alt', 'ui.bootstrap', 'ui.utils.masks', 'ui.sortable', 'isteven-multi-select', 'ngCpfCnpj', 'zingchart-angularjs', 'jcs-autoValidate', 'mwl.calendar', 'ui.bootstrap',
                        'ngTagsInput', 'angular-confirm', 'oc.lazyLoad', 'ui.tinymce', 'frapontillo.bootstrap-duallistbox'])
	.config(['$translateProvider', function ($translateProvider) {

		//Config. angular translate (i18n)
		$translateProvider.useStaticFilesLoader({
			prefix: 'i18n/messages_',
			suffix: '.json'
		});
		$translateProvider.preferredLanguage('pt_BR');
		$translateProvider.useSanitizeValueStrategy('escapeParameters');

	}])

	.config(['$datepickerProvider', function ($datepickerProvider) {
		angular.extend($datepickerProvider.defaults, {
			dateFormat: 'dd/MM/yyyy',
			startWeek: 1,
			lang: 'pt-br',
			modelDateFormat: "yyyy-MM-dd HH:mm:ss",
			dateType: "string"
		});
	}])


	.config(['$timepickerProvider', function ($timepickerProvider) {
		angular.extend($timepickerProvider.defaults, {
			modelTimeFormat: "yyyy-MM-dd HH:mm:ss",
			timeType: "string"
		});
	}])

	.config(['$httpProvider', function ($httpProvider) {
		$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
	}])
	
	.config(function(calendarConfig) {
	    calendarConfig.dateFormatter = 'moment'; 
	    calendarConfig.allDateFormats.moment.date.hour = 'HH:mm'; 
	 })                    
	
	.run(['$rootScope', function ($rootScope) {
		$rootScope.$on('loading:progress', function() {
			$('.loading').show();
		});
	
		$rootScope.$on('loading:finish', function() {
			$('.loading').hide();
		});

		$rootScope.showCalendar = function (el) {
			jQuery('#' + el.id + '_date').focus();
		}
	}])
	
    .run(['validator', function (validator) {
            validator.setValidElementStyling(false);
    }])
	
    .run(['bootstrap3ElementModifier', function (bootstrap3ElementModifier) {
          bootstrap3ElementModifier.enableValidationStateIcons(true);
    }])

	angular.module('jcs-autoValidate')
	.run(['defaultErrorMessageResolver', function (defaultErrorMessageResolver) {
	    defaultErrorMessageResolver.setI18nFileRootPath('i18n/');
	    defaultErrorMessageResolver.setCulture('pt-BR');
	}]);