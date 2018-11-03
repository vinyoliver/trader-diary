angular.module('app').controller('DateTimeAppController', ['$scope', '$modal', 'Resources', function ($scope, $modal, Resources) {
	$scope.Resources = Resources;
	$scope.dateTime = {};
	
	$scope.$watch('ngModel', function(ngModel) {
		if(ngModel !== undefined) {
			Resources.desconcatenaDataComHora(ngModel, $scope.dateTime);
		} else {
			$scope.dateTime = {};
		}
	});
			
	$scope.disparaDate = function(el){
		jQuery('#'+el.id+'_date').focus();
	}
	
	$scope.disparaTime = function(el){
		jQuery('#'+el.id+'_time').focus();
	}
	
	$scope.change = function(el){
		if(jQuery('#'+el.id+'_date').val() && $scope.dateTime.date === undefined) {
			date = jQuery('#'+el.id+'_date').val();
			$scope.dateTime.date = date.substring(6,10) + "-" + date.substring(3,5) + "-" + date.substring(0,2);
		}
		el.$parent.ngModel = Resources.concatenaDataComHora($scope.dateTime);
	}
	
}]);