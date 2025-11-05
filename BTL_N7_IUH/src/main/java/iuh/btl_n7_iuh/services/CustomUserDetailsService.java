package iuh.btl_n7_iuh.services;

import iuh.btl_n7_iuh.entities.Account;
import iuh.btl_n7_iuh.repositories.AccountRepository;
import iuh.btl_n7_iuh.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {


    private final AccountRepository accountRepository; // Cập nhật Repository

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("Không tìm thấy tài khoản: " + username);
        }

        String roleName = account.getRole().getName();
        GrantedAuthority authority = new SimpleGrantedAuthority(roleName);

        // ✅ Dùng CustomUserDetails để Thymeleaf có thể truy cập principal.fullName
        return new CustomUserDetails(account, Collections.singletonList(authority));
    }
}

