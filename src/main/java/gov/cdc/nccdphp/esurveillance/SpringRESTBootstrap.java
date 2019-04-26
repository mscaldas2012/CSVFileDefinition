package gov.cdc.nccdphp.esurveillance;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@EntityScan("gov.cdc.nccdphp.esurveillance")
//@EnableJpaRepositories(basePackages = {"gov.cdc.nccdphp.esurveillance"})
public class SpringRESTBootstrap {
	Log log = LogFactory.getLog(SpringRESTBootstrap.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringRESTBootstrap.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**");
			}
		};
	}



//	@Profile("DEV")
//	@Bean
//	public CommandLineRunner loadMDE(DataLoader dataLoader) {
//		log.info("Loading MDE 4 WW");
//		return (args) -> {
//			dataLoader.loadData("WW_MDE903.csv", "Wise Woman MDE", "WW_MDE", "9.0.3");
//		};
//	}

}
