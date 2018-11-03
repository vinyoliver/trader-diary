package com.traderdiary.security;

import com.traderdiary.model.Unidade;
import com.traderdiary.model.Usuario;
import com.traderdiary.utils.JPAUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Component
public class SystemUserDAO implements UserDetailsService {

    @PersistenceContext
    private EntityManager em;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        final TypedQuery<Usuario> query = em.createNamedQuery("findByEmail", Usuario.class).setParameter("email", username);
        final Usuario usuario = JPAUtils.uniqueResultOrNull(query);
        if (usuario == null || (usuario.getEmpresa() != null && !usuario.getEmpresa().isAtivo())) {
            throw new UsernameNotFoundException("Usuário ou senha incorreto.");
        }
        return usuario;
    }

    private boolean possuiPeloMenosUmaUnidadeAtiva(Usuario usuario) {
        return usuario.getUnidades().stream().anyMatch(Unidade::isAtivo);
    }

}
