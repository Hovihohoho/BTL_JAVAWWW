package iuh.btl_n7_iuh.repositories;

import iuh.btl_n7_iuh.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
