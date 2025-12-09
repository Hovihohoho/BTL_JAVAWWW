package iuh.btl_n7_iuh.services;

import iuh.btl_n7_iuh.entities.Comment;
import iuh.btl_n7_iuh.repositories.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public List<Comment> getAllComments() {
        return commentRepository.findAllByOrderByCreatedAtDesc();
    }

    public boolean deleteComment(Long id) {
        if (!commentRepository.existsById(id)) return false;
        commentRepository.deleteById(id);
        return true;
    }
}
