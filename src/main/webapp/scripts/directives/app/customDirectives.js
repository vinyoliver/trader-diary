(function () {

    angular.module('app').directive('decimalValue', function () {
		return {
			require: 'ngModel',
			link: function (scope, elem, attrs, ctrl) {
				ctrl.$formatters.unshift(function (value) {
					// what you return here will be passed to the text field
					var valid = value == null || isFinite(value);
					return valid && value != null ? numeral(value).format('0,0.00') : '0,00';
				});
			}
		};
	});


    angular.module('app').directive('hasRole', ['$usuarioLogadoService', '$http', function ($usuarioLogadoService, $http) {
		return {
			link: function (scope, element, attrs) {
				if (!angular.isString(attrs.hasRole)) throw "hasPermission value must be a string";
				var permissions = attrs.hasRole.split(",");
				
				if (!$usuarioLogadoService.isUsuarioLogado()) {
					$http.get('rest/usuario/current-user').then(function (data) {
						$usuarioLogadoService.setUsuarioLogado(data);
						
						if ($usuarioLogadoService.isAuthorized(permissions)) {
							element.show();
						} else {
							element.hide();
						}
					});
				} else {
					if ($usuarioLogadoService.isAuthorized(permissions)) {
						element.show();
					} else {
						element.hide();
					}
				}
			}
		}
	}]);


    angular.module('app').directive('uppercase', function () {
		return {
			require: 'ngModel',
			link: function (scope, element, attrs, modelCtrl) {
				modelCtrl.$parsers.push(function (input) {
					return input ? input.toUpperCase() : "";
				});
				$(element).css("text-transform", "uppercase");
			}
		};
	});

    angular.module('app').directive("compareTo", function () {
	    return {
	        require: "ngModel",
	        scope: {
	            otherModelValue: "=compareTo"
	        },
	        link: function(scope, element, attributes, ngModel) {
	             
	            ngModel.$validators.compareTo = function(modelValue) {
	                return modelValue == scope.otherModelValue;
	            };
	 
	            scope.$watch("otherModelValue", function() {
	                ngModel.$validate();
	            });
	        }
	    };
	});


    angular.module('app').directive('mask2', [function () {
		return {
			require: 'ngModel',
			link: function (scope, element, attrs, ctrl) {
				scope.$watch(function () {
					return element.val();
				}, function () {
					if (attrs.mask2 == 'phone') {
						element.setMask("(99) 9999-99999").ready(function (event) {
							var phone = element.val().replace(/\D/g, '');
							element.unsetMask();
							if (phone.length > 10) {
								element.setMask("(99) 99999-9999");
							} else {
								element.setMask("(99) 9999-99999");
							}
						});
					} else {
						element.setMask(attrs.mask2);
					}
				})
			}
		};
	}]);


    angular.module('app').directive('carousel', [function (scope) {
		return {
			restrict :'E',
			templateUrl: '/views/directives/carousel.html',

			scope:{
				entidade: '=',
				images: '=',
				showAdd: '=',
				showRemove: '=',
				showZoom: '=',
				showControl: '=',
				id: '@'
			}
		};
	}]);

    angular.module('app').directive('checkboxGroup', function () {
		return {
			restrict: 'A',
			link: function(scope, elem, attrs) {
				//
				if (scope.array.indexOf(scope.item.id) !== -1) {
					elem[0].checked = true;
				}
				
				elem.bind('click', function() {
					var index = scope.array.indexOf(scope.item.id);
					
					if (elem[0].checked) {
						if (index === -1)
							scope.array.push(scope.item.id);
					} else {
						if (index !== -1) scope.array.splice(index, 1);
					}
					
					scope.$apply(scope.array.sort(function(a,b) {
						return a - b;
					}));
				});
			}
		}
	});


    angular.module('app').directive('changeOnBlur', function () {
		return {
			restrict: 'A',
			require: 'ngModel',
			link: function(scope, elm, attrs, ngModelCtrl) {
				if (attrs.type === 'radio' || attrs.type === 'checkbox')
					return;

				var expressionToCall = attrs.changeOnBlur;

				var oldValue = null;
				elm.bind('focus',function() {
					scope.$apply(function() {
						oldValue = elm.val();
					});
				})
				elm.bind('blur', function() {
					scope.$apply(function() {
						var newValue = elm.val();
						
						if (newValue !== oldValue){
							scope.$eval(expressionToCall);
						}
					});
				});
			}
		};
	});

    angular.module('app').directive('input', function () {
	    function link(scope, element, attrs, ngModel) {
	        function allowSchemelessUrls() {
	            var URL_REGEXP = /^((?:http|ftp)s?:\/\/)(?:(?:[A-Z0-9](?:[A-Z0-9-]{0,61}[A-Z0-9])?\.)+(?:[A-Z]{2,6}\.?|[A-Z0-9-]{2,}\.?)|localhost|\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3})(?::\d+)?(?:\/?|[\/?]\S+)$/i;  
	            ngModel.$parsers.unshift(function(value) {
	                if (!URL_REGEXP.test(value) && URL_REGEXP.test('http://' + value)) {
	                    return 'http://' + value;
	                } else {
	                    return value;
	                }
	            });
	            ngModel.$validators.url = function(value) {
	                return ngModel.$isEmpty(value) || URL_REGEXP.test(value);
	            };
	        }

	        if (ngModel && attrs.type === 'url') {
	            allowSchemelessUrls();
	        }
	    }

	    return {
	        require: '?ngModel',
	        link: link
	    };
	});

    angular.module('app').directive('dateTime', [function (scope, ngModel) {
		return {
			restrict :'E',
			require: 'ngModel',
			templateUrl: '/views/directives/dateTime.html',
				scope:{
					ngModel: '=',
					id: '@',
					ngRequired: '=',
					blur: '&ngBlur',
					ngDisabled: '=',
					minDate: '@',
					maxDate: '@',
					placeholder: '@'
				}
		};
	}]);

    angular.module('app').directive('onFinishRender', ["$timeout", function ($timeout) {
        return {
            restrict: 'A',
            link: function (scope, element, attr) {
                if (scope.$last === true) {
                    $timeout(function () {
                        scope.$emit(attr.onFinishRender);
                    });
                }
            }
        }
    }]);

})();

