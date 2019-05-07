app.controller('searchController', function ($scope,searchService) {
    //定义搜索对象的结构
    $scope.searchMap = {'keywords': '', 'category': '','brand':'','spec':{},'price':'','pageNo':1,'pageSize':40,'sort':'','sortField':''};

    //搜索
    $scope.search=function () {
        $scope.searchMap.pageNo=parseInt($scope.searchMap.pageNo);//强转为int格式
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
        var firstPage = 1;//开始页码
        var endPage = $scope.resultMap.totalPages;//截止页码

        if ($scope.resultMap.totalPages> 5) {
            if ($scope.searchMap.pageNo <= 3) {
                endPage = 5;
            }else if ($scope.searchMap.pageNo >= $scope.resultMap.totalPages - 2) {
                firstPage = $scope.resultMap.totalPages - 4;
            } else {
                firstPage = $scope.searchMap.pageNo - 2;
                endPage = $scope.searchMap.pageNo + 2;
            }
        }

        for (var i = firstPage; i <=endPage; i++) {
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
        //重置查询页码
        $scope.searchMap.pageNo = 1;
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
        //重置查询页码
        $scope.searchMap.pageNo = 1;
        //根据改变后的条件查询
        $scope.search();
    }

    //根据分页查询
    $scope.queryByPage=function (pageNo) {
        if (pageNo < 1||pageNo>$scope.resultMap.totalPages) {
        }
        $scope.searchMap.pageNo = pageNo;
        $scope.search();
    }

    //判断当前页是否是首页
    $scope.isTopPage=function () {
        if ($scope.searchMap.pageNo == 1) {
            return true;
        } else {
            return false;
        }
    }
    //判断当前页是否是尾页
    $scope.isEndPage=function () {
        if ($scope.searchMap.pageNo == $scope.resultMap.totalPages) {
            return true;
        } else {
            return false;
        }
    }

    //设置排序规则
    $scope.sortSearch=function(sortField,sort){
        $scope.searchMap.sortField=sortField;
        $scope.searchMap.sort=sort;
        $scope.search();
    }


    //关键字判断是否是品牌
    $scope.keywordsIsBrand=function () {
        for (var i = 0; i < $scope.resultMap.brandList.length; i++) {
            if ($scope.searchMap.keywords.indexOf($scope.resultMap.brandList[i].text)>=0) {
                return true;
            }
        }
        return false;
    }

});