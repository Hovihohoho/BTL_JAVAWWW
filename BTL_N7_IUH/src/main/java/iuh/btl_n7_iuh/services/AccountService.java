
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

                    public List<Account> findAll() {
                        return accountRepository.findAll();
                    }

                    @Transactional
                    public boolean deleteById(Long id) {

                        // Không tồn tại account => trả false
                        if (!accountRepository.existsById(id)) {
                            return false;
                        }

                        // Nếu tài khoản đã có đơn hàng => không cho xóa
                        if (orderRepository.existsByAccountId(id)) {
                            // Có thể log thêm ở đây
                            // log.warn("Không thể xóa account {} vì đã có orders", id);
                            return false;
                        }

                        try {
                            // 1) Xóa tất cả comment của account
                            commentRepository.deleteByAccountId(id);

                            // 2) Xóa account_detail nếu có
                            accountDetailRepository
                                    .findByAccountId(id)
                                    .ifPresent(accountDetailRepository::delete);

                            // 3) Xóa account
                            accountRepository.deleteById(id);

                            return true;
                        } catch (Exception e) {
                            // Nếu có lỗi ràng buộc khác
                            // e.printStackTrace();
                            return false;
                        }
                    }

                    public boolean updateRole(Long accountId, Long roleId) {
                        Optional<Account> accountOpt = accountRepository.findById(accountId);
                        if (!accountOpt.isPresent()) {
                            return false;
                        }
                        Optional<Role> roleOpt = roleRepository.findById(roleId);
                        if (!roleOpt.isPresent()) {
                            return false;
                        }
                        Account account = accountOpt.get();
                        account.setRole(roleOpt.get());
                        accountRepository.save(account);
                        return true;
                    }

                    @Transactional
                    public boolean updateEnabled(Long id, boolean enabled) {
                        return accountRepository.findById(id)
                                .map(acc -> {
                                    acc.setIsEnabled(enabled); // QUAN TRỌNG: đúng tên field
                                    return true;
                                })
                                .orElse(false);
                    }

                    /**
                     * Đảo trạng thái (true → false, false → true)
                     */
                    @Transactional
                    public boolean toggleEnabled(Long id) {
                        return accountRepository.findById(id)
                                .map(acc -> {
                                    boolean current = acc.getIsEnabled() != null && acc.getIsEnabled();
                                    acc.setIsEnabled(!current);
                                    return true;
                                })
                                .orElse(false);
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
                        account.setPassword(passwordEncoder.encode(newPassword));
                        account.setResetToken(null);
                        accountRepository.save(account);
                    }

                }