package iuh.btl_n7_iuh.services;

import iuh.btl_n7_iuh.entities.Account; // Cập nhật import
import iuh.btl_n7_iuh.entities.AccountDetail;
import iuh.btl_n7_iuh.entities.Role;
import iuh.btl_n7_iuh.repositories.AccountRepository; // Cập nhật import
import iuh.btl_n7_iuh.repositories.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository; // Cập nhật Repository
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
        detail.setAccount(account); // liên kết 2 chiều

        // Gán lại cho account
        account.setAccountDetail(detail);

        // ✅ Lưu account (Hibernate cascade ALL sẽ tự lưu AccountDetail)
        return accountRepository.save(account);
    }

    public Account findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    public void updateResetToken(String token, String email) throws Exception {
        Account account = findByEmail(email);
        if (account == null) {
            throw new Exception("Email không tồn tại");
        }
        account.setResetToken(token);
        accountRepository.save(account);
    }

    public Account getByResetToken(String token) {
        return accountRepository.findByResetToken(token);
    }

    public void updatePassword(Account account, String newPassword) {
        account.setPassword(newPassword); // nhớ mã hóa nếu dùng BCrypt
        account.setResetToken(null);
        accountRepository.save(account);
    }

}