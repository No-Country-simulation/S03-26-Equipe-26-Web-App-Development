package br.com.smartTrafficFlow.Smart_Traffic_Flow.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.common.aliasing.qual.Unique;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "usuario")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;

    @Column(unique = true)
    private String email;

    private String senha;
    private String provider; //local ou google

    @OneToMany(mappedBy = "usuario")
    private List<Vehicle> veiculos;

    // 2. Este método resolve o erro no seu JwtFilter
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Retorna a autoridade básica "ROLE_USER" para todos os usuários logados
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    // 3. Indica qual campo é usado como identificador (login)
    @Override
    public String getUsername() {
        return this.email;
    }

    // 4. Indica qual campo contém a senha criptografada
    @Override
    public String getPassword() {
        return this.senha;
    }

    // 5. Configurações de expiração e bloqueio (mantenha true para simplificar agora)
    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

}
