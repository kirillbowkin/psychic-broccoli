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

@SpringBootApplication
public class BackendApplication {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;

	@Value("${secret.key}")
	private String secret_key;

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@PostConstruct
	public void loadUsers() {
		User user = new User();
		user.setUsername("test123");
		user.setPassword(new BCryptPasswordEncoder().encode("1234"));
		user.setIsEnabled(true);

		Role role = new Role();
		role.setName("USER");
		roleRepository.save(role);

		user.setRoles(Collections.singleton(role));
		userRepository.save(user);

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
