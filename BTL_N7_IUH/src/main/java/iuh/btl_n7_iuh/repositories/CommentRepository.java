package iuh.btl_n7_iuh.repositories;

import iuh.btl_n7_iuh.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByOrderByCreatedAtDesc();
    @Modifying
    @Transactional
    @Query("DELETE FROM Comment c WHERE c.account.id = :accountId")
    void deleteByAccountId(@Param("accountId") Long accountId);
}
