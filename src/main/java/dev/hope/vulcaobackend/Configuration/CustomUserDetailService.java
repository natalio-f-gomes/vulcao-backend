package dev.hope.vulcaobackend.Configuration;


import dev.hope.vulcaobackend.Models.AccountEntities.EmployerModel;
import dev.hope.vulcaobackend.Models.AccountEntities.StaffModel;
import dev.hope.vulcaobackend.Repositories.EmployerRepository;
import dev.hope.vulcaobackend.Repositories.StaffRepository;
import dev.hope.vulcaobackend.Repositories.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired private UserRepository userRepository;
    @Autowired
    private EmployerRepository employerRepository;
    @Autowired
    private StaffRepository staffRepository;

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {

        Optional<EmployerModel> executive = employerRepository.findByUsername(username);
        if (executive.isPresent()) {
            EmployerModel user = executive.get();
            return User.withUsername(user.getUsername())
                    .password(user.getPassword())
                    .roles("EMPLOYER")
                    .build();
        }

        Optional<StaffModel> staff = staffRepository.findByUsername(username);
        if (staff.isPresent()) {
            StaffModel user = staff.get();
            return User.withUsername(user.getUsername())
                    .password(user.getPassword())
                    .roles("STAFF")
                    .build();
        }

        return userRepository.findByUsername(username)
                .map(user -> User.withUsername(user.getUsername())
                        .password(user.getPassword())
                        .roles("USER")
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
