package com.springbootcartapp;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


@SpringBootApplication
public class SpringbootCartAppApplication {
	

	public static void main(String[] args) {
		ApplicationContext context=SpringApplication.run(SpringbootCartAppApplication.class, args);
		 System.out.println("Beans loaded: " + Arrays.toString(context.getBeanDefinitionNames()));
		  

        
	}


}
