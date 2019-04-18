app.controller('baseController', function ($scope) {
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

    //提取JSON字符串中的某个属性，转换格式
    $scope.jsonToString=function (jsonString ,key) {
        var json = JSON.parse(jsonString);
        var value = "";
        for (var i = 0; i < json.length; i++) {
            value +=","+ json[i][key];
        }

        var s = value.substring(1);

        return s;
    }

});