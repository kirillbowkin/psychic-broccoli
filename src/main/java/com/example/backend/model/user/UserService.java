package com.example.backend.model.user;

import com.example.backend.model.role.Role;
import com.example.backend.model.role.RoleRepository;
import com.example.backend.social.SocialProvider;
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
        user.setAvatarUrl(userInfo.get("picture"));
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
