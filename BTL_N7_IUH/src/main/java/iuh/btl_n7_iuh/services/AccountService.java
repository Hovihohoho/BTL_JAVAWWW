package iuh.btl_n7_iuh.services;

import iuh.btl_n7_iuh.entities.Account;
import iuh.btl_n7_iuh.entities.AccountDetail;
import iuh.btl_n7_iuh.entities.Role;
import iuh.btl_n7_iuh.repositories.AccountRepository;
import iuh.btl_n7_iuh.repositories.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public Account registerNewUser(Account account, String fullName, String phone, String address) {

        // Mã hóa mật khẩu
        account.setPassword(passwordEncoder.encode(account.getPassword()));

        // Gán ROLE_USER mặc định
        Role userRole = roleRepository.findByName("ROLE_USER");
        account.setRole(userRole);
        account.setIsEnabled(true);

        // Tạo chi tiết tài khoản
        AccountDetail detail = new AccountDetail();
        detail.setFullName(fullName);
        detail.setPhoneNumber(phone);
        detail.setAddress(address);
        detail.setAccount(account); // Liên kết 2 chiều

        // Gán chi tiết vào tài khoản
        account.setAccountDetail(detail);

        // Hibernate cascade ALL sẽ tự lưu AccountDetail
        return accountRepository.save(account);
    }
}
