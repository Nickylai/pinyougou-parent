app.controller('searchController', function ($scope,searchService) {
    //定义搜索对象的结构
    $scope.searchMap = {'keywords': '', 'category': '','brand':'','spec':{}};

    //搜索
    $scope.search=function () {
        searchService.search($scope.searchMap).success(
            function (response) {
                $scope.resultMap = response;
            }
        );
    }

    //添加搜索项 改变searchMap的值
    $scope.addSearchItem=function (key,value) {
        if (key == 'category' || key == 'brand') {
            //如果用户点击的是品牌或分类
            $scope.searchMap[key] = value;
        } else {
            //用户点击的是规格
            $scope.searchMap.spec[key] = value;
        }
    }

});