package iuh.btl_n7_iuh.models;



import java.time.LocalDateTime;

public class TestimonialView {
    private Long id;
    private String fullName;
    private String productName;
    private String content;
    private Integer rating;
    private LocalDateTime createdAt;

    public TestimonialView(Long id, String fullName, String productName,
                           String content, Integer rating, LocalDateTime createdAt) {
        this.id = id;
        this.fullName = fullName;
        this.productName = productName;
        this.content = content;
        this.rating = rating;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getProductName() { return productName; }
    public String getContent() { return content; }
    public Integer getRating() { return rating; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}

