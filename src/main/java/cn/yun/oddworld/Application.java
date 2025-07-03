package cn.yun.oddworld;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Collection;

import cn.yun.oddworld.config.AuthProperties;

@EnableConfigurationProperties({AuthProperties.class})
@SpringBootApplication
@EnableFeignClients
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);


	@Autowired
	private Environment env;

	@PostConstruct
	public void initApplication() {
		log.info("Running with Spring profile(s) :{} ", Arrays.asList(env.getActiveProfiles()));
		Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
		if (activeProfiles.contains(ConfigConstant.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(ConfigConstant.SPRING_PROFILE_PRODUCTION)) {
			log.error("You have error configured your application! It should not run " +
					"with both the 'dev' and 'prod' profiles at the same time.");
		}
		if (activeProfiles.contains(ConfigConstant.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(ConfigConstant.SPRING_PROFILE_CLOUD)) {
			log.error("You have error configured your application! It should not" +
					"run with both the 'dev' and 'cloud' profiles at the same time.");
		}
	}

	/**
	 * Main method, used to run the application.
	 *
	 * @param args the vo line arguments
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		SpringApplication app = new SpringApplication(Application.class);

		ApplicationContext appContext = app.run(args);

		Environment env = appContext.getEnvironment();

		log.info("\n----------------------------------------------------------\n\t" +
						"Application '{}' is running! Access URLs:\n\t" +
						"Local: \t\thttp://localhost:{}\n\t" +
						"External: \thttp://{}:{}\n----------------------------------------------------------",
				env.getProperty("spring.application.name"),
				env.getProperty("server.port"),
				InetAddress.getLocalHost().getHostAddress(),
				env.getProperty("server.port"));


	}
}
