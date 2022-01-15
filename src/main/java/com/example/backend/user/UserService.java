package com.example.backend.user;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> getUser(String username) {
        return userRepository.findByUsername(username);
    }
    public User socialLogin(SocialProvider socialProvider) {
        Map<String, String> userInfo = socialProvider.getUserInfo();

        Optional<User> optionalUser = userRepository.findByUsername(userInfo.get("id"));
        if(optionalUser.isPresent()) {
            return optionalUser.get();
        }

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername(userInfo.get("id"));
        user.setProfileName(userInfo.get("name"));
        user.setPassword(passwordEncoder.encode("")); //TODO: Think about password for social users
        user.setAvatar_url(userInfo.get("picture"));
        user.setIsEnabled(true);
        user.setEmail(userInfo.get("email"));
        user.setIsSocial(true);

        Optional<Role> optionalRole = roleRepository.findByName("USER");
        if (optionalRole.isPresent()) {
            Role role = optionalRole.get();
            user.setRoles(Collections.singleton(role));
        }

        return userRepository.save(user);

    }
}
