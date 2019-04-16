app.controller('brandController',function ($scope,$controller,brandService) {

    //要继承的controller
    $controller('baseController',{$scope:$scope});

    //查询品牌列表
    $scope.findAll = function () {
        brandService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    };





    $scope.findPage = function (page, size) {
        brandService.findPage(page,size).success(
            function (response) {
                $scope.list = response.rows;//显示当前页数据
                $scope.paginationConf.totalItems = response.total;//更新总记录数

            }
        )
    };

    //实现新增
    $scope.save = function () {

        var object=null;
        if ($scope.entity.id != null) {
            object = brandService.update($scope.entity);
        } else {
            object = brandService.add($scope.entity);
        }
        object.success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();//成功了，重新加载列表
                } else {
                    alert(response.message);//失败了，弹出信息
                }
            }
        );
    };

    //查询实体
    $scope.findOne = function (id) {
        brandService.findOne(id).success(
            function (response) {
                $scope.entity = response;
            }
        );
    };


    //删除选中的实体
    $scope.dele = function () {
        if (confirm('确定要删除选中的品牌吗？')) {
            brandService.dele($scope.selectIds).success(
                function (response) {
                    if (response.success) {
                        $scope.reloadList();
                    } else {
                        alert(response.message);
                    }
                }
            );
        }
    };

    $scope.searchEntity = {};//初始化
    //条件查询
    $scope.search=function (page,size) {
        brandService.serach(page, size, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;//显示当前页数据
                $scope.paginationConf.totalItems = response.total;//更新总记录数

            }
        );
    }

});
