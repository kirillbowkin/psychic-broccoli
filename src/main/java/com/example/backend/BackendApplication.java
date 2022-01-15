package com.example.backend;

import com.auth0.jwt.algorithms.Algorithm;
import com.example.backend.model.role.Role;
import com.example.backend.model.role.RoleRepository;
import com.example.backend.model.user.User;
import com.example.backend.model.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashSet;

@SpringBootApplication
public class BackendApplication {

	@Autowired
	private RoleRepository roleRepository;

	@Value("${secret.key}")
	private String secret_key;

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@PostConstruct
	public void setup() {

//		Role userRole = new Role();
//		userRole.setName("USER");
//
//		Role adminRole = new Role();
//		adminRole.setName("ADMIN");
//		adminRole.setColor("red");
//
//		roleRepository.saveAll(new HashSet<>() {{
//			add(userRole);
//			add(adminRole);
//		}});
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public Algorithm algorithm() {
		return Algorithm.HMAC256(secret_key);
	}

}
