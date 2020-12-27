package com.nandasatria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = { "com.nandasatria" })
@EnableAsync
@EnableScheduling
@PropertySource({
	"file:config/application.properties",
	"file:config/rest-template.properties",
	"file:config/elastic.properties",
	"file:config/freemarker.properties",
	"file:config/email.properties"
})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
