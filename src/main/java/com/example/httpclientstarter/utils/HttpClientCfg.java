package com.example.httpclientstarter.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * httpclient配置
 *
 */
@ConfigurationProperties(prefix = "ins.httpclient")
@Getter
@Setter
@ToString
public class HttpClientCfg {
    /**
     * 连接超时时间
     */
    private int connectTimeout = 5000;
    /**
     * 响应超时时间
     */
    private int soTimeout = 20000;
    /**
     * userAgent
     */
    private String userAgent;
    /**
     * 最大总连接数
     */
    private int maxTotalConnections = 1024;
    /**
     * 每个host最大连接数
     */
    private int defaultMaxConnectionsPerHost = 256;

    /**
     * 链接默认存活时间
     */
    private int connTimeToLive = 15_000;
}
