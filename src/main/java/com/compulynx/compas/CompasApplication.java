package com.compulynx.compas;

import java.util.Properties;

import com.compulynx.compas.customs.SendMail;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.compulynx.compas.configs.ResourceConfig;

@SpringBootApplication(exclude = SpringDataWebAutoConfiguration.class)
@EnableJpaRepositories
@EnableAutoConfiguration(exclude = {FlywayAutoConfiguration.class, RepositoryRestMvcAutoConfiguration.class})
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class CompasApplication extends SpringBootServletInitializer {
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CompasApplication.class).properties(getProperties());
    } 
	
	public static void main(String[] args) {
		System.out.println("catalina base ###"+ResourceConfig.CATALINA_BASE);
		SpringApplication.run(CompasApplication.class, args);
	}
	
	static Properties getProperties() {
	      Properties props = new Properties();
	      props.put("spring.config.location", ResourceConfig.CATALINA_BASE+"/conf/");
			System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ResourceConfig.CATALINA_BASE+/conf/++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			System.out.println(ResourceConfig.CATALINA_BASE+"/conf/");
		   // props.put("spring.config.location", "D:\\Compulynx\\Projects\\prod_pbu_compas_otc_web\\backend\\src\\main\\resources");
	      return props;
	}
}
