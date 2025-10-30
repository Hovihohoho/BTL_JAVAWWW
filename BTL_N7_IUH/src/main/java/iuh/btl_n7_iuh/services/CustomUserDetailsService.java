package iuh.btl_n7_iuh.services;

<<<<<<< HEAD
import iuh.btl_n7_iuh.entities.Account;
import iuh.btl_n7_iuh.repositories.AccountRepository;
import iuh.btl_n7_iuh.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
=======
import iuh.btl_n7_iuh.entities.Account; // Cập nhật import
import iuh.btl_n7_iuh.repositories.AccountRepository; // Cập nhật import
>>>>>>> 2ae1df9f312eac7306823a73ceed800f5fac9dfd
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
<<<<<<< HEAD

import java.util.Collections;
=======
import lombok.RequiredArgsConstructor;
>>>>>>> 2ae1df9f312eac7306823a73ceed800f5fac9dfd

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

<<<<<<< HEAD
    private final AccountRepository accountRepository;
=======
    private final AccountRepository accountRepository; // Cập nhật Repository
>>>>>>> 2ae1df9f312eac7306823a73ceed800f5fac9dfd

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
<<<<<<< HEAD
            throw new UsernameNotFoundException("Không tìm thấy tài khoản: " + username);
        }

        String roleName = account.getRole().getName();
        GrantedAuthority authority = new SimpleGrantedAuthority(roleName);

        // ✅ Dùng CustomUserDetails để Thymeleaf có thể truy cập principal.fullName
        return new CustomUserDetails(account, Collections.singletonList(authority));
    }
}
=======
            throw new UsernameNotFoundException("Account not found: " + username);
        }
        return org.springframework.security.core.userdetails.User
                .withUsername(account.getUsername())
                .password(account.getPassword())
                .roles("USER") // Gán vai trò mặc định
                .build();
    }
}
>>>>>>> 2ae1df9f312eac7306823a73ceed800f5fac9dfd
