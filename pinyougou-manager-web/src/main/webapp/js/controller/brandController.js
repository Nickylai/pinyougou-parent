app.controller('brandController',function ($scope,brandService) {
    //查询品牌列表
    $scope.findAll = function () {
        brandService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    };

    //分页控件配置
    $scope.paginationConf = {
        currentPage: 1,//当前页
        totalItems: 10,//总记录数
        itemsPerPage: 10,//每页记录数
        perPageOptions: [10, 20, 30, 40, 50],//分页选项
        onChange: function () {
            $scope.reloadList();
        }
    };

    //刷新列表
    $scope.reloadList = function () {
        $scope.search($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);

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

    //选中实体复选框
    $scope.selectIds=[];//用户勾选的id集合
    $scope.updateSelection = function ($event, id) {
        if ($event.target.checked) {//判断当前复选框是否被选中状态
            $scope.selectIds.push(id);//向集合中添加元素
        } else {
            var index = $scope.selectIds.indexOf(id);//查找id的索引
            $scope.selectIds.splice(index, 1);//index移除的位置，1为移除的个数
        }
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
