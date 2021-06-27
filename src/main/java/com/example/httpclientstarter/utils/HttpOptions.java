package com.example.httpclientstarter.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * http请求配置
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HttpOptions {
    /**
     * http请求连接超时时间
     */
    private int connectTimeoutMillis;
    /**
     * http请求读超时时间
     */
    private int soTimeoutMillis;
}
