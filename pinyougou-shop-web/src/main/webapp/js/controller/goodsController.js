 //控制层
app.controller('goodsController' ,function($scope,$location,$controller,goodsService,uploadService,itemCatService,typeTemplateService){

	$controller('baseController',{$scope:$scope});//继承
    $scope.entity = {goods:'', goodsDesc: {itemImages: [], specificationItems: []},itemList:[]};
    $scope.image_entity = {};
    //读取列表数据绑定到表单中
	$scope.findAll=function(){
		goodsService.findAll().success(
			function(response){
				$scope.list=response;
			}
		);
	}

	//分页
	$scope.findPage=function(page,rows){
		goodsService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}
		);
	}

	//查询实体
	$scope.findOne=function(){
        var id = $location.search()['id'];
        // alert(id)
        if (id == null) {
            return;
        }
		goodsService.findOne(id).success(
			function(response){
				$scope.entity= response;
                editor.html($scope.entity.goodsDesc.introduction);//商品介绍
                //商品图片
                $scope.entity.goodsDesc.itemImages = JSON.parse($scope.entity.goodsDesc.itemImages);
                //扩展属性
                    $scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.entity.goodsDesc.customAttributeItems);
                    //规格
                var specJson = $scope.entity.goodsDesc.specificationItems;
                $scope.entity.goodsDesc.specificationItems=JSON.parse(specJson);
                $scope.entity.specs = JSON.parse(specJson);

                //SKU列表转化
                for( var i=0;i<$scope.entity.itemList.length;i++ ){
                    $scope.entity.itemList[i].spec = JSON.parse( $scope.entity.itemList[i].spec);
                }

			}
		);
	}

	//保存
	$scope.save=function(){
		var serviceObject;//服务层对象
		if($scope.entity.id!=null){//如果有ID
			serviceObject=goodsService.update( $scope.entity ); //修改
		}else{
			serviceObject=goodsService.add( $scope.entity  );//增加
		}
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询
		        	$scope.reloadList();//重新加载
				}else{
					alert(response.message);
				}
			}
		);
	}


	//批量删除
	$scope.dele=function(){
		//获取选中的复选框
		goodsService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}
			}
		);
	}

	$scope.searchEntity={};//定义搜索对象

	//搜索
	$scope.search=function(page,rows){
		goodsService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}
		);
	}

    //增加商品
    $scope.add=function(){

		// alert(editor.html());
        $scope.entity.goodsDesc.introduction=editor.html();
        alert( "introduction: "+$scope.entity.goodsDesc.introduction)

        goodsService.add( $scope.entity  ).success(
            function(response){
                if(response.success){
                	alert("增加成功")
					//清空表单
                    $scope.entity={};
                	//清空富文本编辑器
                    editor.html("");
                }else{
                    alert(response.message);
                }
            }
        );
    }

    //上传图片
    $scope.uploadFile=function () {
        uploadService.uploadFile().success(
            function (response) {
                if (response.success) {
                    $scope.image_entity.url = response.message;
                } else {
                    alert(response.message)
                }
            }
        )

    }
    //将当前上传的图片实体存入列表
    $scope.add_image_entity=function () {

        $scope.entity.goodsDesc.itemImages.push($scope.image_entity);
    }

    //移除图片
    $scope.remove_image_entity=function (index) {
        $scope.entity.goodsDesc.itemImages.splice(index, 1);
    }

    //查询1级商品分类列表
    $scope.selectItemCat1List=function () {
		// alert("初始化查询")
		itemCatService.findByParentId(0).success(
			function (response) {
                $scope.itemCat1List = response;
            }
		);
    }
	//检测设置的变量的变化,读取二级变量的parentId
    $scope.$watch('entity.goods.category1Id',function (newValue, oldValue) {
		// alert(newValue);
        itemCatService.findByParentId(newValue).success(
            function (response) {
                $scope.itemCat2List = response;
            }
        );
    });

    //检测设置的变量的变化,读取三级变量的parentId
    $scope.$watch('entity.goods.category2Id',function (newValue, oldValue) {
        // alert(newValue);
        itemCatService.findByParentId(newValue).success(
            function (response) {
                $scope.itemCat3List = response;
            }
        );
    });

    //读取模板id
    $scope.$watch('entity.goods.category3Id',function (newValue, oldValue) {
        // alert(newValue);
        itemCatService.findOne(newValue).success(
            function (response) {
                $scope.entity.goods.typeTemplateId = response.typeId;
            }
        );
    });

    //读取模板Id后读取品牌列表
    $scope.$watch('entity.goods.typeTemplateId',function (newValue, oldValue) {
            //模板id改变后清空集合的内容
        if ($scope.entity.goods.id == null) {
            $scope.entity.goodsDesc.specificationItems = [];
        }

            // alert(newValue);
            typeTemplateService.findOne(newValue).success(
                function (response) {
                    $scope.typeTemplate = response;
                    // alert($scope.typeTemplate.brandIds)
                    $scope.typeTemplate.brandIds = JSON.parse($scope.typeTemplate.brandIds);
                    if ($location.search()['id'] == null) {//解决与现有产品查询出的结果冲突问题
                        $scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.typeTemplate.customAttributeItems);
                    }
                }
            );
            //读取规格列表
            typeTemplateService.findSpecList(newValue).success(
                function(response){
                    $scope.specList=response;
                }
            );
    });

    $scope.updateSpecAttribute = function ($event,name, value) {
        var object = $scope.searchObjectByKey($scope.entity.goodsDesc.specificationItems, 'attributeName', name);

        if (object != null) {
            if ($event.target.checked) {
                object.attributeValue.push(value);
            } else {
                object.attributeValue.splice(
                    object.attributeValue.indexOf(value),1
				)
                //如果选项都取消了，将此条记录移除
                if(object.attributeValue.length==0){
                    $scope.entity.goodsDesc.specificationItems.splice(
                        $scope.entity.goodsDesc.specificationItems.indexOf(object),1);
                }
            }
        } else {
            $scope.entity.goodsDesc.specificationItems.push({"attributeName":name,"attributeValue":[value]});
        }
    };

    //创建SKU
	$scope.createItemList=function () {
        $scope.entity.itemList = [{spec:{},price:0,num:99999,status:'0',isDefault:'0' }];//初始化
        var items= $scope.entity.goodsDesc.specificationItems;
        for (var i = 0; i < items.length; i++) {
            $scope.entity.itemList= addColumn($scope.entity.itemList, items[i].attributeName, items[i].attributeValue);

        }

    }

    addColumn=function (list,columnName,columnValues) {
        var newList = [];

        for (var i = 0; i < list.length; i++) {
            var oldRow = list[i];
            for (var j = 0; j < columnValues.length; j++) {
                var newRow = JSON.parse(JSON.stringify(oldRow));
                newRow.spec[columnName] = columnValues[j];
                newList.push(newRow);
            }
        }
        return newList;
    }

    $scope.status = ['未审核','已审核','审核未通过','已通过'];

    $scope.itemCatList = [];

	$scope.findItemCatList=function () {
        itemCatService.findAll().success(
            function (response) {
                for (var i = 0; i < response.length; i++) {
                    $scope.itemCatList[response[i].id] = response[i].name;
                }
            }
        );

    }

    $scope.checkAttributeValue=function (specName,optionName) {

        var items= $scope.entity.specs;
        // alert(items);
        var object= $scope.searchObjectByKey(items,'attributeName',specName);
        if(object==null){
            return false;
        }else{
            if(object.attributeValue.indexOf(optionName)>=0){
                return true;
            }else{
                return false;
            }
        }
    }

});	
