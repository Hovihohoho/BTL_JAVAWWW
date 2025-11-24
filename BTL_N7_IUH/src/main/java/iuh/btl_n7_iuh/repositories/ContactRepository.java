package iuh.btl_n7_iuh.repositories;

import iuh.btl_n7_iuh.entities.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
}

