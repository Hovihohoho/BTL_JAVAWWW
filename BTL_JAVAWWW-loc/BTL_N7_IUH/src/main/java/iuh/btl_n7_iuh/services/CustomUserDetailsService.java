package iuh.btl_n7_iuh.services;

import iuh.btl_n7_iuh.entities.Account; // Cập nhật import
import iuh.btl_n7_iuh.repositories.AccountRepository; // Cập nhật import
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository; // Cập nhật Repository

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("Account not found: " + username);
        }
        return org.springframework.security.core.userdetails.User
                .withUsername(account.getUsername())
                .password(account.getPassword())
                .roles("USER") // Gán vai trò mặc định
                .build();
    }
}