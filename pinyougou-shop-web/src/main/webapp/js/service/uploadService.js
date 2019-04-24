app.service('uploadService', function ($http) {

    //上传文件
    this.uploadFile=function () {
        var fromData = new FormData();
        fromData.append('file', file.files[0]);//file 文件上传匡的name
        return $http({
            url: '../upload.do',
            method: 'post',
            data: fromData,
            headers:{'Content-Type':undefined},
            transformRequest:angular.identity
        })
    }

});