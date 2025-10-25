package iuh.btl_n7_iuh.repositories;

import iuh.btl_n7_iuh.entities.Account; // Cập nhật import
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByUsername(String username);
    Account findByEmail(String email);
}