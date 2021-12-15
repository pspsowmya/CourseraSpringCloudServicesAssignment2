package org.magnum.mobilecloud.video;



import java.security.Principal;

import org.magnum.mobilecloud.video.auth.SecurityConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RequestMapping;


@SpringBootApplication
@EnableResourceServer


public class Application {


	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	

}
