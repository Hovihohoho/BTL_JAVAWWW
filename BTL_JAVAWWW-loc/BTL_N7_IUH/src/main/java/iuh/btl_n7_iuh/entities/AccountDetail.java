package iuh.btl_n7_iuh.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Account_Details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "account_id", unique = true)
    private Account account;

    @Column(name = "full_name", length = 255)
    private String fullName;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column(length = 255)
    private String address;
}
