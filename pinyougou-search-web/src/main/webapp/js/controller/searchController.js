app.controller('searchController', function ($scope,searchService) {
    //定义搜索对象的结构
    $scope.searchMap = {'keywords': '', 'category': '','brand':'','spec':{},'price':'','pageNo':1,'pageSize':40};

    //搜索
    $scope.search=function () {
        searchService.search($scope.searchMap).success(
            function (response) {
                $scope.resultMap = response;
                buildPageLable();
            }
        );
    }
    //构建页码标签
    buildPageLable = function () {
        $scope.pageLabel = [];
        for (var i = 1; i < $scope.resultMap.totalPages; i++) {
            $scope.pageLabel.push(i);
        }
    };

    //添加搜索项 改变searchMap的值
    $scope.addSearchItem=function (key,value) {
        if (key == 'category' || key == 'brand'||key=='price') {
            //如果用户点击的是品牌或分类
            $scope.searchMap[key] = value;
        } else {
            //用户点击的是规格
            $scope.searchMap.spec[key] = value;
        }
        //根据改变后的条件查询
        $scope.search();
    }

    //撤销搜索项
    $scope.removeSearchItem=function (key) {
        if (key == 'category' || key == 'brand'||key=='price') {
            //如果用户点击的是品牌或分类
            $scope.searchMap[key] = '';
        } else {
            //用户点击的是规格
           delete $scope.searchMap.spec[key] ;
        }
        //根据改变后的条件查询
        $scope.search();
    }

    //

});