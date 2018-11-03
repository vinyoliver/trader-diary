angular.module('app').controller('CarouselAppController', ['$scope', '$modal', function ($scope, $modal) {

    $scope.$watch('entidade', function (entidade) {
    });
    var index = 0;


    var zoomImagemModal = $modal({
		scope: $scope,
		templateUrl: 'views/directives/zoomImagemModal.html',
		show: false
	});

    $scope.uploadImage = function (files) {
        for (index in files) {
            var file = files[index];
            if (file) {
                $scope.file = file;
                var reader = new FileReader();
                reader.onload = (function (f) {
                    return function (e) {
                        // Here you can use `e.target.result` or `this.result` and `f.name`.
                        for (i in $scope.images) {
                            $scope.images[i].active = 'item';
                        }
                        $scope.images.push({file: f, src: e.target.result, active: 'item active'});
                    };
                })(file);
                reader.readAsDataURL(file);
            }
        }
	};
	
    $scope.scrollImage = function (type, id) {
		var incDec = type == 'next' ? 1 : -1;
		if($scope.images.length > 1) {
            var currentId = $('.item.active img.group_' + id).attr('id').substring(1);
            var nextIndex = (parseInt(currentId) + incDec + $scope.images.length) % ($scope.images.length);
            $scope.images[currentId].active = 'item';
			$scope.images[nextIndex].active = 'item active';
		}
	}

    $scope.removeImage = function (id) {
        if ($scope.entidade.imagem) {
            $scope.entidade.imagem = null;
		} else {
            var index = parseInt($('.item.active img.group_' + id).attr('id').substring(1));
			if($scope.images[index].id != null) {
                for (i in $scope.entidade.imagens) {
                    if ($scope.entidade.imagens[i].id == $scope.images[index].id) {
                        $scope.entidade.imagens.splice(i, 1);
                        break;
                    }
                }
			}
		}

//		remove imagem do carousel
		$scope.images.splice(index, 1);
		if($scope.images.length > 0) {
			for(i in $scope.images) {
	    		$scope.images[i].active = 'item';
	    	}
			$scope.images[0].active = 'item active';
		}
	};

	$scope.openModalZoomImagem = function () {
		$scope.imgSrc = $scope.images[$('.item.active img').attr('id')].src;
		zoomImagemModal.show();
	};

}]);