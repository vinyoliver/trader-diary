package com.traderdiary.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Objects;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Entity
@NamedQueries({
        @NamedQuery(name = "findByCpf", query = "select u from Usuario u where u.cpf = :cpf and u.ativo = true"),
        @NamedQuery(name = "findByEmail", query = "select u from Usuario u where u.email = :email and u.ativo = true")
})
@JsonIgnoreProperties(value = {"password", "accountNonExpired", "accountNonLocked", "credentialsNonExpired", "enabled", "authorities", "username"})
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Usuario extends ModelBase implements UserDetails {

    @NotNull
    @Column(length = 50)
    private String nome;

    @Column(length = 50)
    private String sobrenome;

    @Column(length = 50)
    @NotNull
    private String email;

    @Column(length = 11, unique = true)
    private String cpf;

    @NotNull
    @Column(length = 100)
    private String senha;

    @ManyToOne
    private Empresa empresa;

    @NotNull
    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro = LocalDateTime.now();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario")
    private Set<Unidade> unidades = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Perfil perfil;

    public List<Autoridade> getAutoridades() {
        if (perfil != null) {
            final Set<Autoridade> autoridades = perfil.getAutoridades().stream().collect(Collectors.toSet());
            return new ArrayList<>(autoridades);
        }
        return new ArrayList<>();
    }

    @Override
    public Collection<Autoridade> getAuthorities() {
        return getAutoridades();
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return cpf;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getId(), this.getCpf());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) obj;
        if (this.getId() != null && other.getId() != null) {
            return Objects.equal(this.getId(), other.getId());
        }
        return Objects.equal(this.getCpf(), other.getCpf());
    }

    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("123456"));
    }

}
