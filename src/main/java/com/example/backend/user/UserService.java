package com.example.backend.user;

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

    public User socialLogin(SocialProvider socialProvider) {
        Map<String, String> userInfo = socialProvider.getUserInfo();

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername(userInfo.get("id"));
        user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        user.setAvatar_url(userInfo.get("picture"));
        user.setIsEnabled(true);
        user.setEmail(userInfo.get("email"));

        Optional<Role> optionalRole = roleRepository.findByName("USER");
        if (optionalRole.isPresent()) {
            Role role = optionalRole.get();
            user.setRoles(Collections.singleton(role));
        }

        return userRepository.save(user);

    }
}
