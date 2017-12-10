package com.nicky;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan("com.nicky.mapper")
public class RedisEhcacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedisEhcacheApplication.class, args);
	}
}
