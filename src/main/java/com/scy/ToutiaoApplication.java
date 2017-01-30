package com.scy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.*;

//@SpringBootApplication
//@Target(ElementType.TYPE)
//@Retention(RetentionPolicy.RUNTIME)
//@Documented
//@Inherited
@Configuration
@EnableAutoConfiguration
//@ComponentScan
public class ToutiaoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ToutiaoApplication.class, args);
	}
}
