angular.module('app').controller('BaseChartController', ['$scope', function ($scope) {

	zingchart.complete = function() {
		$('a[href*="zingchart"]').each(function() {
			this.remove()
		});
	}
	
	$scope.render = {
		hideprogresslogo: true
	};
	
}]);
