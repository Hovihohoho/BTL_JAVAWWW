package iuh.btl_n7_iuh.services;

import iuh.btl_n7_iuh.entities.Account; // Cập nhật import
import iuh.btl_n7_iuh.repositories.AccountRepository; // Cập nhật import
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository; // Cập nhật Repository
    private final PasswordEncoder passwordEncoder;

    public void registerNewUser(Account account) { // Cập nhật tham số
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setIsEnabled(true);
        // Có thể gán role mặc định ở đây nếu cần
        accountRepository.save(account);
    }
}