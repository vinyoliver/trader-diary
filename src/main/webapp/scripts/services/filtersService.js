angular.module('app').service('FiltersService', ['$state', '$location', function ($state, $location) {

	var Filter = {};
	var state = '';
	
    return {
        getFilters: function () {
        	if(state.indexOf($state.current.name) == -1){
        		Filter = {};
        	}
        	return Filter;
        },
        setFilters: function(value) {
        	Filter = {};
        	state = $state.current.name;
        	angular.forEach( value, function( prop, key ) {
        		if(Array.isArray(prop)){
        			Filter[key] = value[key];
        			delete value[key];
        		}
        	});
        },
        getLocationSearch: function () {
        	if(state.indexOf($state.current.name) == -1){
        		Filter = {};
        		$location.search({});
        	}
        	return JSON.parse(JSON.stringify($location.search()));
        }
        
    };

}]);