package uz.jvh.nextpizza.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.jvh.nextpizza.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {  // ← Email
        return userRepository.findByEmail(email)  // ← Email bo'yicha
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }
}
