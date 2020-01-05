# parametercheck-spring-boot-starter
适用于微服务的参数校验注解 
只需在controller之前加入注解即可进行参数校验

1， 引入 parametercheck-spring-boot-starter依赖 
   
~~~
<dependency>
         <artifactId>parametercheck-spring-boot-starter</artifactId>
         <groupId>top.9421</groupId>
         <version>1.0.1-SNAPSHOT</version>
</dependency>
~~~
   
2，在Controller中使用示例
```

    @GetMapping("/product")
    @ParamCheck(name = "paramMap", params = {
            @ParamCheck.Param(name = "requireProductCategory", type = Integer.class),
            @ParamCheck.Param(name = "requireProductDetail", type = Integer.class, required = false),
            @ParamCheck.Param(name = "productName", type = String.class, required = true)})
    public ResultModel query(@RequestParam Map<String, String> paramMap) {
        // 业务逻辑
        ResultModel resultModel = new ResultModel();
        resultModel.setMsg("success");
        return resultModel;
    }

```

详解

| 注解 | name | 解释 |
| ------ | ------ | ------ |
| @ParamCheck | name | name = "paramMap"应与 query(@RequestParam Map<String, String> paramMap) 对应，表示要校验的参数 |
| @ParamCheck.Param | name | 检验的名称 |
| @ParamCheck.Param | type | 检验的类型 String.class Interger.class 等|
| @ParamCheck.Param | required | 是否必填，默认false 。productName的 required = true 表示必须传入productName |
| @ParamCheck.Param | format | 时间格式化 如"yyyy-MM-dd HH:mm:ss" |

**验证效果**

请求
```
http://localhost:8080/product
```
响应
```
{
    code: 10001,
    msg: "参数[productName]不能为空"
}
```

请求
```
http://localhost:8080/product?productName=%E8%8B%B9%E6%9E%9C
```
响应
```
{
    code: 0,
    msg: "success"
}
```
