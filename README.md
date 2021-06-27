## http client的封装类

### 1 简介

httpClient封装类，已封装好get，post等方法，方便简单



### 2 使用方法


**配置**

```properties
# 选填，连接超时时间，默认5秒
ins.httpclient.connectTimeout=
# 选填，响应超时时间，默认20秒
ins.httpclient.soTimeout=
# 选填，userAgent，默认null
ins.httpclient.userAgent=
# 选填，最大总连接数，默认1024
ins.httpclient.maxTotalConnections=
# 选填，每个host最大连接数，默认256
ins.httpclient.defaultMaxConnectionsPerHost=
```



**HttpClientHelper**

httpclient工具类，提供各种get，post方法，直接使用HttpClientHelper.getInstance()获取实例调用方法即可使用

```
public class HttpClientHelper {
	/**
     * 
     * @param url 请求url
     * @param json 参数json化字符串
     * @param timeout 超时时间，单位毫秒
     * @return
     * @throws Exception
     */
	public String postJson(String url, String json, Integer timeout);
	// 发送post请求，obj为内容对象，内部自动转json
	public String postJson(String url, Object obj);
	// 其他方法见源码
}
```



样例

```java
import com.jdaz.ins.common.httpclient.HttpClientHelper;
public class TestService {
    public void test(){
        // 其他方法同理
    	HttpClientHelper.getInstance().postJson(url, obj);
    }
}
```

