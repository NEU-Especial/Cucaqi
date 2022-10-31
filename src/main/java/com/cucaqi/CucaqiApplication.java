package com.cucaqi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.cucaqi.mapper")
public class CucaqiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CucaqiApplication.class, args);
	}

}
