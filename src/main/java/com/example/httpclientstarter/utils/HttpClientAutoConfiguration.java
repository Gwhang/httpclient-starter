package com.example.httpclientstarter.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * 自动初始化HttpClient
 *
 */
@Configuration
@ConditionalOnProperty(value = "ins.httpclient.enabled", matchIfMissing = true)
@EnableConfigurationProperties(HttpClientCfg.class)
public class HttpClientAutoConfiguration {

    @ConditionalOnMissingBean(CloseableHttpClient.class)
    @Bean(destroyMethod = "close")
    public CloseableHttpClient apacheHttpClient(HttpClientCfg cfg) {
        RequestConfig config =
                RequestConfig.custom().setConnectTimeout(cfg.getConnectTimeout()).setSocketTimeout(cfg.getSoTimeout())
                        .build();
        HttpClientBuilder builder = HttpClients.custom().setDefaultRequestConfig(config);
        if (StringUtils.isNotEmpty(cfg.getUserAgent())) {
            builder.setUserAgent(cfg.getUserAgent());
        }
        builder.setConnectionTimeToLive(cfg.getConnTimeToLive(), TimeUnit.MILLISECONDS);
        builder.setMaxConnTotal(cfg.getMaxTotalConnections()).setMaxConnPerRoute(cfg.getDefaultMaxConnectionsPerHost());
        return builder.build();
    }

    @ConditionalOnBean(CloseableHttpClient.class)
    @Bean
    public HttpClientHelper initHttpClientHelper(CloseableHttpClient httpClient) {
        return HttpClientHelper.setHttpClient(httpClient);
    }

}
