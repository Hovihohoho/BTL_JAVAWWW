package iuh.btl_n7_iuh.repositories;

import iuh.btl_n7_iuh.entities.AccountDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountDetailRepository extends JpaRepository<AccountDetail, Long> {
    Optional<AccountDetail> findByAccountId(Long id);
}
