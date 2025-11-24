package iuh.btl_n7_iuh.repositories;

import iuh.btl_n7_iuh.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByOrderByCreatedAtDesc();
}
