package iuh.btl_n7_iuh.repositories;

import iuh.btl_n7_iuh.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    // ✅ Trả về Optional để dùng được .orElseThrow()
    Optional<Account> findByUsername(String username);

    Optional<Account> findByEmail(String email);
}
