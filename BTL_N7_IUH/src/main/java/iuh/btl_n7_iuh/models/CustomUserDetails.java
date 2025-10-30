package iuh.btl_n7_iuh.models;

import iuh.btl_n7_iuh.entities.Account;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomUserDetails implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private String fullName;
    private String role;
    private boolean enabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return enabled; }

    public static CustomUserDetails fromAccount(Account account) {
        return new CustomUserDetails(
                account.getId(),
                account.getUsername(),
                account.getPassword(),
                (account.getAccountDetail() != null ? account.getAccountDetail().getFullName() : account.getUsername()),
                account.getRole() != null ? account.getRole().getName() : "ROLE_USER",
                account.getIsEnabled() != null ? account.getIsEnabled() : true
        );
    }
}
