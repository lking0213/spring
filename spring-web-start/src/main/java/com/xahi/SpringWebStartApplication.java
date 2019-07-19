package com.xahi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableJpaAuditing
@EntityScan(basePackages = {"com.xahi"}, basePackageClasses = {
		Jsr310JpaConverters.class
})
@ComponentScan(basePackages = {"com.xahi"})
public class SpringWebStartApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringWebStartApplication.class, args);
	}

	@Bean
	public CommandLineRunner processApplicationSetup(ApplicationSetup applicationSetup){
		return args -> applicationSetup.init();
	}

	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
}
