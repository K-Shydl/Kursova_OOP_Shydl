package ua.opnu.pract1shydl.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ua.opnu.pract1shydl.enums.AuthProvider;
import ua.opnu.pract1shydl.enums.Role;


import java.util.Collection;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyOAuth2User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> role.name());
    }

    @Override
    public String getPassword() { return null; }

    @Override
    public String getUsername() { return email; }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}