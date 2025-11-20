package iuh.btl_n7_iuh.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "is_enabled")
    private Boolean isEnabled ;

    @Column(name = "reset_token")
    private String resetToken;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    private AccountDetail accountDetail;
}
