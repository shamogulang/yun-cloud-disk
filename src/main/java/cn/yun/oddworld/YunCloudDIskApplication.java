package cn.yun.oddworld;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

import cn.yun.oddworld.config.AuthProperties;

@EnableConfigurationProperties({AuthProperties.class})
@SpringBootApplication
@EnableFeignClients
public class YunCloudDIskApplication {
	private static final Logger log = LoggerFactory.getLogger(YunCloudDIskApplication.class);
	public static void main(String[] args) throws Exception {

		SpringApplication.run(YunCloudDIskApplication.class, args);
	}
}
