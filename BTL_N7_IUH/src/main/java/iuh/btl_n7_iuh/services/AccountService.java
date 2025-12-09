
// src/main/java/iuh/btl_n7_iuh/services/AccountService.java
package iuh.btl_n7_iuh.services;
import iuh.btl_n7_iuh.entities.Account;
import iuh.btl_n7_iuh.entities.AccountDetail;
import iuh.btl_n7_iuh.entities.Role;
import iuh.btl_n7_iuh.repositories.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final AccountDetailRepository accountDetailRepository;
    private final CommentRepository commentRepository;
    private final OrderRepository orderRepository;
    private final CustomUserDetailsService customUserDetailsService;

    // ============================================================
    // 🔥 NEW — Tạo tài khoản (dùng trong /add)
    // ============================================================
    @Transactional
    public Account createAccount(Account account, Long roleId, String rawPassword) {

        // 1. Mã hóa mật khẩu
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new RuntimeException("Mật khẩu không được để trống");
        }
        account.setPassword(passwordEncoder.encode(rawPassword));

        // 2. Gán role
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role không tồn tại"));
        account.setRole(role);

        // 3. Gán trạng thái mặc định
        if (account.getIsEnabled() == null) {
            account.setIsEnabled(true);
        }

        // 4. Tạo AccountDetail từ form (KHÔNG được lấy lại từ account.getAccountDetail)
        AccountDetail detail = new AccountDetail();
        detail.setAccount(account);

        // 🔥 Form đã binding vào account.accountDetail → LẤY DỮ LIỆU TỪ ĐÂY
        if (account.getAccountDetail() != null) {
            detail.setFullName(account.getAccountDetail().getFullName());
            detail.setPhoneNumber(account.getAccountDetail().getPhoneNumber());
            detail.setAddress(account.getAccountDetail().getAddress());
        }

        // Gán vào account
        account.setAccountDetail(detail);

        return accountRepository.save(account);
    }




    // ============================================================
    // 🔥 NEW — Cập nhật tài khoản (dùng trong /edit/{id})
    // ============================================================
    @Transactional
    public boolean updateAccount(Long id, Account formAccount, Long roleId, String rawPassword) {

        return accountRepository.findById(id).map(acc -> {

            acc.setUsername(formAccount.getUsername());
            acc.setEmail(formAccount.getEmail());
            acc.setIsEnabled(formAccount.getIsEnabled());

            // role
            roleRepository.findById(roleId).ifPresent(acc::setRole);

            // đổi password nếu có
            if (rawPassword != null && !rawPassword.isBlank()) {
                acc.setPassword(passwordEncoder.encode(rawPassword));
            }

            // ===============================
            // 🔥 CẬP NHẬT ACCOUNT DETAIL
            // ===============================
            AccountDetail detail = acc.getAccountDetail();
            if (detail == null) {
                detail = new AccountDetail();
                detail.setAccount(acc);
                acc.setAccountDetail(detail);
            }

            detail.setFullName(formAccount.getAccountDetail().getFullName());
            detail.setPhoneNumber(formAccount.getAccountDetail().getPhoneNumber());
            detail.setAddress(formAccount.getAccountDetail().getAddress());

            return true;
        }).orElse(false);
    }



    // ============================================================
    // PHẦN CODE SẴN CỦA BẠN — GIỮ NGUYÊN
    // ============================================================

    public Account registerNewUser(Account account, String fullName, String phone, String address) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        Role userRole = roleRepository.findByName("ROLE_USER");
        account.setRole(userRole);
        account.setIsEnabled(true);

        AccountDetail detail = new AccountDetail();
        detail.setFullName(fullName);
        detail.setPhoneNumber(phone);
        detail.setAddress(address);
        detail.setAccount(account);

        account.setAccountDetail(detail);

        return accountRepository.save(account);
    }

    public Account findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    public Account findByUsername(String username) {
        return accountRepository.findByUsername(username).orElse(null);
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    // ✅ THÊM HÀM NÀY
    public Account findById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deleteById(Long id) {
        // Không tồn tại account
        if (!accountRepository.existsById(id)) {
            throw new RuntimeException("Tài khoản không tồn tại!");
        }

        // Có order → không được xoá
        if (orderRepository.existsByAccountId(id)) {
            throw new RuntimeException("Không thể xoá tài khoản vì đã phát sinh đơn hàng.");
        }

        try {
            // Xoá comment
            commentRepository.deleteByAccountId(id);

            // Xoá accountDetail
            accountDetailRepository.findByAccountId(id)
                    .ifPresent(accountDetailRepository::delete);

            // Xoá account
            accountRepository.deleteById(id);

        } catch (Exception e) {
            throw new RuntimeException("Không thể xoá tài khoản. Lỗi hệ thống!");
        }
    }


    public boolean updateRole(Long accountId, Long roleId) {
        Optional<Account> accountOpt = accountRepository.findById(accountId);
        Optional<Role> roleOpt = roleRepository.findById(roleId);

        if (!accountOpt.isPresent() || !roleOpt.isPresent()) return false;

        Account account = accountOpt.get();
        account.setRole(roleOpt.get());
        accountRepository.save(account);

        return true;
    }

    @Transactional
    public boolean updateEnabled(Long id, boolean enabled) {
        return accountRepository.findById(id)
                .map(acc -> {
                    acc.setIsEnabled(enabled);
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public boolean toggleEnabled(Long id) {
        return accountRepository.findById(id)
                .map(acc -> {
                    boolean now = acc.getIsEnabled() != null && acc.getIsEnabled();
                    acc.setIsEnabled(!now);
                    return true;
                })
                .orElse(false);
    }

    public void updateResetToken(String token, String email) throws Exception {
        Account account = findByEmail(email);
        if (account == null) throw new Exception("Email không tồn tại");

        account.setResetToken(token);
        accountRepository.save(account);
    }

    public Account getByResetToken(String token) {
        return accountRepository.findByResetToken(token);
    }

    public void updatePassword(Account account, String newPassword) {
        account.setPassword(passwordEncoder.encode(newPassword));
        account.setResetToken(null);
        accountRepository.save(account);
    }

    @Transactional
    public void updateProfile(String username, String fullName, String phone, String address) {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        AccountDetail detail = account.getAccountDetail();
        if (detail == null) {
            detail = new AccountDetail();
            detail.setAccount(account);
            account.setAccountDetail(detail);
        }

        detail.setFullName(fullName);
        detail.setPhoneNumber(phone);
        detail.setAddress(address);

        accountRepository.save(account);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails updatedUser = customUserDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken newAuth =
                new UsernamePasswordAuthenticationToken(
                        updatedUser,
                        auth.getCredentials(),
                        updatedUser.getAuthorities()
                );

        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }
}
