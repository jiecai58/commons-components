🥤 使用commons-components-exception(公共能力sdk-全局异常捕获)
---

`commons-components-exception`是一款轻量易上手的全局异常捕获工具，包含以下功能：

- 全局捕获异常
- Assert捕获异常
- 自定义http状态不处理
- 支持自定义response

## 在Maven项目中使用

在项目`pom.xml`文件中，增加`commons-components-exception`依赖，具体方法如下。

建议先添加一个标识commons-components-exception版本的`property`，便于统一管理：

```xml
<properties>
    <commons.exception.version>1.0.1-2021082616</commons.exception.version>
</properties>
```

在`dependencies`列表添加TestableMock依赖：

```xml
<dependencies>
    <dependency>
        <groupId>com</groupId>
        <artifactId>commons-components-exception</artifactId>
        <version>${commons.exception.version}</version>
    </dependency>
</dependencies>
```
##自定义response
默认异常跑出给`BizResponse`,支持自定义`Response`
```java
public abstract class HandlerExceptionResponse implements ExceptionResponse {

    @Override
    public Object throwExceptionInfo(HttpServletRequest request, HttpServletResponse response, long errCode, String errMsg) {
        BizResponse bizResponse = new BizResponse();
        bizResponse.setCode((int) errCode);
        bizResponse.setMessage(errMsg);
        return bizResponse;
    }
}
```

自定义返回`Response`案例：
```java
@Component
class ThrowExceptionResponse extends HandlerExceptionResponse {

    @Override
    public Object throwExceptionInfo(HttpServletRequest request, HttpServletResponse response, long errCode, String errMsg) {

        ArrayList<String> objects = new ArrayList<>();
        objects.add("/userAddress/changeUserAddress");
        if (objects.contains(request.getRequestURI())) {
            return new BaseResponse(errCode, errMsg);
        }

        BizResponse bizResponse = new BizResponse();
        bizResponse.setCode((int) errCode);
        bizResponse.setMessage(errMsg);
        return bizResponse;
    }
}
```

##错误码规约
- 规范
- 【强制】code以5位int类型组成，10000-19999为保留号段，业务以20000开始提供编排，各业务请implements BizEnum，具体业务线与号段的映射见附录；
- 【强制】业务线自定义异常码，必须以业务线分配的号段开头；
- 【强制】业务线自定义的异常码，默认均为业务异常，如需定义非业务异常请联系@中间件客服 报备；
- 【强制】错误码对应的name以英文大写字母和数字组成，且必须以大写英文字母开头，必须使用有自然语义的英文单词或国际通用的缩写，禁止滥用缩写以及拼音、英文混用，多个自然语义单词之间以下划线(_)分割；
- 【推荐】错误码对应的desc以最简单的方式描述清楚错误码的具体意义；