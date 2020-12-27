package com.nandasatria.elastic.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

@Configuration
public class FreemarkerConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(FreemarkerConfig.class);
	
	@Value("${freemarker.config.path}")
	private String configPath;
	
	
	@Bean
	@Primary
    public FreeMarkerConfigurationFactoryBean getFreeMarkerConfiguration() {
		String defaultTemplateLocation = configPath;
		LOGGER.debug("Initiate freemarkerConfig, set to ["+defaultTemplateLocation+"], processing...");
        FreeMarkerConfigurationFactoryBean bean = new FreeMarkerConfigurationFactoryBean();
        bean.setTemplateLoaderPath(defaultTemplateLocation);
        return bean;
    }
}
