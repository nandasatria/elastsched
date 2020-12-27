package com.nandasatria.elastic.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ElasticRestTemplateConfig {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ElasticRestTemplateConfig.class);
	
	@Value("${rest.socket.timeout:30}")
	private int socketTimeout;
	
	@Value("${rest.connect.timeout:30}")
	private int connectTimeout;
	
	@Value("${rest.max.total:10}")
	private int maxTotal;
	
	@Value("${rest.max.route:5}")
	private int maxRoute;
	
	@Value("${rest.retry.count:3}")
	private int retryCount;
	
	
	@Bean("elasticRestTemplate")
	public RestTemplate restTemplate(RestTemplateBuilder builder) throws Exception {
		
		CloseableHttpClient httpClient = null;
		try {
			
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Construct custom HTTPClient with Pooling features for ElasticRestTemplate...");
			
			SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
			sslContextBuilder.loadTrustMaterial(null, new TrustSelfSignedStrategy() {
					/* it always better to have a proper JKS and/or PKS12 based certificated */
					@Override
					public boolean isTrusted(java.security.cert.X509Certificate[] chain, String authType) 
						throws java.security.cert.CertificateException {
					// forcefully trust the given keystore (in this case null) so that return true
					return true;
					}
				});
			
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
					sslContextBuilder.build(), NoopHostnameVerifier.INSTANCE);
			
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
					.register("http", new PlainConnectionSocketFactory())
					.register("https", sslsf)
					.build();

			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(socketTimeout * 1000)
					.setConnectTimeout(connectTimeout * 1000)
					.build();
			
			PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
			connManager.setMaxTotal(maxTotal);
			connManager.setDefaultMaxPerRoute(maxRoute);
			
			httpClient = HttpClients.custom()
					.setDefaultRequestConfig(requestConfig)
					.setConnectionManager(connManager)
					.setRetryHandler((exception, executionCount, context) -> {
						if (executionCount > retryCount) 
							return false;
						return true;
					})
					.build();
				
		} catch (Exception e) {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Construct custom default HTTPClient for CiscoFinesseRestTemplate...");
			
			// default HTTPClient
			httpClient = HttpClients.custom().build();
		}
		
		final CloseableHttpClient client =httpClient;
		
		return builder.requestFactory(() -> new 
			    HttpComponentsClientHttpRequestFactory(client)).build();

	}

}
