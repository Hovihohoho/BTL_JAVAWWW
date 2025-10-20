package iuh.btl_n7_iuh.repositories;


import iuh.btl_n7_iuh.entities.Comment;
import iuh.btl_n7_iuh.models.TestimonialView;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface TestimonialRepository extends JpaRepository<Comment, Long> {

    @Query("""
        select new iuh.btl_n7_iuh.models.TestimonialView(
            c.id,
            coalesce(ad.fullName, a.username),
            p.name,
            c.content,
            c.rating,
            c.createdAt
        )
        from Comment c
        join c.account a
        left join AccountDetail ad on ad.account.id = a.id
        left join c.product p
        order by c.createdAt desc
    """)
    List<TestimonialView> findLatest(Pageable pageable);
}

